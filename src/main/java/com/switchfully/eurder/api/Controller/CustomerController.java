package com.switchfully.eurder.api.Controller;

import com.switchfully.eurder.api.dto.CreateCustomerDto;
import com.switchfully.eurder.api.dto.CustomerDto;
import com.switchfully.eurder.api.mapper.CustomerMapper;
import com.switchfully.eurder.domain.Customer;
import com.switchfully.eurder.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService customerService;
    private final CustomerMapper customerMapper;

    public CustomerController(CustomerService customerService, CustomerMapper customerMapper) {
        this.customerService = customerService;
        this.customerMapper = customerMapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerDto createCustomer(@RequestBody @Valid CreateCustomerDto createCustomerDto) {
        Customer customer = customerMapper.toCustomer(createCustomerDto);
        Customer createdCustomer = customerService.createCustomer(customer);
        return customerMapper.toCustomerDto(createdCustomer);

    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CustomerDto> getAll() {
        List<Customer> customers = customerService.getAllCustomers();
        return customers.stream().map(customer -> customerMapper.toCustomerDto(customer)).toList();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CustomerDto getCustomer(@PathVariable UUID id) {
        Customer customer = customerService.getCustomerById(id);
        return customerMapper.toCustomerDto(customer);

    }

}
