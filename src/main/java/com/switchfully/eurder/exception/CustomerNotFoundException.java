package com.switchfully.eurder.exception;


import java.util.UUID;

public class CustomerNotFoundException extends RuntimeException {

    public CustomerNotFoundException(UUID id) {
        super("Customer with id " + id + " not found");
    }

    public CustomerNotFoundException(String email) {
        super("Customer with email " + email + " not found");
    }


}
