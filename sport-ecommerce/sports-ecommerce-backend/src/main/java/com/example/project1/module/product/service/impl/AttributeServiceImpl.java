package com.example.project1.module.product.service.impl;

import com.example.project1.expection.ValidateException;
import com.example.project1.local.Translator;
import com.example.project1.mapper.product.AttributeMapper;
import com.example.project1.mapper.product.AttributeValueMapper;
import com.example.project1.model.dto.product.AttributeDto;
import com.example.project1.model.dto.product.AttributeValueDTO;
import com.example.project1.model.dto.product.ProductAttributeValueDto;
import com.example.project1.model.dto.request.product.AttributeCreateRequest;
import com.example.project1.model.dto.request.product.AttributeSearchRequest;
import com.example.project1.model.enity.product.Attribute;
import com.example.project1.model.enity.product.AttributeValue;
import com.example.project1.model.enity.product.ProductAttribute;
import com.example.project1.model.enity.product.ProductAttributeValue;
import com.example.project1.module.PageableCustom;
import com.example.project1.module.product.repository.AttributeRepository;
import com.example.project1.module.product.repository.AttributeValueRepository;
import com.example.project1.module.product.repository.ProductAttributeRepository;
import com.example.project1.module.product.repository.ProductAttributeValueRepository;
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
    ProductAttributeValueRepository productAttributeValueRepository;
    ProductAttributeRepository productAttributeRepository;
//    ProductAttributeValue

    @Override
    public void delete(Long id) {
        List<ProductAttribute> productAttributes = productAttributeRepository.findAllByAttributeId(id);
        if (productAttributes.size() > 0){
            throw new ValidateException(Translator.toMessage("Sản phẩm đang có thuộc tính, không thể xoá"));
        }else{
            Attribute attribute = attributeRepository.findById(id)
                    .orElseThrow(() -> new ValidateException(Translator.toMessage("Thuộc tính không tồn tại")));
            attributeRepository.delete(attribute);
        }

    }

    private void validateLogic(AttributeCreateRequest request, boolean isCreated) {
        if (isCreated) {
            if (attributeRepository.findByNameIgnoreCase(request.getName()).isPresent()) {
                throw new ValidateException(Translator.toMessage("Thuộc tính đã tồn tại"));
            }
            Integer maxDisplayOrder = attributeRepository.findMaxDisplayOrder().orElse(0);
            request.setDisplayOrder(maxDisplayOrder + 1);
        } else {
            if (attributeRepository.findByNameIgnoreCaseAndIdNot(request.getName(), request.getId()).isPresent()) {
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
            page.getContent().forEach(attribute -> {
                Set<AttributeValue> sortedValues = attribute.getValues().stream()
                        .sorted(Comparator.comparing(AttributeValue::getName, Comparator.nullsLast(String::compareTo)))  // Sắp xếp với xử lý null
                        .collect(Collectors.toCollection(LinkedHashSet::new));

                attribute.setValues(sortedValues);
            });

            return new PageImpl<>(attributeMapper.toDto(page.getContent()), pageable, page.getTotalElements());
        }
        return attributeMapper.toDto(attributeRepository.findAll(conditions, pageable.getSort()));
    }

    @Override
    public List<AttributeDto> findAll() {
        List<Attribute> attributes = attributeRepository.findAll();

        attributes.forEach(attribute -> {
            // Sắp xếp danh sách values theo tên và gán lại vào Set mới
            Set<AttributeValue> sortedValues = attribute.getValues().stream()
                    .sorted(Comparator.comparing(AttributeValue::getName))  // Sắp xếp theo tên của lớp AttributeValue
                    .collect(Collectors.toCollection(LinkedHashSet::new));  // Thu lại kết quả vào LinkedHashSet để giữ thứ tự

            // Cập nhật lại danh sách values sau khi sắp xếp
            attribute.setValues(sortedValues);
        });

//        List<AttributeValue> attributesvalue = attributeValueRepository.findAllByAttributeId();
        return attributeMapper.toDto(attributes);
    }

    @Override
    public List<AttributeValueDTO> getAttributeValue(Long id) {
        return attributeValueMapper.toDto(attributeValueRepository.findAllByAttributeId(id));
    }

    @Override
    public AttributeDto getDetail(Long id) {
        return attributeMapper.toDto(attributeRepository.findById(id).get());
    }

    @Override
    public Boolean productAttributeExist(Long id) {
//        List<ProductAttributeValue> productAttributeValues = productAttributeValueRepository.findAll();

        List<ProductAttributeValue> productAttributeValues = productAttributeValueRepository.findAllByAttributeValueId(id);

        return productAttributeValues.size() > 0 ? true : false;
    }



}
