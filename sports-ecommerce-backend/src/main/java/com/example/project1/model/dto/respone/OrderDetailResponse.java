package com.example.project1.model.dto.respone;

import com.example.project1.model.dto.view.product.ProductViewDto;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderDetailResponse {

    private Long orderItemId;

    private Long orderId;

    private Long productId;

    private Long productVariantId;

    private Integer quantity;

    private BigDecimal price;

    private BigDecimal total;

    private ProductViewDto productView;
}
