package com.example.project1.module.product.service.impl;

import com.example.project1.expection.ValidateException;
import com.example.project1.local.Translator;
import com.example.project1.mapper.product.ProductRatingMapper;
import com.example.project1.model.dto.product.ProductRatingDto;
import com.example.project1.model.dto.product.ProductRatingSearch;
import com.example.project1.model.dto.request.Order.OrderSearchRequest;
import com.example.project1.model.dto.request.product.ProductRatingCreateRequest;
import com.example.project1.model.dto.request.product.ProductRatingSearchRequest;
import com.example.project1.model.enity.order.OrderDetail;
import com.example.project1.model.enity.order.OrderView;
import com.example.project1.model.enity.product.Product;
import com.example.project1.model.enity.product.ProductRating;
import com.example.project1.model.enity.product.ProductRatingView;
import com.example.project1.module.Order.repository.OrderDetailRepository;
import com.example.project1.module.Order.repository.OrderRepository;
import com.example.project1.module.PageableCustom;
import com.example.project1.module.product.repository.ProductRatingRepository;
import com.example.project1.module.product.repository.ProductRatingViewRepository;
import com.example.project1.module.product.repository.ProductRepository;
import com.example.project1.module.product.service.ProductRatingService;
import com.example.project1.utils.DataUtils;
import com.example.project1.utils.SearchSpecificationUtil;
import com.example.project1.utils.TokenUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductRatingServiceImpl implements ProductRatingService {

    private final ProductRatingRepository productRatingRepository;
    private final ProductRatingMapper productRatingMapper;
    private final ProductRepository productRepository;
    private final TokenUtil tokenUtil;
    private final ProductRatingViewRepository productRatingViewRepository;
    private final OrderRepository orderRepository;

    private void validateLogic(ProductRatingCreateRequest request, boolean isCreated) {
        if (productRepository.findById(request.getProductId()).isEmpty()){
            throw new ValidateException(Translator.toMessage("Sản phẩm không tồn tại"));
        }
        request.setUserId(tokenUtil.getCurrentUserId());
    }

    public Object create(ProductRatingCreateRequest productRatingDto){
        validateLogic(productRatingDto, true);
        productRatingDto.setIsActive(1);
        ProductRating productRating = productRatingMapper.toCreate(productRatingDto);
        ProductRating productRatingSave = productRatingRepository.save(productRating);
        Product product = productRepository.findById(productRatingDto.getProductId())
                .orElseThrow(() -> new ValidateException(Translator.toMessage("Sản phẩm không tồn tại")));

        BigDecimal averageProduct = productRatingRepository.calculateAverageRating(productRatingDto.getProductId());
        product.setTotalRating(averageProduct);
        productRepository.save(product);
        return productRatingSave;
    }

public Object getAllByProduct(Long productId){
    return  productRatingViewRepository.findAllByProductIdAndIsActive(productId, 1);
}

    public Object search(ProductRatingSearch searchRequest, PageableCustom pageable) {
        Map<String, String> mapCondition = new HashMap<>();
        if (!DataUtils.isNullOrEmpty(searchRequest.getSearchText())) {
            mapCondition.put("productName", searchRequest.getSearchText());
            mapCondition.put("email", searchRequest.getSearchText());
            mapCondition.put("productCode", searchRequest.getSearchText());
            mapCondition.put("ratingComment", searchRequest.getSearchText());
            mapCondition.put("fullName", searchRequest.getSearchText());
        }
        Specification<ProductRatingView> conditions = Specification.where(SearchSpecificationUtil.<ProductRatingView>alwaysTrue())
                .and(SearchSpecificationUtil.or(mapCondition));
        if (!DataUtils.isNullOrEmpty(pageable) && !pageable.isFindAll()) {
            Page<ProductRatingView> page = productRatingViewRepository.findAll(conditions, pageable);
            return new PageImpl<>(page.getContent(), pageable, page.getTotalElements());
        }
        return productRatingViewRepository.findAll(conditions, pageable.getSort());
    }
}
