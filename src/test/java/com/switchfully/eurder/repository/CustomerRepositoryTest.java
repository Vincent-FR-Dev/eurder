package com.switchfully.eurder.repository;

import com.switchfully.eurder.domain.Address;
import com.switchfully.eurder.domain.Customer;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@SpringBootTest
@Transactional
public class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;
    private Customer rudy;
    private Customer karel;

    @BeforeEach
    void setUp() {
        Address address = new Address("Rue des loups", "2", "1000", "Bruxelles");
        rudy = new Customer("Rudy", "Switchfully", "rudy@switchfully.com", address, "0472548963", "password123");
        karel = new Customer("Karel", "Switchfully", "karel@switchfully.com", address, "0472548123", "password123");
    }

    @Test
    void findCustomerById_whenCustomerIsSaved(){
        Customer saved = customerRepository.save(rudy);
        Optional<Customer> result = customerRepository.findById(rudy.getId());
        assertThat(result).contains(saved);
    }

    @Test
    void iFindEmptyCustomer_whenIdDoesntExist(){
        Optional<Customer> result = customerRepository.findById(rudy.getId());
        assertThat(result).isEmpty();
    }

    @Test
    void whenTwoOrMoreCustomersAreSaved_thenReturnSaved(){
        Customer savedRudy = customerRepository.save(rudy);
        Customer savedKarel = customerRepository.save(karel);
        List<Customer> result = customerRepository.findAll();
        assertThat(result).contains(savedKarel, savedRudy);
    }

    @Test
    void findCustomerByEmail_whenCustomerIsSaved(){
        Customer saved = customerRepository.save(rudy);
        Optional<Customer> result = customerRepository.findByEmail(rudy.getEmail());
        assertThat(result).contains(saved);
    }

    @Test
    void findCustomerByEmail_whenEmailIsUnknown_returnsEmpty() {
        customerRepository.save(rudy);
        Optional<Customer> result = customerRepository.findByEmail("unknown@switchfully.com");
        assertThat(result).isEmpty();
    }

    @Test
    void existsByEmail_whenCustomerIsSaved_returnsTrue() {
        customerRepository.save(rudy);
        assertThat(customerRepository.existsByEmail(rudy.getEmail())).isTrue();
        assertThat(customerRepository.existsByEmail("unknown@switchfully.com")).isFalse();
    }
}
