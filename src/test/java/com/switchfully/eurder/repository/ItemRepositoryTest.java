package com.switchfully.eurder.repository;

import com.switchfully.eurder.domain.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class ItemRepositoryTest {

    private ItemRepository itemRepository;
    private Item item;

    @BeforeEach
    void setUp() {
        itemRepository = new ItemRepository();
        item = new Item("VTT", "Sport Bike", new BigDecimal("350.00") , 10);
    }

    @Test
    void whenItemIsSaved_returnsItem() {
        itemRepository.save(item);
        Optional<Item> result = itemRepository.getById(item.getId());
        assertThat(result).contains(item);
    }

    @Test
    void whenItemIsNotSaved_returnsEmpty() {
        Optional<Item> result = itemRepository.getById(item.getId());
        assertThat(result).isEmpty();
    }
}
