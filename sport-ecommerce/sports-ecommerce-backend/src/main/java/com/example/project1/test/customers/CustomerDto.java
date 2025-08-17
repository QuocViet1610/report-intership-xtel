package com.example.project1.test.customers;

import lombok.Getter;

import java.util.Date;

@Getter
public class    CustomerDto {
    private Long customerIndex;
    private String customerId;
    private String firstName;
    private String lastName;
    private String company;
    private String city;
    private String country;
    private String phone1;
    private String phone2;
    private String email;
    private Date subscriptionDate;
    private String website;

    public CustomerDto(Long customerIndex, String customerId, String firstName, String lastName,
                       String company, String city, String country,
                       String phone1, String phone2, String email,
                       Date subscriptionDate, String website) {
        this.customerIndex = customerIndex;
        this.customerId = customerId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.company = company;
        this.city = city;
        this.country = country;
        this.phone1 = phone1;
        this.phone2 = phone2;
        this.email = email;
        this.subscriptionDate = subscriptionDate;
        this.website = website;
    }

    public CustomerDto() {
    }
}
