package com.example.project1.module.Order.repository;

import com.example.project1.model.enity.order.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long>, JpaSpecificationExecutor<Address> {
    List<Address> findAllByUserId(Long id);
    @Modifying
    @Query("UPDATE Address a SET a.isDefault = 0")
    void updateAllAddressesToDefault();

}
