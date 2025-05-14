package com.example.project1.model.enity.blog;

import com.example.project1.model.enity.User.User;
import com.example.project1.utils.DataUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Entity
@Table(name = "Blogs")
@Data
@Setter
@Getter
public class Blog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt ;

    @Column(name = "image")
    private String image;

    @Column(name = "category_id")
    private Long categoryId;


    @Column(name = "user_id")
    private Long userId;

//    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", updatable = false, insertable = false)
    private User user;

    public String getImage() {
        return DataUtils.convertUrl(image);
    }
}
