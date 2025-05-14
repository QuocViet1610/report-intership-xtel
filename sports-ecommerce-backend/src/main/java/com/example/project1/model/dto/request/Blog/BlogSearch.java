package com.example.project1.model.dto.request.Blog;

import lombok.Data;

@Data
public class BlogSearch {
    private String searchText;
    private Long statusId;

}
