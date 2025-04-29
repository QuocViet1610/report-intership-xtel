package com.example.project1.model.dto.request.product;

import com.example.project1.expection.ValidateException;
import com.example.project1.local.Translator;
import com.example.project1.model.dto.BaseDto;
import com.example.project1.model.enity.product.ProductVariantAttribute;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Data
public class ProductVariantCreateRequest extends BaseDto {
    private Long id;
    @NotNull(message = "Product ID không được để trống")
    private Long productId;

    @NotNull(message = "Giá bán không được để trống")
    @DecimalMin(value = "0.0", inclusive = false, message = "Giá bán phải lớn hơn 0")
    private BigDecimal price;

    @NotNull(message = "Giá vốn không được để trống")
    @DecimalMin(value = "0.0", inclusive = false, message = "Giá vốn phải lớn hơn 0")
    private BigDecimal costPrice;

    @NotNull(message = "Số lượng không được để trống")
    @Min(value = 0, message = "Số lượng không thể nhỏ hơn 0")
    private Long quantity;

    private String image;
    private List<Long> variantAttributeIds;


    public void validate() {

        // Kiểm tra price
        if (price == null) {
            throw new ValidateException(Translator.toMessage("error.product.price_variant.required"));
        } else if (price.compareTo(BigDecimal.ZERO) < 0) {
            throw new ValidateException(Translator.toMessage("error.product.price_variant.positive"));
        }

        // Kiểm tra costPrice
        if (costPrice == null) {
            throw new ValidateException(Translator.toMessage("error.product.costPrice_variant.required"));
        } else if (costPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new ValidateException(Translator.toMessage("error.product.costPrice_variant.positive"));
        }

        // Kiểm tra quantity
        if (quantity == null) {
            throw new ValidateException(Translator.toMessage("error.product.quantity_variant.required"));
        } else if (quantity < 0) {
            throw new ValidateException(Translator.toMessage("error.product.quantity_variant.positive"));
        }


        // Kiểm tra variantAttributeIds (nếu cần)
        if (variantAttributeIds == null || variantAttributeIds.isEmpty()) {
            throw new ValidateException(Translator.toMessage("error.product.variantAttributeIds.required"));
        }
    }

}
