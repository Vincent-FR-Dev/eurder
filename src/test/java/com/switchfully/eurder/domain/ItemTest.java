package com.switchfully.eurder.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class ItemTest {

    @Test
    void twoItemsDontHaveSameId(){

        Item bike = new Item("VTT", "Sport Bike", new BigDecimal("350.00") , 10);
        Item ball = new Item("World Cup Ball", "For playing football", new BigDecimal("40"), 50);

        assertThat(bike.getId()).isNotNull();
        assertThat(bike.getId()).isNotEqualTo(ball.getId());
    }
}
