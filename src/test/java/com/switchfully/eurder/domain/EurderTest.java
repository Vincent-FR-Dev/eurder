package com.switchfully.eurder.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class EurderTest {

    private static ItemGroup itemGroup(String price, int quantity) {
        Item item = new Item("TV", "TV full hd", new BigDecimal(price), 20);
        return new ItemGroup(item, quantity, LocalDate.now());
    }

    @Test
    void getTotalPrice_returnsSumOfItemGroups() {
        Eurder eurder = new Eurder(UUID.randomUUID(), List.of(itemGroup("275.00", 4), itemGroup("25.00", 4)));
        assertThat(eurder.getTotalPrice()).isEqualByComparingTo(new BigDecimal("1200"));
    }






}

