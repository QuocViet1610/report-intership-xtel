package com.example.project1.test.order;
import com.example.project1.cache.CacheUtility;
import com.example.project1.cache.RedisUtil;
import com.example.project1.expection.ValidateException;
import com.example.project1.local.Translator;
import com.example.project1.mapper.CouponMapper;
import com.example.project1.model.Enum.ActionType;
import com.example.project1.model.dto.product.CouponDto;
import com.example.project1.model.enity.Coupon;
import com.example.project1.module.Coupon.CouponRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CouponCacheService {
    private final RedisUtil<String> redisUtil;
    private final CacheUtility cacheUtility;
    private final CouponRepository couponRepository;
    private final CouponMapper couponMapper;

    @Autowired
    public CouponCacheService(RedisUtil<String> redisUtil, CacheUtility cacheUtility, CouponRepository couponRepository, CouponMapper couponMapper) {
        this.redisUtil = redisUtil;
        this.cacheUtility = cacheUtility;
        this.couponRepository = couponRepository;
        this.couponMapper = couponMapper;
    }

    public Boolean applyDiscountLock(String discountCodeKey, String lockValue, long timeout, TimeUnit unit) {
        return redisUtil.tryLock(discountCodeKey, lockValue, timeout, unit);
    }

    public Boolean releaseDiscountLock(String discountCodeKey, String lockValue) {
        return redisUtil.releaseLock(discountCodeKey, lockValue);
    }


    public List<CouponDto> getAll(){
        return cacheUtility.getCoupon();
    }

    public CouponDto create(CouponDto couponDto){
        if (couponRepository.findByCouponCode(couponDto.getCouponCode()).isPresent()){
            throw new ValidateException(Translator.toMessage("Mã giảm giá đã tồn tại "));
        }
        Coupon coupon = couponMapper.toEntity(couponDto);
        couponRepository.save(coupon);
        cacheUtility.setCoupon(coupon, ActionType.CREATE.getStatus());
        return couponMapper.toDto(coupon);
    }

    public CouponDto delete(Long id){
        Coupon coupon = couponRepository.findById(id).orElseThrow(() -> new ValidateException(Translator.toMessage("Mã giảm không tồn tại ")));
        coupon.setActive(false);
        cacheUtility.setCoupon(coupon, ActionType.UPDATE.getStatus());
        return couponMapper.toDto(couponRepository.save(coupon));
    }

    public boolean applyDiscountCode(String couponCode, String userLockValue) {
        String lockKey = "discount_lock:" + couponCode; // Khóa Redis cho mã giảm giá

        int randomNumber = ThreadLocalRandom.current().nextInt(1, 100);

        System.out.println(randomNumber);

        // Cố gắng lấy khóa (lock) cho mã giảm giá
        boolean lockAcquired = redisUtil.tryLock(lockKey, userLockValue + randomNumber, 10, TimeUnit.SECONDS);

        if (!lockAcquired) {
            throw new ValidateException(Translator.toMessage("Đang có người sử dụng mã giảm giá này, vui lòng thử lại sau"));
        }

        try {
            // Kiểm tra xem mã giảm giá có hợp lệ không
            CouponDto couponDto = cacheUtility.getCoupon().stream()
                    .filter(c -> c.getCouponCode().equals(couponCode))
                    .findFirst()
                    .orElse(null);
            if (couponDto == null) {
                throw new ValidateException(Translator.toMessage("Mã giảm giá không tồn tại"));
            }
            if (couponDto.getTimesUsed() >= couponDto.getUsageLimit()) {
                throw new ValidateException(Translator.toMessage("Mã giảm giá đã hết hạn sử dụng hoặc đã đạt giới hạn."));
            }
            couponDto.setTimesUsed(couponDto.getTimesUsed() + 1);
            Coupon coupon = couponMapper.toEntity(couponDto);
            cacheUtility.setCoupon(coupon, ActionType.UPDATE.getStatus());
            couponRepository.save(coupon);
            System.out.println("Mã giảm giá đã được áp dụng thành công.");
            return true;

        } catch (Exception e) {
            System.out.println("Lỗi khi áp dụng mã giảm giá: " + e.getMessage());
            return false;
        } finally {
            // Giải phóng khóa sau khi xử lý xong
            redisUtil.releaseLock(lockKey, userLockValue);
        }
    }

}
