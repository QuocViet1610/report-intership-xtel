package com.example.project1.model.enity.product;

import com.example.project1.utils.DataUtils;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Table(name = "product_rating_view")
@Entity
@Setter
@Getter
public class ProductRatingView {
    @Id
    private Long id; // Primary key (Nếu view có ID là khóa chính)

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "product_code")
    private String productCode;

    @Column(name = "product_price")
    private Double productPrice;

    @Column(name = "product_average_rating")
    private Double productAverageRating;

    @Column(name = "rating_value")
    private Integer ratingValue;

    @Column(name = "rating_comment")
    private String ratingComment;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "email")
    private String email;

    @Column(name = "avatar")
    private String avatar;

    @Column(name = "phone")
    private String phone;

    @Column(name = "created_at")
    private String createdAt;

    @Column(name = "updated_at")
    private String updatedAt;

    @Column(name = "is_active")
    private Integer isActive;

    public String getAvatar() {
        return DataUtils.convertUrl("product/avatar-vo-danh-9.png");

    }
}
