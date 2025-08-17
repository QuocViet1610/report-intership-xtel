package com.example.project1.model.Enum;

public enum ActionType {
    CREATE(0),
    UPDATE(1),
    DELETE(2);

    private final Integer status;

    ActionType(Integer status){
        this.status = status;
    }

    public Integer getStatus() {
        return status;
    }
}
