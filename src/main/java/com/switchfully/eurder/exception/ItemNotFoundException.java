package com.switchfully.eurder.exception;


import java.util.UUID;

public class ItemNotFoundException extends RuntimeException {

    public ItemNotFoundException(UUID id) {
        super("Item with id " + id + " not found");
    }
}
