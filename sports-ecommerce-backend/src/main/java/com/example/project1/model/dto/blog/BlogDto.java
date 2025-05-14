package com.example.project1.model.dto.blog;

import com.example.project1.model.enity.User.User;
import com.example.project1.model.enity.blog.BlogCategory;
import jakarta.persistence.*;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class BlogDto {

    private Long id;

    private String title;

    private String content;

    private OffsetDateTime createdAt;

    private OffsetDateTime updatedAt ;

    private String image;

    private Long categoryId;

    private Long userId;

    private User user;
}
