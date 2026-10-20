package com.switchfully.eurder.api.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.UUID;

public record CreateItemGroupDto(@NotNull UUID itemId, @Positive int quantity) {
}