package com.switchfully.eurder.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class ItemTest {

    @Test
    void twoItemsDontHaveSameId() {
        Item bike = new Item("VTT", "Sport Bike", new BigDecimal("350.00"), 10);
        Item ball = new Item("World Cup Ball", "For playing football", new BigDecimal("40"), 50);

        assertThat(bike.getId()).isNotNull();
        assertThat(bike.getId()).isNotEqualTo(ball.getId());
    }

    @Test
    void update_changesAllFieldsButKeepsSameId() {
        Item bike = new Item("VTT", "Sport Bike", new BigDecimal("350.00"), 10);
        UUID originalId = bike.getId();

        bike.update("VTT Pro", "Carbon bike", new BigDecimal("500.00"), 3);

        assertThat(bike.getId()).isEqualTo(originalId);
        assertThat(bike.getName()).isEqualTo("VTT Pro");
        assertThat(bike.getDescription()).isEqualTo("Carbon bike");
        assertThat(bike.getPrice()).isEqualByComparingTo(new BigDecimal("500.00"));
        assertThat(bike.getStock()).isEqualTo(3);
    }
}