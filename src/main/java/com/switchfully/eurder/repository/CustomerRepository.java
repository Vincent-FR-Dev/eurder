package com.switchfully.eurder.repository;

import com.switchfully.eurder.domain.Customer;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
public class CustomerRepository {

    private final Map<UUID, Customer> customers = new HashMap<>();


    public CustomerRepository() {

    }

    public Optional<Customer> getById(UUID id) {
        return Optional.ofNullable(customers.get(id));
    }

    public Customer save(Customer customer) {
        customers.put(customer.getId(), customer);
        return customer;
    }


}
