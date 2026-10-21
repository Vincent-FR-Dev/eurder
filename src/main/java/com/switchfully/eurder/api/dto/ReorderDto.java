package com.switchfully.eurder.api.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ReorderDto(@NotNull UUID customerId) {
}
