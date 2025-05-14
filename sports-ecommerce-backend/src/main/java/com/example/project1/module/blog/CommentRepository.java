package com.example.project1.module.blog;
import com.example.project1.model.enity.blog.BlogCategory;
import com.example.project1.model.enity.blog.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long>{


}
