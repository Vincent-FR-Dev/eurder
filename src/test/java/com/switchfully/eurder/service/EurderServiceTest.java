package com.switchfully.eurder.service;

import com.switchfully.eurder.domain.Address;
import com.switchfully.eurder.domain.Customer;
import com.switchfully.eurder.domain.Eurder;
import com.switchfully.eurder.domain.Item;
import com.switchfully.eurder.exception.CustomerNotFoundException;
import com.switchfully.eurder.exception.ItemNotFoundException;
import com.switchfully.eurder.repository.CustomerRepository;
import com.switchfully.eurder.repository.EurderRepository;
import com.switchfully.eurder.repository.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class EurderServiceTest {

    private CustomerRepository customerRepository;
    private ItemRepository itemRepository;
    private EurderRepository eurderRepository;

    private ItemService itemService;
    private CustomerService customerService;
    private EurderService eurderService;

    private Customer rudy;
    private Item bike;

    @BeforeEach
    public void setUp() {
        customerRepository = new CustomerRepository();
        itemRepository = new ItemRepository();
        eurderRepository = new EurderRepository();
        customerService = new CustomerService(customerRepository);
        itemService = new ItemService(itemRepository);
        eurderService = new EurderService(eurderRepository, customerService, itemService);

        rudy = new Customer("Rudy", "Switchfully", "rudy@switchfully.com",
                new Address("Rue des loups", "2", "1000", "Bruxelles"), "0472548963");
        bike = new Item("VTT", "Sport Bike", new BigDecimal("350.00"), 10);
        customerRepository.save(rudy);
        itemRepository.save(bike);
    }

    @Test
    void createEurder_withValidData_returnsEurderWithItemGroups() {
        List<ItemGroupRequest> requests = List.of(new ItemGroupRequest(bike.getId(), 2));

        Eurder eurder = eurderService.createEurder(rudy.getId(), requests);

        assertThat(eurder.getItemGroups()).hasSize(1);
    }

    @Test
    void createEurder_withUnknownCustomerId_throwsException() {
        assertThatThrownBy(() -> eurderService.createEurder(UUID.randomUUID(), List.of(new ItemGroupRequest(bike.getId(), 2)))).isInstanceOf(CustomerNotFoundException.class);
    }

    @Test
    void createEurder_withUnknownItemGroupId_throwsException() {
        assertThatThrownBy(() -> eurderService.createEurder(rudy.getId(), List.of(new ItemGroupRequest(UUID.randomUUID(), 2)))).isInstanceOf(ItemNotFoundException.class);
    }
}
