package com.example.project1.model.dto.cart;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartItemCreateRequest {

    private Long id;

    @NotNull(message = "Sản phẩm không được để trống")
    private Long productId;

    private Long productVariantId;
    @NotNull(message = "Số lượng không được để trống")
    private Long quantity;

//    private BigDecimal price;
//    private BigDecimal total;
}
