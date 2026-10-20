package com.switchfully.eurder.domain;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class Eurder {

    private final UUID id;
    private final UUID customerId;
    private final List<ItemGroup> itemGroups;

    public Eurder(UUID customerId, List<ItemGroup> itemGroups){
        this.id = UUID.randomUUID();
        this.customerId = customerId;
        this.itemGroups = List.copyOf(itemGroups);
    }

    public UUID getId() {
        return id;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public List<ItemGroup> getItemGroups() {
        return itemGroups;
    }

    public BigDecimal getTotalPrice() {
        return itemGroups.stream().map(itemGroup -> itemGroup.getTotalPrice()).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
