package com.example.project1.module.blog;
import com.example.project1.model.enity.blog.Blog;
import com.example.project1.model.enity.blog.BlogCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogCategoryRepository extends JpaRepository<BlogCategory, Long>{


}
