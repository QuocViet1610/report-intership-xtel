package com.example.project1.model.dto.request.Order;

import lombok.Data;

@Data
public class OrderSearchRequest {

    private String searchText;
    private Long statusId;

}
