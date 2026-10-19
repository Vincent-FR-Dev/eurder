package com.switchfully.eurder.api.dto;

import jakarta.validation.constraints.NotBlank;

public record AddressDto( @NotBlank String streetName, @NotBlank String streetNumber, @NotBlank String postalCode, @NotBlank String city) {

}
