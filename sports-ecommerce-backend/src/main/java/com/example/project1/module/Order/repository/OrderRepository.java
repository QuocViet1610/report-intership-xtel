package com.example.project1.module.Order.repository;

import com.example.project1.model.dto.respone.CartResponse;
import com.example.project1.model.enity.order.Address;
import com.example.project1.model.enity.order.Order;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {

    Optional<Order> findByIdAndUserId(Long id, Long userId);

    List<Order> findByUserId(Long userId, Sort sort);

    @Query("SELECT SUM(o.finalPrice) FROM Order o")
    Double getTotalRevenue();

    @Query("SELECT COUNT(o) FROM Order o")
    Long getTotalOrders();
}
