package com.switchfully.eurder.exception;

import java.util.UUID;

public class EurderNotFoundException extends RuntimeException {
    public EurderNotFoundException(UUID id){
        super("Order with id " + id + " not found");
    }
}
