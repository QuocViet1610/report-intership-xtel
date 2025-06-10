package com.example.project1.module.blog;

import com.example.project1.model.dto.blog.BlogDto;
import com.example.project1.model.dto.request.Blog.BlogBaseRequest;
import com.example.project1.model.dto.request.Blog.BlogSearch;
import com.example.project1.module.PageableCustom;

public interface BlogService {

    Object createBlog(BlogDto blogDto);
    void updateBlog(BlogDto blogDto, Long id);

    public Object search(BlogSearch searchRequest, PageableCustom pageable);

    public Object create(BlogBaseRequest request);

    Object getDetail(Long id);

    void deleteBlog(Long id);
}
