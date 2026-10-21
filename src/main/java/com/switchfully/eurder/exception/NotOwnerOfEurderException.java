package com.switchfully.eurder.exception;

import java.util.UUID;

public class NotOwnerOfEurderException extends RuntimeException {
    public NotOwnerOfEurderException(UUID eurderId, UUID customerId){
        super("Customer " + customerId + " is not the owner of order " + eurderId);
    }
}
