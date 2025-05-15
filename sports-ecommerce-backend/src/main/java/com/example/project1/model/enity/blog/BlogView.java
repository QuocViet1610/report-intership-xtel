package com.example.project1.model.enity.blog;

import com.example.project1.utils.DataUtils;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Entity
@Table(name = "user_blog_view")
@Data
@Setter
@Getter
public class BlogView {

    @Id
    @Column(name = "id") // This maps to the `id` column in the view (could be `post_id` if it's the post ID)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "image")
    private String image;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "email")
    private String email;

    @Column(name = "avatar")
    private String avatar;

    @Column(name = "phone")
    private String phone;

    @Column(name = "is_active")
    private Integer isActive;

    @Column(name = "category_id")
    private Long categoryId;

    public String getImage() {
        return DataUtils.convertUrl(image);
    }

    public String getAvatar() {
        return DataUtils.convertUrl(avatar == null ? "product/avatar-vo-danh-9.png" : avatar);
    }
}
