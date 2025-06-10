package com.example.project1.model.enity.User;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Table(name = "invalidated_token")
@Entity
@Data
@Setter
@Getter
public class InvalidatedToken {
    @Id
    String id;

    Date expiryTime;


}
