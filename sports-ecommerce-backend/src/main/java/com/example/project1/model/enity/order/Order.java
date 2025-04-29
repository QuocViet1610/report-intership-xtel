package com.example.project1.model.enity.order;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Set;

@Entity
@Table(name = "Orders")
@Data
@Getter
@Setter
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "order_date", nullable = false, updatable = false)
    private OffsetDateTime orderDate;

    @Column(name = "total_price", nullable = false)
    private BigDecimal totalPrice;

    @Column(name = "total_discount", nullable = false)
    private BigDecimal totalDiscount;

    @Column(name = "final_price", nullable = false)
    private BigDecimal finalPrice;

    @Column(name = "status_id", nullable = false)
    private Long statusId; // '1: Đang chờ, 2: Đang xử lý, 3: Đã chuyển, 4: Đã giao, 5: Đã hủy, 6: Đã hoàn tiền, 7: Không thành công'

    @Column(name = "shipping_address")
    private String shippingAddress;

    @Column(name = "coupon_id")
    private Long couponId;
    @Column(name = "total_product")
    private Long totalProduct;

    @Column(name = "payment_method", nullable = false)
    private String paymentMethod;  // 'Cash on Delivery', 'VNPay'

    @Column(name = "shipping_method", nullable = false)
    private String shippingMethod;  // 'Standard', 'Express'

    @Column(name = "created_at", updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    @Column(name = "vnp_txn_ref")
    private String vnpTxnRef;

    @Column(name = "order_code")
    private String orderCode;

    @JsonIgnore
    @OneToMany(mappedBy = "order", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private Set<OrderDetail> orderDetails;

}
