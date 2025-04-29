package com.example.project1.module.product.service.impl;

import com.example.project1.expection.ValidateException;
import com.example.project1.local.Translator;
import com.example.project1.mapper.product.AttributeMapper;
import com.example.project1.mapper.product.AttributeValueMapper;
import com.example.project1.model.dto.product.AttributeDto;
import com.example.project1.model.dto.product.AttributeValueDTO;
import com.example.project1.model.dto.request.product.AttributeCreateRequest;
import com.example.project1.model.dto.request.product.AttributeSearchRequest;
import com.example.project1.model.dto.request.product.BrandCreateRequest;
import com.example.project1.model.dto.view.product.CategoryView;
import com.example.project1.model.enity.product.Attribute;
import com.example.project1.model.enity.product.AttributeValue;
import com.example.project1.module.PageableCustom;
import com.example.project1.module.product.repository.AttributeRepository;
import com.example.project1.module.product.repository.AttributeValueRepository;
import com.example.project1.module.product.service.AttributeService;
import com.example.project1.utils.DataUtils;
import com.example.project1.utils.SearchSpecificationUtil;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AttributeServiceImpl implements AttributeService {

    AttributeRepository attributeRepository;
    AttributeMapper attributeMapper;
    AttributeValueRepository attributeValueRepository;
    AttributeValueMapper attributeValueMapper;
    @Override
    public void delete(Long id) {
        Attribute attribute = attributeRepository.findById(id)
                .orElseThrow(() -> new ValidateException(Translator.toMessage("Thuộc tính không tồn tại")));
        attributeRepository.delete(attribute);
    }

    private void validateLogic(AttributeCreateRequest request, boolean isCreated) {
        if (isCreated) {
            if (attributeRepository.findByName(request.getName()).isPresent()) {
                throw new ValidateException(Translator.toMessage("Thuộc tính đã tồn tại"));
            }
            Integer maxDisplayOrder = attributeRepository.findMaxDisplayOrder().orElse(0);
            request.setDisplayOrder(maxDisplayOrder + 1);
        } else {
            if (attributeRepository.findByNameAndIdNot(request.getName(), request.getId()).isPresent()) {
                throw new ValidateException(Translator.toMessage("Thuộc tính đã tồn tại"));
            }
        }
    }

    @Override
    public AttributeDto create(AttributeCreateRequest request) {
        this.validateLogic(request, true);
        Attribute attribute = attributeRepository.save(attributeMapper.toCreate(request));
        if (!request.getValue().isEmpty()){
            Set<AttributeValue> attributeValues = request.getValue().stream()
                    .map(value -> {
                        AttributeValue attributeValue = new AttributeValue();
                        attributeValue.setName(value.trim());
                        attributeValue.setAttributeId(attribute.getId());
                        return attributeValue;
                    })
                    .collect(Collectors.toSet());

            attributeValueRepository.saveAll(attributeValues);
            attribute.setValues(attributeValues);
        }

        return attributeMapper.toDto(attribute);
    }

    @Override
    public AttributeDto update(AttributeCreateRequest request, Long id) {
        return attributeRepository.findById(id)
                .map(attribute -> {
                    request.setId(id);
                    this.validateLogic(request, false);

                    attributeMapper.partialUpdate(attribute, request);
                    attributeRepository.save(attribute);
                    saveAttributeValues(attribute, request.getValue());

                    return attributeMapper.toDto(attribute);
                })
                .orElseThrow(() -> new ValidateException(Translator.toMessage("Thuộc tính không tồn tại")));
    }

    private void saveAttributeValues(Attribute attribute, List<String> newValueList) {
        Set<AttributeValue> existingValues = new HashSet<>(attributeValueRepository.findAllByAttributeId(attribute.getId()));
        Set<String> existingValueNames = existingValues.stream()
                .map(AttributeValue::getName)
                .collect(Collectors.toSet());

        Set<String> newValues = new HashSet<>(newValueList);

        Set<AttributeValue> toDelete = existingValues.stream()
                .filter(value -> !newValues.contains(value.getName()))
                .collect(Collectors.toSet());

        Set<AttributeValue> toAdd = newValues.stream()
                .filter(value -> !existingValueNames.contains(value))
                .map(value -> {
                    AttributeValue attributeValue = new AttributeValue();
                    attributeValue.setName(value);
                    attributeValue.setAttributeId(attribute.getId());
                    return attributeValue;
                })
                .collect(Collectors.toSet());

        if (!toDelete.isEmpty()) {
            existingValues.removeAll(toDelete);
            attributeValueRepository.deleteAll(toDelete);
        }


        if (!toAdd.isEmpty()) {
            existingValues.addAll(toAdd);
            attributeValueRepository.saveAll(toAdd);
        }
        existingValues.forEach(attributeValue -> attributeValue.setAttribute(null));
        attribute.setValues(existingValues);
    }


    @Override
    public Object search(AttributeSearchRequest searchRequest, PageableCustom pageable) {
        Map<String, String> mapCondition = new HashMap<>();
        if (!DataUtils.isNullOrEmpty(searchRequest.getSearchText())) {
            mapCondition.put("name", searchRequest.getSearchText());
        }
        Specification<Attribute> conditions = Specification.where(SearchSpecificationUtil.<Attribute>alwaysTrue())
                .and(SearchSpecificationUtil.or(mapCondition));
        if (!DataUtils.isNullOrEmpty(pageable) && !pageable.isFindAll()) {
            Page<Attribute> page = attributeRepository.findAll(conditions, pageable);
            return new PageImpl<>(attributeMapper.toDto(page.getContent()), pageable, page.getTotalElements());
        }
        return attributeMapper.toDto(attributeRepository.findAll(conditions, pageable.getSort()));
    }

    @Override
    public List<AttributeDto> findAll() {
        List<Attribute> attributes = attributeRepository.findAll();
        attributes.forEach(attribute -> attribute.getValues().size());

//        List<AttributeValue> attributesvalue = attributeValueRepository.findAllByAttributeId();
        return attributeMapper.toDto(attributes);
    }

    @Override
    public List<AttributeValueDTO> getAttributeValue(Long id) {
        return attributeValueMapper.toDto(attributeValueRepository.findAllByAttributeId(id));
    }

}
