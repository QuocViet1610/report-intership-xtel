package com.example.project1.model.dto.request.product;



import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class AttributeCreateRequest  {

    private Long id;

    @NotBlank(message = "Tên thuộc tính không được để trống")
    @Size(max = 255, message = "Tên thuộc tính tối đa 255 ký tự")
    private String name;

    private String description;

    private Integer displayOrder;

    List<String> value;

}
