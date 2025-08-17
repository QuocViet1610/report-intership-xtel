package com.example.project1.cache;
import com.example.project1.mapper.CouponMapper;
import com.example.project1.mapper.product.CategoryMapper;
import com.example.project1.model.Enum.ActionType;
import com.example.project1.model.dto.product.CategoryDto;
import com.example.project1.model.dto.product.CouponDto;
import com.example.project1.model.enity.Coupon;
import com.example.project1.model.enity.product.Category;
import com.example.project1.module.Coupon.CouponRepository;
import com.example.project1.module.product.repository.CategoryRepository;
import com.example.project1.test.customers.Customer;
import com.example.project1.test.customers.CustomerDto;
import com.example.project1.test.customers.CustomerRepository;
import com.example.project1.test.customers.service.CustomerService;
import com.example.project1.utils.DataUtils;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.CRC32;

@Service
@RequiredArgsConstructor
public class CacheUtility {

    private final CategoryMapper categoryMapper;
    private final CategoryRepository categoryRepository;
    private final BaseCacheManager baseCacheManager;
    private final CouponRepository couponRepository;
    private final CouponMapper couponMapper;
    private final CustomerRepository customerRepository;
//    private final CustomerRepository customerRepository;
//    private final CustomerService customerService;
    private static final String CUSTOMER_CACHE_KEY = "Customer";
    private static final String USER_BITMAP_KEY = "user:bitmap";
    private static final int MAX_BITMAP_SIZE = 1_000_000;


    @PostConstruct
    public void initCache() {
        getCustomerCache();
    }

    public List<CategoryDto> getCategory() {
        List<CategoryDto> dto = baseCacheManager.getList("Category", CategoryDto.class);
        if (DataUtils.isNullOrEmpty(dto)) {
            dto = categoryMapper.toDto(categoryRepository.findAll());
            baseCacheManager.setCache("Category", dto);
        }
        return dto;
    }

    public void setCategory(Category zClass, int actionType) {
        List<CategoryDto> dto = getCategory();
        for (CategoryDto c : dto) {
            if (c.getId().equals(zClass.getId())) {
                dto.remove(c);
                break;
            }
        }
        if (ActionType.CREATE.getStatus().equals(actionType) || ActionType.UPDATE.getStatus().equals(actionType)) {
            dto.add(categoryMapper.toDto(zClass));
        }
        baseCacheManager.setCache("Category", dto);
    }


    public List<CouponDto> getCoupon() {
        List<CouponDto> dto = baseCacheManager.getList("Discount", CouponDto.class);
        if (DataUtils.isNullOrEmpty(dto)) {
            dto = couponMapper.toDto(couponRepository.findAll());
            baseCacheManager.setCache("Discount", dto);
        }
        return dto;
    }

    public void setCoupon(Coupon zClass, int actionType) {
        List<CouponDto> dto = getCoupon();
        for (CouponDto c : dto) {
            if (c.getCouponId().equals(zClass.getCouponId())) {
                dto.remove(c);
                break;
            }
        }
        if (ActionType.CREATE.getStatus().equals(actionType) || ActionType.UPDATE.getStatus().equals(actionType)) {
            dto.add(couponMapper.toDto(zClass));
        }
        baseCacheManager.setCache("Discount", dto);
    }


//    public List<CustomerDto> getCustomer() {
//        List<CustomerDto> dto = baseCacheManager.getList("Customer", CustomerDto.class);
//        if (DataUtils.isNullOrEmpty(dto)) {
//            dto =customerService.getNative();
//            baseCacheManager.setCache("Customer", dto);
//        }
//        return dto;
//    }

//    public void setCustomer(Customer zClass, int actionType) {
//        List<CustomerDto> dto = getCustomer();
//        for (CustomerDto c : dto) {
//            if (c.getCustomerIndex().equals(zClass.getIndex())) {
//                dto.remove(c);
//                break;
//            }
//        }
//        if (ActionType.CREATE.getStatus().equals(actionType) || ActionType.UPDATE.getStatus().equals(actionType)) {
//            dto.add(new Custome);
//        }
//        baseCacheManager.setCache("Discount", dto);
//    }

    private long hashUsername(String username) {
        CRC32 crc = new CRC32();
        crc.update(username.getBytes(StandardCharsets.UTF_8));
        return crc.getValue() % MAX_BITMAP_SIZE;
    }

    public boolean isUsernameExists(String username) {
        long offset = hashUsername(username);
        return baseCacheManager.getBit(USER_BITMAP_KEY, offset);
    }

    public void syncBitmapFromCustomerCache() {
        List<CustomerDto> customerList = baseCacheManager.getList(CUSTOMER_CACHE_KEY, CustomerDto.class);
        if (customerList != null) {
            for (CustomerDto dto : customerList) {
                if (dto.getEmail() != null) {
                    long offset = hashUsername(dto.getEmail());
                    baseCacheManager.setBit(USER_BITMAP_KEY, offset, true);
                }
            }
        }
    }

        public List<CustomerDto> getCustomerCache() {
            List<CustomerDto> customers = baseCacheManager.getList(CUSTOMER_CACHE_KEY, CustomerDto.class);
            if (customers == null || customers.isEmpty()) {
                customers = getNative(); // Hàm tự định nghĩa để lấy từ DB
                baseCacheManager.setCache(CUSTOMER_CACHE_KEY, customers);
                syncBitmapFromCustomerCache();
            }
            return customers;
        }

    public List<CustomerDto> getNative() {
        List<Object[]> rawList = customerRepository.getRawCustomerData();

        return rawList.stream()
                .map(obj -> new CustomerDto(
                        ((Number) obj[0]).longValue(),   // Index
                        (String) obj[1],                 // customerId
                        (String) obj[2],                 // firstName
                        (String) obj[3],                 // lastName
                        (String) obj[4],                 // company
                        (String) obj[5],                 // city
                        (String) obj[6],                 // country
                        (String) obj[7],                 // phone1
                        (String) obj[8],                 // phone2
                        (String) obj[9],                 // email
                        (Date)   obj[10],                // subscriptionDate
                        (String) obj[11]                 // website
                ))
                .collect(Collectors.toList());
    }

}
