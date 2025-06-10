package com.example.project1.model.dto.respone;

import lombok.Data;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@Data
public class ListOrderResponse {
    private Long id;

    private Long userId;

    private OffsetDateTime orderDate;

    private BigDecimal totalPrice;

    private BigDecimal totalDiscount;

    private BigDecimal finalPrice;

    private Long orderStatus; // '1: Đang chờ, 2: Đang xử lý, 3: Đã chuyển, 4: Đã giao, 5: Đã hủy, 6: Đã hoàn tiền, 7: Không thành công'

    private String shippingAddress;

    private Long couponId;

    private String paymentMethod;

    private String shippingMethod;

    private OffsetDateTime createdAt;

    private OffsetDateTime updatedAt;

    private String orderCode;
    private String vnpTxnRef;

    private List<String> urlImage;
    private Long totalProduct;

}
