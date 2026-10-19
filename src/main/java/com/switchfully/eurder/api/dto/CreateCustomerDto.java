package com.switchfully.eurder.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreateCustomerDto(@NotBlank String firstname, @NotBlank String lastname, @NotBlank @Email String email, AddressDto address, @NotBlank String phoneNumber) {

}
