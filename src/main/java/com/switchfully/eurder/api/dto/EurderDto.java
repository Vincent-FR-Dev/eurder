package com.switchfully.eurder.api.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record EurderDto(UUID id, UUID customerId, List<ItemGroupDto> itemGroups, BigDecimal totalPrice) {
}