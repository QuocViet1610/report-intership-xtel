package com.example.project1.model.dto.request.Order;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderCreateRequest {
    private Long id;

    @NotNull(message = "User ID không được để trống")
    private Long userId;

    @NotNull(message = "Tổng giá không được để trống")
    private BigDecimal totalPrice;

    private BigDecimal totalDiscount = BigDecimal.ZERO;

    @NotNull(message = "Giá cuối cùng không được để trống")
    private BigDecimal finalPrice;

    @NotNull(message = "Trạng thái đơn hàng không được để trống")
    private Long statusId;

    @Size(max = 500, message = "Địa chỉ giao hàng không được quá 500 ký tự")
    private String shippingAddress;

    private Long couponId;

    @NotNull(message = "Phương thức thanh toán không được để trống")
    @Size(min = 3, max = 100, message = "Phương thức thanh toán phải có độ dài từ 3 đến 100 ký tự")
    private String paymentMethod;  // 'Cash on Delivery', 'VNPay'

    @NotNull(message = "Phương thức vận chuyển không được để trống")
    @Size(min = 3, max = 100, message = "Phương thức vận chuyển phải có độ dài từ 3 đến 100 ký tự")
    private String shippingMethod;  // 'Standard', 'Express'

    private String vnpTxnRef;
}
