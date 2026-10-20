package com.switchfully.eurder.repository;

import com.switchfully.eurder.domain.Address;
import com.switchfully.eurder.domain.Customer;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

public class CustomerRepositoryTest {

    private CustomerRepository customerRepository;
    private Customer rudy;
    private Customer karel;

    @BeforeEach
    void setUp() {
        customerRepository = new CustomerRepository();
        Address address = new Address("Rue des loups", "2", "1000", "Bruxelles");
        rudy = new Customer("Rudy", "Switchfully", "rudy@switchfully.com", address, "0472548963");
        karel = new Customer("Karel", "Switchfully", "karel@switchfully.com", address, "0472548123");
    }

    @Test
    void findCustomerById_whenCustomerIsSaved(){
        customerRepository.save(rudy);
        Optional<Customer> result = customerRepository.getById(rudy.getId());
        assertThat(result).contains(rudy);
    }

    @Test
    void iFindEmptyCustomer_whenIdDoesntExist(){
        Optional<Customer> result = customerRepository.getById(rudy.getId());
        assertThat(result).isEmpty();
    }

    @Test
    void whenTwoOrMoreCustomersAreSaved_thenReturnSaved(){
        customerRepository.save(rudy);
        customerRepository.save(karel);
        List<Customer> result = customerRepository.getAll();
        assertThat(result).containsExactlyInAnyOrder(karel, rudy);
    }
}
