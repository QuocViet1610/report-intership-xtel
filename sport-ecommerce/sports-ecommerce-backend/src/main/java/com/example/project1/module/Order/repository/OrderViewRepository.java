package com.example.project1.module.Order.repository;

import com.example.project1.model.enity.order.Order;
import com.example.project1.model.enity.order.OrderView;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface OrderViewRepository extends JpaRepository<OrderView, Long>, JpaSpecificationExecutor<OrderView> {
//
//    Optional<Order> findByIdAndUserId(Long id, Long userId);
//
//    List<Order> findByUserId(Long userId, Sort sort);
}
