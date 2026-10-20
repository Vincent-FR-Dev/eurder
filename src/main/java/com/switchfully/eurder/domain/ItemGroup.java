package com.switchfully.eurder.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class ItemGroup {

    private final UUID itemId;
    private final int quantity;
    private final BigDecimal price;
    private final LocalDate shippingDate;

    public ItemGroup(Item item, int quantity, LocalDate orderDate) {
        this.itemId = item.getId();
        this.quantity = quantity;
        this.price = item.getPrice();

        if (quantity <= item.getStock()){
            this.shippingDate = orderDate.plusDays(1);
        }
        else{
            this.shippingDate = orderDate.plusDays(7);
        }

    }

    public UUID getItemId() {
        return itemId;
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public LocalDate getShippingDate() {
        return shippingDate;
    }

    public BigDecimal getTotalPrice() {
        return price.multiply(BigDecimal.valueOf(quantity));
    }
}
