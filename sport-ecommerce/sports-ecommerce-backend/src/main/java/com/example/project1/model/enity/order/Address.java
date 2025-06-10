package com.example.project1.model.enity.order;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Entity
@Table(name = "addresses_ship")
@Data
@Getter
@Setter
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "province_name", nullable = false)
    private String provinceName;

    @Column(name = "province_id", nullable = false)
    private Long provinceId;

    @Column(name = "district_name", nullable = false)
    private String districtName;

    @Column(name = "district_id", nullable = false)
    private Long districtId;

    @Column(name = "ward_name", nullable = false)
    private String wardName;

    @Column(name = "ward_id", nullable = false)
    private Long wardId;

    @Column(name = "country", nullable = false)
    private String country;

    @Column(name = "is_default", nullable = false)
    private Integer isDefault;

    @Column(name = "note")
    private String note;

    @Column(name = "address_text", nullable = false)
    private String addressText; // địa chỉ nhận hàng (số nhà, đường)

    @Column(name = "address_type", nullable = false)
    private String addressType;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

}

