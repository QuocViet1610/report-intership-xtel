package com.example.project1.model.dto.request.product;



import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class AttributeCreateRequest  {

    private Long id;

    @NotBlank(message = "error.product.attribute.required")
    @Size(min = 3, max = 255, message = "error.product.attribute.size ")
    private String name;

    private String description;

    private Integer displayOrder;

    List<String> value;

}
