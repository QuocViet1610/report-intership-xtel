package com.example.project1.model.enity.product;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Table(name = "attribute_value")
@Entity
@Setter
@Getter
public class AttributeValue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "attribute_id")
    private Long attributeId;

    @ManyToOne
    @JoinColumn(name = "attribute_id", insertable = false, updatable = false)
    @JsonIgnore
    private Attribute attribute;

    @Column(nullable = false, length = 255)
    private String name;

}
