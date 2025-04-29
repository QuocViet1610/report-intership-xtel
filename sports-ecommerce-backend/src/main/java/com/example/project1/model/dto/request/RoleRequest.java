package com.example.project1.model.dto.request;


import lombok.Data;

import java.util.Set;

@Data
public class RoleRequest {
    String name;
    String description;
    Set<String> permissions;
}
