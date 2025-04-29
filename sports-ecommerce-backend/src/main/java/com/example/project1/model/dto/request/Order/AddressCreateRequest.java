package com.example.project1.model.dto.request.Order;


import com.example.project1.utils.Constants;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.time.OffsetDateTime;

@Data
public class AddressCreateRequest {

    private Long id;

    private Long userId;

    @NotNull(message = "Họ và tên không được để trống")
    @Size(min = 3, max = 255, message = "Họ và tên phải có độ dài từ 3 đến 255 ký tự")
    private String fullName;

    @NotNull(message = "Số điện thoại không được để trống")
    @Size(min = 10, max = 15, message = "Số điện thoại phải có độ dài từ 10 đến 15 ký tự")
    @Pattern(regexp = Constants.REGEX.PHONE_PATTERN, message = "error.user.phone.invalid")
    private String phoneNumber;
    @Size(max = 255, message = "Tên tỉnh không được quá 255 ký tự")
    private String provinceName;

    private Long provinceId;

    @Size(max = 255, message = "Tên quận không được quá 255 ký tự")
    private String districtName;

    private Long districtId;

    @Size(max = 255, message = "Tên phường không được quá 255 ký tự")
    private String wardName;

    private Long wardId;

    private String country;

    private Integer isDefault;

    @Size(max = 500, message = "Ghi chú không được quá 500 ký tự")
    private String note;

    @NotNull(message = "Địa chỉ nhận hàng không được để trống")
    @Size(min = 5, max = 500, message = "Địa chỉ nhận hàng phải có độ dài từ 5 đến 500 ký tự")
    private String addressText;

    @Size(min = 3, max = 100, message = "Địa chỉ nhận hàng phải có độ dài từ 3 đến 100 ký tự")
    private String addressType;
}
