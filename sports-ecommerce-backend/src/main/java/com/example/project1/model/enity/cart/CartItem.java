package com.example.project1.model.enity.cart;
import com.example.project1.model.enity.product.Product;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Entity
@Table(name = "cart_items")
@Getter
@Setter
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", insertable = false, updatable = false)
    private Cart cart;

    @Column(name = "cart_id", nullable = false)
    private Long cartId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", insertable = false, updatable = false)
    private Product product;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "product_variant_id")
    private Long productVariantId;

    @Column(name = "quantity", nullable = false)
    private Long quantity;

//    @Column(name = "price", nullable = false)
//    private BigDecimal price;

//    @Column(name = "total", nullable = false)
//    private BigDecimal total;
}
