package com.example.project1.model.dto.request.Order;

import com.example.project1.expection.ValidateException;
import com.example.project1.local.Translator;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderDetailCreateRequest {

    private Long id;

    private Long orderId;

    @NotNull(message = "Product không được để trống")
    private Long productId;

    private Long productVariantId;

    @NotNull(message = "Số lượng không được để trống")
    @Positive(message = "Số lượng phải là một số dương")
    private Integer quantity;

    @NotNull(message = "Giá sản phẩm không được để trống")
    @Positive(message = "Giá sản phẩm phải lớn hơn số 0")
    private BigDecimal price;

    @NotNull(message = "Tổng giá không được để trống")
    @Positive(message = "Tổng giá phải lớn hơn số 0")
    private BigDecimal total;

    public static boolean validate(OrderDetailCreateRequest request) {

        if (request.getProductId() == null) {
            throw new ValidateException(Translator.toMessage("Sản phẩm không tồn tại"));
        }
        if (request.getQuantity() == null || request.getQuantity() <= 0) {
            throw new ValidateException(Translator.toMessage("Số lượng phải là một số dương"));
        }
        if (request.getPrice() == null || request.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidateException(Translator.toMessage("Giá sản phẩm phải lớn hơn số 0"));
        }
        if (request.getTotal() == null || request.getTotal().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidateException(Translator.toMessage("Tổng giá phải lớn hơn số 0"));
        }
        return true;
    }
}
