package com.switchfully.eurder.domain;

import jakarta.persistence.Embeddable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Embeddable
public class ItemGroup {

    private UUID itemId;
    private int quantity;
    private BigDecimal price;
    private LocalDate shippingDate;

    protected ItemGroup() {
    }

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
