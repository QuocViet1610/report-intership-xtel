package com.example.project1.model.dto.User;

import com.example.project1.utils.DataUtils;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
@Entity
@Table(name = "user_order_summary")
public class UserView {

    @Id
    @Column(name = "id")  // user id
    private Long id;

    @Column(name = "email")
    private String email;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "phone")
    private String phone;

    @Column(name = "avatar")
    private String avatar;

    @Column(name = "total_orders")
    private Integer totalOrders;

    @Column(name = "total_spent")
    private Double totalSpent;

    @Column(name = "total_products_ordered")
    private Integer totalProductsOrdered;

    @Column(name = "is_active")
    private Integer isActive;

    public String getAvatar() {
        return DataUtils.convertUrl(avatar == null ? "product/avatar-vo-danh-9.png" : avatar);
    }
}
