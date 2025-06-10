package com.example.project1.model.dto.request.product;

import com.example.project1.expection.ValidateException;
import com.example.project1.local.Translator;
import com.example.project1.model.enity.product.ProductAttribute;
import com.example.project1.model.enity.product.ProductVariant;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ProductCreateRequest {
    private Long id;
    private String code;

    @NotBlank(message = "error.product.name.required")
    @Size(min = 3, max = 255, message = "error.product.name.size")
    private String name;

    @NotNull(message = "error.product.categoryId.required")
    private Long categoryId;

    @NotNull(message = "error.product.price.required")
    @Positive(message = "error.product.price.positive")
    private BigDecimal price;

    @NotNull(message = "error.product.costPrice.required")
    @Positive(message = "error.product.costPrice.positive")
    private BigDecimal costPrice;
    private String description;

    private Integer isActive;
    private Long brandId;
    private Long supplierId;
    @NotNull(message = "error.product.genderId.required")
    private Long genderId;
    private Long totalSold;
    private BigDecimal totalRating;
    @NotNull(message = "error.product.stock.required")
    @Positive(message = "error.product.stock.positive")
    private Long stock;
    private BigDecimal discountPrice;

    @Positive(message = "error.product.weight.positive")
    private BigDecimal weight;


    private List<AttributeProductCreatRequest> attributeCreatRequests;

    private List<ProductVariantCreateRequest> productVariantCreateRequests;
    public void validate() {
        // Kiểm tra name
        if (name == null || name.trim().isEmpty()) {
            throw new ValidateException(Translator.toMessage("error.product.name.required"));
        } else if (name.length() < 3 || name.length() > 255) {
            throw new ValidateException(Translator.toMessage("error.product.name.size"));
        }

        // Kiểm tra categoryId
        if (categoryId == null) {
            throw new ValidateException(Translator.toMessage("error.product.categoryId.required"));
        }

        // Kiểm tra price
        if (price == null) {
            throw new ValidateException(Translator.toMessage("error.product.price.required"));
        } else if (price.compareTo(BigDecimal.ZERO) < 0) {
            throw new ValidateException(Translator.toMessage("error.product.price.positive"));
        }

        // Kiểm tra costPrice
        if (costPrice == null) {
            throw new ValidateException(Translator.toMessage("error.product.costPrice.required"));
        } else if (costPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new ValidateException(Translator.toMessage("error.product.costPrice.positive"));
        }

        // Kiểm tra genderId
        if (genderId == null) {
            throw new ValidateException(Translator.toMessage("error.product.genderId.required"));
        }

        // Kiểm tra stock
        if (stock == null) {
            throw new ValidateException(Translator.toMessage("error.product.stock.required"));
        } else if (stock < 0) {
            throw new ValidateException(Translator.toMessage("error.product.stock.positive"));
        }

        // Kiểm tra weight
        if (weight != null && weight.compareTo(BigDecimal.ZERO) < 0) {
            throw new ValidateException(Translator.toMessage("error.product.weight.positive"));
        }


    }
}
