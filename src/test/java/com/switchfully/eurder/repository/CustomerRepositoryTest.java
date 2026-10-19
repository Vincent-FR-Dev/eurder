package com.switchfully.eurder.repository;

import com.switchfully.eurder.domain.Address;
import com.switchfully.eurder.domain.Customer;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

public class CustomerRepositoryTest {

    private CustomerRepository customerRepository;
    private Customer rudy;

    @BeforeEach
    void setUp() {
        customerRepository = new CustomerRepository();
        Address address = new Address("Rue des loups", "2", "1000", "Bruxelles");
        rudy = new Customer("Rudy", "Switchfully", "rudy@switchfully.com", address, "0472548963");
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
}
