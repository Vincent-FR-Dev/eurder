package com.switchfully.eurder.api.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record CustomerDto(UUID id, String firstname, String lastname, String email, AddressDto address, String phoneNumber) {

}
