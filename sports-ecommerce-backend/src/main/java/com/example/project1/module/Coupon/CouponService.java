package com.example.project1.module.Coupon;

import com.example.project1.expection.ValidateException;
import com.example.project1.local.Translator;
import com.example.project1.mapper.CouponMapper;
import com.example.project1.model.dto.product.CouponDto;
import com.example.project1.model.enity.Coupon;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CouponService {

    CouponRepository couponRepository;
    CouponMapper couponMapper;

    public Coupon create(CouponDto couponDto){
        if (couponRepository.findByCouponCode(couponDto.getCouponCode()).isPresent()){
            throw new ValidateException(Translator.toMessage("Mã giảm giá đã tồn tại "));
        }
        Coupon coupon = couponMapper.toEntity(couponDto);
        return couponRepository.save(coupon);
    }

    public List<Coupon> getAll(){
        return couponRepository.findByIsActiveTrue();
    }


    public Coupon delete(Long id){
        Coupon coupon = couponRepository.findById(id).orElseThrow(() -> new ValidateException(Translator.toMessage("Mã giảm không tồn tại ")));
        coupon.setActive(false);
        return couponRepository.save(coupon);
    }

    public Coupon getDetailByCode(String code){
        Coupon coupon = couponRepository.findByCouponCode(code).orElseThrow(() -> new ValidateException(Translator.toMessage("Mã giảm giá không hợp lệ")));
        // Kiểm tra ngày hiện tại so với ngày bắt đầu và ngày kết thúc
        LocalDate startDate = coupon.getStartDate().toLocalDate();
        LocalDate endDate = coupon.getEndDate().toLocalDate();

        // Lấy ngày hiện tại
        LocalDate currentDate = LocalDate.now();

        // Kiểm tra xem ngày hiện tại có trước ngày bắt đầu hoặc sau ngày kết thúc không
        if (currentDate.isBefore(startDate)) {
            throw new ValidateException(Translator.toMessage("Mã giảm giá chưa bắt đầu sử dụng"));
        }

        if (currentDate.isAfter(endDate)) {
            throw new ValidateException(Translator.toMessage("Mã giảm giá đã hết hạn"));
        }

        if (!coupon.isActive()) {
            throw new ValidateException(Translator.toMessage("Mã giảm giá đã hết hạn"));
        }

        return coupon;
    }
}
