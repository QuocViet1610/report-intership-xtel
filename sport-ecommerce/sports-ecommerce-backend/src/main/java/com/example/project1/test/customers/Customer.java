package com.example.project1.test.customers;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "customers")
@Data
@Getter
@Setter
public class Customer {
        @Id
        @Column(name = "`Index`")
        private Long index;

        @Column(name = "customer_id", length = 50)
        private String customerId;

        @Column(name = "first_name", length = 100)
        private String firstName;

        @Column(name = "last_name", length = 100)
        private String lastName;

        @Column(name = "company", length = 150)
        private String company;

        @Column(name = "city", length = 100)
        private String city;

        @Column(name = "country", length = 100)
        private String country;

        @Column(name = "phone1", length = 50)
        private String phone1;

        @Column(name = "phone2", length = 50)
        private String phone2;

        @Column(name = "email", length = 150)
        private String email;

        @Column(name = "subscription_date")
        private Date subscriptionDate;

        @Column(name = "website", length = 200)
        private String website;
}
