package com.example.project1.test.customers.service;

import com.example.project1.test.customers.Customer;
import com.example.project1.test.customers.CustomerDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CustomerService {
    List<CustomerDto> getNative();
    List<Customer> getAll();
    Page<Customer> getAllCustomers(int page, int size);
    Page<Customer> getAllCustomersNative(int page, int size);

    List<CustomerDto> getNativeCache();

    boolean checkUser(String username);

    boolean checkUserCache(String username);
}
