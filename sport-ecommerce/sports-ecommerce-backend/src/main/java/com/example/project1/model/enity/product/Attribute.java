package com.example.project1.model.enity.product;

import com.example.project1.model.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.HashSet;
import java.util.Set;

@Table(name = "attribute")
@Entity
@Setter
@Getter
public class Attribute extends BaseEntity {
    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "display_order")
    private Integer displayOrder;

    @JsonIgnore
    @OneToMany(mappedBy = "attribute", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private Set<AttributeValue> values = new HashSet<>();
}
