package com.example.project1.model.enity.User;

import com.example.project1.model.BaseEntity;
import com.example.project1.model.enity.product.ProductRating;
import com.example.project1.utils.DataUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.Set;

@Table(name = "users")
@Entity
@Data
@Setter
@Getter

public class User  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "password")
    private String password;

    @Column(name = "facebook_account_id")
    private String facebookAccountId;

    @Column(name = "google_account_id")
    private String googleAccountId;

    @Column(name = "avatar")
    private String avatar;

    @Column(name = "password_changed_at")
    private OffsetDateTime passwordChangedAt;

    @Column(name = "is_active")
    private Integer isActive;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private Set<ProductRating> productRatings ;

    public User(Long id, String fullName, String email, String phone, String password, String facebookAccountId, String googleAccountId, String avatar, OffsetDateTime passwordChangedAt, Integer isActive, OffsetDateTime createdAt, OffsetDateTime updatedAt, Set<ProductRating> productRatings) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.facebookAccountId = facebookAccountId;
        this.googleAccountId = googleAccountId;
        this.avatar = avatar;
        this.passwordChangedAt = passwordChangedAt;
        this.isActive = isActive;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.productRatings = productRatings;
    }

    public User() {
    }

    public String getAvatar() {
        return DataUtils.convertUrl("product/avatar-vo-danh-9.png");

    }
}
