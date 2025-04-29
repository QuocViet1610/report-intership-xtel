package com.example.project1.model.dto.product;
import com.example.project1.model.dto.BaseDto;
import jakarta.persistence.Column;
import lombok.Data;


@Data
public class ProductRatingDto extends BaseDto {

    private Long productId;

    private Integer rating;

    private String comment;

    private Long userId;
}

