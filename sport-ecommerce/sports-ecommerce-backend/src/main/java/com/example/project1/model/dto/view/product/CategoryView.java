package com.example.project1.model.dto.view.product;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "category_view")
@Getter
@Setter
public class CategoryView {
    @Id
    private Long id; // ID của danh mục

    private String categoryName;
    private Long parentId;
    private String parentName;
    private String fullParentId;
    private String image;
    private Integer level;

}
