package com.switchfully.eurder.service;

import com.switchfully.eurder.domain.Item;
import com.switchfully.eurder.exception.ItemNotFoundException;
import com.switchfully.eurder.repository.ItemRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
public class ItemServiceTest {

    @Autowired
    private ItemService itemService;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private EntityManager entityManager;
    private Item item;

    @BeforeEach
    void setUp() {
        item = new Item("VTT", "Sport Bike", new BigDecimal("350.00"), 10);
    }

    @Test
    void whenItemExists_thenReturnItem() {
        Item saved = itemRepository.save(item);
        Item result = itemService.getItemById(item.getId());
        assertThat(result).isEqualTo(saved);
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

    @Test
    void updateItem_isWrittenToTheDatabase() {
        itemRepository.save(item);
        itemService.updateItem(item.getId(), "Car", "Sport Car", new BigDecimal("25000.00"), 3);

        entityManager.flush();
        entityManager.clear();

        Item reloaded = itemRepository.findById(item.getId()).orElseThrow();
        assertThat(reloaded.getName()).isEqualTo("Car");
        assertThat(reloaded.getPrice()).isEqualByComparingTo(new BigDecimal("25000.00"));
        assertThat(reloaded.getStock()).isEqualTo(3);
    }
}
