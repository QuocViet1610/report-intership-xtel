package com.example.project1.module.blog;

import com.example.project1.model.enity.blog.Blog;
import com.example.project1.model.enity.blog.BlogView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BlogViewRepository extends JpaRepository<BlogView, Long>, JpaSpecificationExecutor<BlogView> {

}
