package com.example.project1.model.dto.request.Order;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderRequest {

    private Long id;


    private Long userId;

    @NotNull(message = "Tổng giá không được để trống")
    private BigDecimal totalPrice;

    private BigDecimal totalDiscount = BigDecimal.ZERO;

    @NotNull(message = "Giá cuối cùng không được để trống")
    private BigDecimal finalPrice;

    @NotNull(message = "Trạng thái đơn hàng không được để trống")
    private Long statusId;

    private String shippingAddress;

    private Long couponId;

    @NotNull(message = "Phương thức thanh toán không được để trống")
    private String paymentMethod;

    @NotNull(message = "Phương thức vận chuyển không được để trống")
    private String shippingMethod;

    private String vnpTxnRef;

    private String orderCode;

    @NotNull(message = "Chi tiết đơn hàng không được để trống")
    private List<OrderDetailCreateRequest> orderDetails;

    private Long totalProduct;
}
