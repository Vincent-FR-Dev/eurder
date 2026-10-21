package com.switchfully.eurder.api.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record CreateEurderDto(@NotEmpty @Valid List<CreateItemGroupDto> itemGroups) {
}