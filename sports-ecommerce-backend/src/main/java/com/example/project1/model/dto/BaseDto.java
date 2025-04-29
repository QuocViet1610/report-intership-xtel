package com.example.project1.model.dto;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import java.time.OffsetDateTime;

@Data
@MappedSuperclass
public class BaseDto {
    private Long id;

    private OffsetDateTime createdAt;

    private String createdBy;

    private OffsetDateTime updatedAt;

    private String updatedBy;
}
