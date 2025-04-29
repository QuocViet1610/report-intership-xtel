package com.example.project1.model.dto.respone;
import com.example.project1.model.enity.order.OrderDetail;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Set;

@Data
public class OrderResponse {

    private Long id;

    private Long userId;

    private OffsetDateTime orderDate;

    private BigDecimal totalPrice;

    private BigDecimal totalDiscount;

    private BigDecimal finalPrice;

    private Long statusId; // '1: Đang chờ, 2: Đang xử lý, 3: Đã chuyển, 4: Đã giao, 5: Đã hủy, 6: Đã hoàn tiền, 7: Không thành công'

    private String shippingAddress;

    private Long couponId;

    private String paymentMethod;  // 'Cash on Delivery', 'VNPay'

    private String shippingMethod;  // 'Standard', 'Express'

    private OffsetDateTime createdAt;

    private OffsetDateTime updatedAt;

    private String orderCode;
    private String vnpTxnRef;

    private Long totalProduct;

    private Set<OrderDetailResponse> orderDetails;


}
