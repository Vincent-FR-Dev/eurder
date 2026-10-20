package com.switchfully.eurder.api.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateCustomerDto(@NotBlank String firstname, @NotBlank String lastname, @NotBlank @Email String email, @NotNull @Valid AddressDto address, @NotBlank String phoneNumber) {

}
