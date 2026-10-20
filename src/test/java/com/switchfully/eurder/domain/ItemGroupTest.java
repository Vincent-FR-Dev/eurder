package com.switchfully.eurder.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

public class ItemGroupTest {

    private static final LocalDate ORDER_DATE = LocalDate.of(2026, 10, 20);

    private static Item bikeWithStock(int stock) {
        return new Item("VTT", "Sport Bike", new BigDecimal("350.00"), stock);
    }

    @Test
    void whenStockIsOk_ShippingNextDay(){
        ItemGroup itemGroup = new ItemGroup(bikeWithStock(10), 2, ORDER_DATE);
        assertThat(itemGroup.getShippingDate()).isEqualTo(LocalDate.of(2026, 10, 21));
    }

    @Test
    void whenStockIsNotOk_ShippingNextWeek(){
        ItemGroup itemGroup = new ItemGroup(bikeWithStock(0), 2, ORDER_DATE);
        assertThat(itemGroup.getShippingDate()).isEqualTo(LocalDate.of(2026, 10, 27));

    }

    @Test
    void whenQuantityEqualsStock_ShippingNextDay() {
        ItemGroup itemGroup = new ItemGroup(bikeWithStock(2), 2, ORDER_DATE);
        assertThat(itemGroup.getShippingDate()).isEqualTo(LocalDate.of(2026, 10, 21));
    }

    @Test
    void getTotalPrice_returnsPriceTimesQuantity() {
        ItemGroup itemGroup = new ItemGroup(bikeWithStock(10), 2, ORDER_DATE);

        assertThat(itemGroup.getTotalPrice()).isEqualByComparingTo(new BigDecimal("700.00"));
    }


}
