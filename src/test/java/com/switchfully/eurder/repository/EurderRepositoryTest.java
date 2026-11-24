package com.switchfully.eurder.repository;

import com.switchfully.eurder.domain.Address;
import com.switchfully.eurder.domain.Customer;
import com.switchfully.eurder.domain.Eurder;
import com.switchfully.eurder.domain.Item;
import com.switchfully.eurder.domain.ItemGroup;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
public class EurderRepositoryTest {

    @Autowired
    private EurderRepository eurderRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private EntityManager entityManager;

    private Customer rudy;
    private Item tv;
    private Item radio;
    private Eurder eurder;

    @BeforeEach
    void setUp() {
        rudy = customerRepository.save(new Customer("Rudy", "Switchfully", "rudy@switchfully.com",
                new Address("Rue des loups", "2", "1000", "Bruxelles"), "0472548963", "password123"));
        tv = itemRepository.save(new Item("TV", "TV full hd", new BigDecimal("100.00"), 20));
        radio = itemRepository.save(new Item("Radio", "FM radio", new BigDecimal("50.00"), 20));
        eurder = new Eurder(rudy.getId(), List.of(
                new ItemGroup(tv, 2, LocalDate.now()),
                new ItemGroup(radio, 10, LocalDate.now())));
    }

    @Test
    void whenEurderIsSaved_returnsEurder() {
        Eurder saved = eurderRepository.save(eurder);
        Optional<Eurder> result = eurderRepository.findById(eurder.getId());
        assertThat(result).contains(saved);
    }

    @Test
    void whenEurderIsNotSaved_returnsEmpty() {
        Optional<Eurder> result = eurderRepository.findById(eurder.getId());
        assertThat(result).isEmpty();
    }

    @Test
    void savedEurder_isReloadedFromDatabaseWithItemGroupsInOrder() {
        eurderRepository.save(eurder);
        entityManager.flush();
        entityManager.clear();

        Eurder reloaded = eurderRepository.findById(eurder.getId()).orElseThrow();

        assertThat(reloaded.getCustomerId()).isEqualTo(rudy.getId());
        assertThat(reloaded.getItemGroups()).extracting(ItemGroup::getItemId)
                .containsExactly(tv.getId(), radio.getId());
        assertThat(reloaded.getTotalPrice()).isEqualByComparingTo(new BigDecimal("700.00"));
    }

    @Test
    void eurderWithUnknownItem_isRefusedByTheDatabase() {
        Item notSaved = new Item("Ghost", "Not in database", new BigDecimal("10.00"), 1);
        Eurder withUnknownItem = new Eurder(rudy.getId(), List.of(new ItemGroup(notSaved, 1, LocalDate.now())));

        assertThatThrownBy(() -> eurderRepository.saveAndFlush(withUnknownItem))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void eurderWithUnknownCustomer_isRefusedByTheDatabase() {
        Eurder withUnknownCustomer = new Eurder(UUID.randomUUID(), List.of(new ItemGroup(tv, 1, LocalDate.now())));

        assertThatThrownBy(() -> eurderRepository.saveAndFlush(withUnknownCustomer))
                .isInstanceOf(DataIntegrityViolationException.class);
    }
}
