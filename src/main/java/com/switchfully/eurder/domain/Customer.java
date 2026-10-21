package com.switchfully.eurder.domain;

import java.util.UUID;

public class Customer {

    private UUID id;
    private String firstname;
    private String lastname;
    private String email;
    private Address address;
    private String phoneNumber;
    private String password;

    public Customer(String firstname, String lastname, String email, Address address, String phoneNumber, String password) {
        this.id = UUID.randomUUID();
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.password = password;
    }

    public UUID getId() {
        return id;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {return password; }

    public Address getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
