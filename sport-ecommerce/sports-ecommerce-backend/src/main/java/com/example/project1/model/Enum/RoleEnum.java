package com.example.project1.model.Enum;

public enum RoleEnum {

    ADMIN(1L), USER(2L);

    private final Long id;

    RoleEnum(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;

    }

}
