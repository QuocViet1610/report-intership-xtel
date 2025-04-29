package com.example.project1.model.enity.order;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "order_status")
@Data
@Getter
@Setter
public class OrderStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "status_id")
    private Long statusId;

    @Column(name = "status_name", nullable = false)
    private String statusName; // '1: Đang chờ, 2: Đang xử lý, 3: Đã chuyển, 4: Đã giao, 5: Đã hủy, 6: Đã hoàn tiền, 7: Không thành công'

}
