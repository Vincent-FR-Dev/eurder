package com.switchfully.eurder.domain;

import java.math.BigDecimal;
import java.util.UUID;

public class Item {

    private final UUID id;
    private String name;
    private String description;
    private BigDecimal price;
    private int stock;

    public Item(String name, String description, BigDecimal price, int stock) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
    }

    public void update(String name, String description, BigDecimal price, int stock) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }
}