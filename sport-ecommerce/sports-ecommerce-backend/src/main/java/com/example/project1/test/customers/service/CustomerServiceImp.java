package com.example.project1.test.customers.service;
import com.example.project1.cache.BaseCacheManager;
import com.example.project1.cache.CacheUtility;
import com.example.project1.test.customers.Customer;
import com.example.project1.test.customers.CustomerDto;
import com.example.project1.test.customers.CustomerRepository;
import com.example.project1.utils.DataUtils;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class CustomerServiceImp implements CustomerService{

    CustomerRepository customerRepository;
    CacheUtility cacheUtility;
    BaseCacheManager baseCacheManager;

    @Override
    public List<Customer> getAll() {
        return customerRepository.findAll();
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

    public List<CustomerDto> getNativeCache() {
        List<CustomerDto> dto = baseCacheManager.getList("Customer", CustomerDto.class);
        if (DataUtils.isNullOrEmpty(dto)) {
            dto = getNative();
            baseCacheManager.setCache("Customer", dto);
        }
        return dto;
    }

    public Page<Customer> getAllCustomers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return customerRepository.findAll(pageable);
    }

    public Page<Customer> getAllCustomersNative(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return customerRepository.findAllNative(pageable);
    }

    public boolean checkUserCache(String username) {
//        cacheUtility.getCustomerCache();
        if (cacheUtility.isUsernameExists(username)) {
           return true;
        } else {
           return false;
        }
    }

    public boolean checkUser(String username) {
        if (customerRepository.findByEmail(username).isPresent()) {
            return true;
        } else {
            return false;
        }
    }

}
