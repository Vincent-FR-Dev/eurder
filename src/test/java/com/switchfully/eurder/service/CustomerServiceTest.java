package com.switchfully.eurder.service;

import com.switchfully.eurder.domain.Address;
import com.switchfully.eurder.domain.Customer;
import com.switchfully.eurder.exception.CustomerNotFoundException;
import com.switchfully.eurder.exception.EmailAlreadyUsedException;
import com.switchfully.eurder.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
public class CustomerServiceTest {

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private CustomerService customerService;
    private Customer rudy;

    @BeforeEach
    void setUp() {
        Address address = new Address("Rue des loups", "2", "1000", "Bruxelles");
        rudy = new Customer("Rudy", "Switchfully", "rudy@switchfully.com", address, "0472548963", "password123");

    }

    @Test
    void whenCustomerExists_thenReturnCustomer() {
        Customer saved = customerRepository.save(rudy);
        Customer result = customerService.getCustomerById(rudy.getId());
        assertThat(result).isEqualTo(saved);
    }

    @Test
    void whenCustomerDoesNotExist_thenThrowException() {
        assertThatThrownBy(() -> customerService.getCustomerById(rudy.getId())).isInstanceOf(CustomerNotFoundException.class);
    }

    @Test
    void createCustomer_storesAnEncodedPassword() {
        Customer created = customerService.createCustomer(rudy);
        assertThat(created.getPassword()).isNotEqualTo("password123").startsWith("$2");
    }

    @Test
    void createCustomer_withEmailAlreadyUsed_throwsException() {
        customerService.createCustomer(rudy);
        Customer otherRudy = new Customer("Other", "Rudy", "rudy@switchfully.com",
                new Address("Rue des loups", "3", "1000", "Bruxelles"), "0472000000", "password456");

        assertThatThrownBy(() -> customerService.createCustomer(otherRudy)).isInstanceOf(EmailAlreadyUsedException.class);
    }
}
