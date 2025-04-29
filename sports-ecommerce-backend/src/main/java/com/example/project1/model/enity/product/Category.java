package com.example.project1.model.enity.product;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Table(name = "category")
@Entity
@Setter
@Getter
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "parent_id", insertable = false, updatable = false)
//    private Category parent;

    @Column(name = "parent_id")
    private Long parentId;

    @Column(name = "full_parent_id")
    private String fullParentId;

    @Column(length = 255)
    private String image;

    private Long level;
}
