package com.example.project1.module.product.service.impl;

import com.example.project1.expection.ValidateException;
import com.example.project1.local.Translator;
import com.example.project1.mapper.product.ProductRatingMapper;
import com.example.project1.model.dto.product.ProductRatingDto;
import com.example.project1.model.dto.request.product.ProductRatingCreateRequest;
import com.example.project1.model.dto.request.product.ProductRatingSearchRequest;
import com.example.project1.model.enity.product.ProductRating;
import com.example.project1.module.PageableCustom;
import com.example.project1.module.product.repository.ProductRatingRepository;
import com.example.project1.module.product.repository.ProductRepository;
import com.example.project1.module.product.service.ProductRatingService;
import com.example.project1.utils.TokenUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductRatingServiceImpl implements ProductRatingService {

    private final ProductRatingRepository productRatingRepository;
    private final ProductRatingMapper productRatingMapper;
    private final ProductRepository productRepository;
    private final TokenUtil tokenUtil;

    private void validateLogic(ProductRatingCreateRequest request, boolean isCreated) {
        if (productRepository.findById(request.getProductId()).isEmpty()){
            throw new ValidateException(Translator.toMessage("Sản phẩm không tồn tại"));
        }
        request.setUserId(tokenUtil.getCurrentUserId());
    }

    @Override
    public ProductRatingDto create(ProductRatingCreateRequest request) {
        ProductRating productRating = productRatingRepository.save(productRatingMapper.toCreate(request));
        return productRatingMapper.toDto(productRating);
    }
    @Override
    public void delete(Long id) {

    }

    @Override
    public Object search(ProductRatingSearchRequest searchRequest, PageableCustom pageable) {
//        Map<String, String> mapCondition = new HashMap<>();
//        if (!DataUtils.isNullOrEmpty(searchRequest.getSearchText())) {
//            mapCondition.put("categoryName", searchRequest.getSearchText());
//        }
//        Specification<ProductRating> conditions = Specification.where(SearchSpecificationUtil.<ProductRating>alwaysTrue())
//                .and(SearchSpecificationUtil.or(mapCondition));
//        if (!DataUtils.isNullOrEmpty(pageable) && !pageable.isFindAll()) {
//            Page<ProductRating> page = categoryViewRepository.findAll(conditions, pageable);
//            return new PageImpl<>(page.getContent(), pageable, page.getTotalElements());
//        }
//        return categoryViewRepository.findAll(conditions, pageable.getSort());
    return null;
    }


}
