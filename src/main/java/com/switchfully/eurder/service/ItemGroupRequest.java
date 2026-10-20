package com.switchfully.eurder.service;

import java.util.UUID;

public record ItemGroupRequest(UUID itemId, int quantity) {
}

/**
 * Service-level input for one line of an order: which item and how many.
 *
 * The controller cannot build an ItemGroup itself, because an ItemGroup
 * needs the full Item (price and stock), which only the service can fetch.
 * This record lets the controller pass the requested data to the service
 * without the service depending on API DTOs (mapping stays in the controller).
 */
