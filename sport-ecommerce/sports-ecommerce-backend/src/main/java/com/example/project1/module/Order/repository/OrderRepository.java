package com.example.project1.module.Order.repository;

import com.example.project1.model.enity.order.Order;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {

    Optional<Order> findByIdAndUserId(Long id, Long userId);

    List<Order> findByUserId(Long userId, Sort sort);



    List<Order> findAllByUserId(Long userId);

    @Query("SELECT SUM(o.finalPrice) FROM Order o WHERE o.statusId = 4")
    Double getTotalRevenue();

    @Query("SELECT COUNT(o) FROM Order o")
    Long getTotalOrders();

    @Query("SELECT MONTH(o.createdAt) as month, SUM(o.finalPrice) as total " +
            "FROM Order o " +
            "WHERE YEAR(o.createdAt) = :year AND o.statusId = 4 " +
            "GROUP BY MONTH(o.createdAt)")
    List<Object[]> getMonthlyRevenue(@Param("year") int year);

    @Query(value = """
    SELECT 
        MONTH(o.created_at) AS month,
        SUM(od.total) AS total_revenue,
        SUM(
            CASE
                WHEN od.product_variant_id IS NOT NULL THEN pv.cost_price * od.quantity
                ELSE p.cost_price * od.quantity
            END
        ) AS total_cost,
        SUM(od.total) - SUM(
            CASE
                WHEN od.product_variant_id IS NOT NULL THEN pv.cost_price * od.quantity
                ELSE p.cost_price * od.quantity
            END
        ) AS total_profit
    FROM Order_detail od
    JOIN Orders o ON o.order_id = od.order_id
    LEFT JOIN product p ON p.id = od.product_id
    LEFT JOIN product_variants pv ON pv.id = od.product_variant_id
    WHERE o.status_id = 4
    GROUP BY MONTH(o.created_at)
    ORDER BY MONTH(o.created_at)
    """, nativeQuery = true)
    List<Object[]> getMonthlyProfitData();

    @Query("SELECT o FROM Order o WHERE o.statusId = 4 ORDER BY o.finalPrice DESC")
    List<Order> findTop5ByOrderByTotalAmountDesc(Pageable pageable);

    @Query(value = "SELECT u.id AS userId, u.full_name AS fullName, u.avatar AS avatar, " +
            "u.email AS email, u.phone AS phone, " +
            "SUM(o.total_product) AS totalProductsBought, " +
            "SUM(o.final_price) AS totalAmountSpent " +
            "FROM Orders o JOIN users u ON o.user_id = u.id " +
            "WHERE o.status_id = 4 " +   // Thêm điều kiện chỉ lấy đơn hàng trạng thái Đã giao
            "GROUP BY u.id, u.full_name, u.avatar, u.email, u.phone " +
            "ORDER BY totalProductsBought DESC", nativeQuery = true)
    List<Object[]> findTopUsersByPurchaseCountNative(Pageable pageable);


}
