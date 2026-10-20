package com.switchfully.eurder.api.dto;

import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public record CreateItemDto(@NotBlank String name, @NotBlank String description, @NotBlank BigDecimal price, @NotBlank int stock) {
}
