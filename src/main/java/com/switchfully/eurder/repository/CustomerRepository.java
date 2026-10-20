package com.switchfully.eurder.repository;

import com.switchfully.eurder.domain.Customer;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class CustomerRepository {

    private final Map<UUID, Customer> customers = new HashMap<>();

    public Optional<Customer> getById(UUID id) {
        return Optional.ofNullable(customers.get(id));
    }

    public Customer save(Customer customer) {
        customers.put(customer.getId(), customer);
        return customer;
    }

    public List<Customer> getAll() {
        return customers.values().stream().toList();
    }



}
