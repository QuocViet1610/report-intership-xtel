package com.example.project1.module.product.service.impl;

import com.example.project1.expection.ValidateException;
import com.example.project1.local.Translator;
import com.example.project1.mapper.product.ProductVariantMapper;
import com.example.project1.model.dto.product.ProductVariantDto;
import com.example.project1.model.dto.request.product.ProductVariantCreateRequest;
import com.example.project1.model.dto.request.product.ProductVariantUpdate;
import com.example.project1.model.enity.product.Product;
import com.example.project1.model.enity.product.ProductVariant;
import com.example.project1.module.product.repository.ProductVariantRepository;
import com.example.project1.module.product.service.ProductVariantService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductVariantServiceImpl implements ProductVariantService {

    private final ProductVariantRepository productVariantRepository;
    private final ProductVariantMapper productVariantMapper;

    @Override
    public void delete(Long id) {
        ProductVariant product = productVariantRepository.findById(id)
                .orElseThrow(() -> new ValidateException(Translator.toMessage("Biến thể sản phẩm không tồn tại ")));
        productVariantRepository.delete(product);
    }

    @Override
    public ProductVariant update(Long id, ProductVariantUpdate productVariantCreateRequest) {
        ProductVariant productVariant = productVariantRepository.findById(id)
                .orElseThrow(() -> new ValidateException(Translator.toMessage("Biến thể sản phẩm không tồn tại ")));
        productVariant.setName(productVariantCreateRequest.getName());
        productVariant.setCostPrice(productVariantCreateRequest.getCostPrice());
        productVariant.setPrice(productVariantCreateRequest.getPrice());
        productVariant.setQuantity(productVariantCreateRequest.getQuantity());
        ProductVariant newProductVariant = productVariantRepository.save(productVariant);
        return newProductVariant;
    }

    @Override
    public ProductVariant getDetail(Long id) {
        ProductVariant productVariant = productVariantRepository.findById(id)
                .orElseThrow(() -> new ValidateException(Translator.toMessage("Biến thể sản phẩm không tồn tại ")));
        return productVariant;
    }


}
