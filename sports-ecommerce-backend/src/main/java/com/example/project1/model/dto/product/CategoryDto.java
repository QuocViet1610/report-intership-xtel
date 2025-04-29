package com.example.project1.model.dto.product;
import lombok.Data;

@Data
public class CategoryDto {
    private String id;
    private String name;
    private Long parentId;
    private String fullParentId;
    private String image;
    private Long level;
}
