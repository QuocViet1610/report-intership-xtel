package com.example.project1.module.product.service.impl;

import com.example.project1.expection.ValidateException;
import com.example.project1.local.Translator;
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

    @Override
    public void delete(Long id) {
        ProductVariant product = productVariantRepository.findById(id)
                .orElseThrow(() -> new ValidateException(Translator.toMessage("Biến thể sản phẩm không tồn tại ")));
        productVariantRepository.delete(product);
    }
}
