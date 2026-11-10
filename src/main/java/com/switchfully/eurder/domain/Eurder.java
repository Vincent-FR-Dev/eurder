package com.switchfully.eurder.domain;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OrderColumn;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "eurder")
public class Eurder {

    @Id
    private UUID id;
    private UUID customerId;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "item_group", joinColumns = @JoinColumn(name = "eurder_id"))
    @OrderColumn(name = "line_number")
    private List<ItemGroup> itemGroups = new ArrayList<>();

    protected Eurder() {
    }

    public Eurder(UUID customerId, List<ItemGroup> itemGroups){
        this.id = UUID.randomUUID();
        this.customerId = customerId;
        this.itemGroups = new ArrayList<>(itemGroups);
    }

    public UUID getId() {
        return id;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public List<ItemGroup> getItemGroups() {
        return Collections.unmodifiableList(itemGroups);
    }

    public BigDecimal getTotalPrice() {
        return itemGroups.stream().map(itemGroup -> itemGroup.getTotalPrice()).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
