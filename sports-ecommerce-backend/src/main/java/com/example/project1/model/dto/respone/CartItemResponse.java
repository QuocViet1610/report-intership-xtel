package com.example.project1.model.dto.respone;

import com.example.project1.model.dto.view.product.ProductViewDto;
import lombok.Data;

@Data
public class CartItemResponse {
    private Long id;

    private Long productId;

    private Long productVariantId;

    private Long quantity;

    private ProductViewDto productView;
}
