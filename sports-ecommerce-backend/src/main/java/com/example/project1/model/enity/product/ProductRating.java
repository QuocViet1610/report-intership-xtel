package com.example.project1.model.enity.product;

import com.example.project1.model.BaseEntity;
import com.example.project1.model.enity.User.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Table(name = "product_rating")
@Entity
@Setter
@Getter
public class ProductRating extends BaseEntity {

    @Column(name = "rating")
    private Integer rating;

    @Column(columnDefinition = "TEXT")
    private String comment;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false, updatable = false, insertable = false )
    private Product product;


    @Column(name = "user_id", nullable = false)
    private Long userId;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, updatable = false, insertable = false )
    private User user;
}
