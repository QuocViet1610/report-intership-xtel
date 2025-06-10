package com.example.project1.mapper;

import com.example.project1.model.dto.product.CouponDto;
import com.example.project1.model.enity.Coupon;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class CouponMapper implements EntityMapper<CouponDto, Coupon>{

}
