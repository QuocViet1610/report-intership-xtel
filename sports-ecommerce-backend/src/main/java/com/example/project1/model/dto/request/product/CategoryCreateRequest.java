package com.example.project1.model.dto.request.product;

import com.example.project1.expection.ValidateException;
import com.example.project1.local.Translator;
import lombok.Data;

@Data
public class CategoryCreateRequest {
    private Long id;
    private String name;
    private Long parentId;
    private String fullParentId;
    private String image;
    private Long level;

    public void validate() {
        if (name == null || name.trim().isEmpty()) {
            throw new ValidateException(Translator.toMessage("error.category.name.required"));
        }
    }
}
