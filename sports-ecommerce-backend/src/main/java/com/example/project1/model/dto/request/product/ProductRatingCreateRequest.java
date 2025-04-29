package com.example.project1.model.dto.request.product;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ProductRatingCreateRequest {
    @NotNull(message = "Rating không được để trống")
    @Min(value = 1, message = "Rating tối thiểu là 1 sao")
    @Max(value = 5, message = "Rating tối đa là 5 sao")
    private Integer rating;

    @Size(max = 1000, message = "Bình luận không vượt quá 1000 ký tự")
    private String comment;

    @NotNull(message = "productId không được để trống")
    private Long productId;

    private Long userId;
}
