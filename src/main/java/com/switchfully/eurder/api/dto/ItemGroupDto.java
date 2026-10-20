package com.switchfully.eurder.api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record ItemGroupDto(UUID itemId, int quantity, BigDecimal price, LocalDate shippingDate) {
}