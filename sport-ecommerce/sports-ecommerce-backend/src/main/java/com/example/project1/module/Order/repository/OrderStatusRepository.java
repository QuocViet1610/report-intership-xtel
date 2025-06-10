package com.example.project1.module.Order.repository;

import com.example.project1.model.enity.order.Order;
import com.example.project1.model.enity.order.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface OrderStatusRepository extends JpaRepository<OrderStatus, Long>, JpaSpecificationExecutor<OrderStatus> {

}
