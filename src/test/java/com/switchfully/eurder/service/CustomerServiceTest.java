package com.switchfully.eurder.service;

import com.switchfully.eurder.domain.Address;
import com.switchfully.eurder.domain.Customer;
import com.switchfully.eurder.exception.CustomerNotFoundException;
import com.switchfully.eurder.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class CustomerServiceTest {

    private CustomerRepository customerRepository;
    private CustomerService customerService;
    private Customer rudy;

    @BeforeEach
    void setUp() {
        customerRepository = new CustomerRepository();
        customerService = new CustomerService(customerRepository, new BCryptPasswordEncoder());
        Address address = new Address("Rue des loups", "2", "1000", "Bruxelles");
        rudy = new Customer("Rudy", "Switchfully", "rudy@switchfully.com", address, "0472548963", "password123");

    }

    @Test
    void whenCustomerExists_thenReturnCustomer() {
        customerRepository.save(rudy);
        Customer result = customerService.getCustomerById(rudy.getId());
        assertThat(result).isEqualTo(rudy);
    }

    @Test
    void whenCustomerDoesNotExist_thenThrowException() {
        assertThatThrownBy(() -> customerService.getCustomerById(rudy.getId())).isInstanceOf(CustomerNotFoundException.class);
    }


}
