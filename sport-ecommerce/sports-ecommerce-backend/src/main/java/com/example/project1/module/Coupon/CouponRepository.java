package com.example.project1.module.Coupon;
import com.example.project1.model.enity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CouponRepository extends JpaRepository<Coupon, Long>{

    Optional<Coupon> findByCouponCode(String code);

    List<Coupon> findByIsActiveTrue();
}
