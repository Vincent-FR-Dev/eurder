package com.switchfully.eurder.repository;

import com.switchfully.eurder.domain.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;
    private Item item;

    @BeforeEach
    void setUp() {
        item = new Item("VTT", "Sport Bike", new BigDecimal("350.00") , 10);
    }

    @Test
    void whenItemIsSaved_returnsItem() {
        Item saved = itemRepository.save(item);
        Optional<Item> result = itemRepository.findById(item.getId());
        assertThat(result).contains(saved);
    }

    @Test
    void whenItemIsNotSaved_returnsEmpty() {
        Optional<Item> result = itemRepository.findById(item.getId());
        assertThat(result).isEmpty();
    }
}
