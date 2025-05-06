package com.example.project1.model.enity.order;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "order_user_view")
@Data
@Getter
@Setter
public class OrderView {
    @Id
    @Column(name = "order_id")
    private Integer id;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "image")
    private String avatar;  // Represents the user's avatar

    @Column(name = "user_full_name")
    private String userFullName;

    @Column(name = "order_code")
    private String orderCode;

    @Column(name = "user_email")
    private String userEmail;

    @Column(name = "phone")
    private String phone;

    @Column(name = "order_date")
    private LocalDateTime orderDate;

    @Column(name = "total_price")
    private BigDecimal totalPrice;

    @Column(name = "total_discount")
    private BigDecimal totalDiscount;

    @Column(name = "final_price")
    private BigDecimal finalPrice;

    @Column(name = "status_id")
    private Integer statusId;

    @Column(name = "shipping_address")
    private String shippingAddress;

    @Column(name = "coupon_id")
    private Integer couponId;

    @Column(name = "payment_method")
    private String paymentMethod;

    @Column(name = "shipping_method")
    private String shippingMethod;

    @Column(name = "vnp_txn_ref")
    private String vnpTxnRef;

    @Column(name = "order_created_at")
    private LocalDateTime createdAt;

    @Column(name = "order_updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "user_created_at")
    private LocalDateTime createdBy;

    @Column(name = "user_updated_at")
    private LocalDateTime updatedBy;

    @Column(name = "user_status")
    private Integer userStatus;

    @Column(name = "total_product")
    private Integer totalProduct;
}
