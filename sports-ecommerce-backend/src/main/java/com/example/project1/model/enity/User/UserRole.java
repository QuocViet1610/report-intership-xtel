package com.example.project1.model.enity.User;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Table(name = "user_role")
@Entity
@Data
@Setter
@Getter
public class UserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "role_id")
    private Long roleId;

//    @ManyToOne
//    @JoinColumn(name = "user_id", nullable = false)
//    private User user;
//
    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false, insertable = false, updatable = false)
    private Role role;
}
