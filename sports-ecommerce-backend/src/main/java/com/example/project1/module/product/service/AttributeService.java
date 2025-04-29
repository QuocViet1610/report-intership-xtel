package com.example.project1.module.product.service;
import com.example.project1.model.dto.product.AttributeDto;
import com.example.project1.model.dto.product.AttributeValueDTO;
import com.example.project1.model.dto.request.product.AttributeCreateRequest;
import com.example.project1.model.dto.request.product.AttributeSearchRequest;
import com.example.project1.module.PageableCustom;

import java.util.List;

public interface AttributeService {
    void delete(Long id);
    AttributeDto create(AttributeCreateRequest request);
     AttributeDto update(AttributeCreateRequest request, Long id) ;
    Object search(AttributeSearchRequest searchRequest, PageableCustom pageable);

    List<AttributeDto> findAll();

    List<AttributeValueDTO> getAttributeValue(Long id);
}
