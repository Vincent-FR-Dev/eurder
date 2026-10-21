package com.switchfully.eurder.service;

import com.switchfully.eurder.domain.Item;
import com.switchfully.eurder.exception.ItemNotFoundException;
import com.switchfully.eurder.repository.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ItemServiceTest {

    private ItemService itemService;
    private ItemRepository itemRepository;
    private Item item;

    @BeforeEach
    void setUp() {
        itemRepository = new ItemRepository();
        itemService = new ItemService(itemRepository);
        item = new Item("VTT", "Sport Bike", new BigDecimal("350.00"), 10);
    }

    @Test
    void whenItemExists_thenReturnItem() {
        itemRepository.save(item);
        Item result = itemService.getItemById(item.getId());
        assertThat(result).isEqualTo(item);
    }

    @Test
    void whenItemDoesntExist_thenThrowException() {
        assertThatThrownBy(() -> itemService.getItemById(item.getId()))
                .isInstanceOf(ItemNotFoundException.class);
    }

    @Test
    void whenItemExists_updatesItem() {
        itemRepository.save(item);
        Item result = itemService.updateItem(item.getId(), "Car", "Sport Car", new BigDecimal("25000.00"), 3);
        assertThat(result.getPrice()).isEqualByComparingTo(new BigDecimal("25000.00"));
    }
}