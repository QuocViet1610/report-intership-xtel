package com.example.project1.module.Order.repository;

import com.example.project1.model.enity.order.Order;
import com.example.project1.model.enity.order.OrderDetail;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long>, JpaSpecificationExecutor<Order> {

//    Optional<OrderDetail> findByOrderIdAndAndOrderItemId(Long orderId, )
List<OrderDetail> findAllByOrderId( Long userId);

    @Query("SELECT od.productId, COUNT(od) AS totalOrders FROM OrderDetail od GROUP BY od.productId ORDER BY totalOrders DESC")
    List<Object[]> findTop5ProductsByTotalOrders(Pageable pageable);

    List<OrderDetail> findAllByProductId( Long ID);
}
