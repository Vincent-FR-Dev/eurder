package com.switchfully.eurder.repository;

import com.switchfully.eurder.domain.Eurder;
import com.switchfully.eurder.domain.Item;
import com.switchfully.eurder.domain.ItemGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class EurderRepositoryTest {

    private EurderRepository eurderRepository;
    private Eurder eurder;

    private static ItemGroup itemGroup(String price, int quantity) {
        Item item = new Item("TV", "TV full hd", new BigDecimal(price), 20);
        return new ItemGroup(item, quantity, LocalDate.now());
    }

    @BeforeEach
    void setUp() {
        eurderRepository = new EurderRepository();
        eurder = new Eurder(UUID.randomUUID(), List.of(itemGroup("100", 2), itemGroup("50", 10)));
    }

    @Test
    void whenEurderIsSaved_returnsEurder() {
        eurderRepository.save(eurder);
        Optional<Eurder> result = eurderRepository.getById(eurder.getId());
        assertThat(result).contains(eurder);
    }

    @Test
    void whenEurderIsNotSaved_returnsEmpty() {
        Optional<Eurder> result = eurderRepository.getById(eurder.getId());
        assertThat(result).isEmpty();
    }
}
