package com.example.project1.model.enity.product;

import com.example.project1.model.dto.view.product.ProductView;
import com.example.project1.utils.DataUtils;
import com.example.project1.utils.DateUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Table(name = "product_image")
@Entity
@Setter
@Getter
public class ProductImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "is_primary")
    private int isPrimary = 0;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false, insertable = false, updatable = false)
    @JsonIgnore
    private Product product;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false, updatable = false, insertable = false )
    @JsonIgnore
    private ProductView productView;

    public String getImageUrl() {
        return DataUtils.convertUrl(imageUrl);

    }
}
