package com.switchfully.eurder.api.mapper;

import com.switchfully.eurder.api.dto.AddressDto;
import com.switchfully.eurder.api.dto.CreateCustomerDto;
import com.switchfully.eurder.api.dto.CustomerDto;
import com.switchfully.eurder.domain.Address;
import com.switchfully.eurder.domain.Customer;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {

    public Customer toCustomer(CreateCustomerDto dto) {
        return new Customer(dto.firstname(), dto.lastname(), dto.email(), toAddress(dto.address()), dto.phoneNumber());
    }

    public CustomerDto toCustomerDto(Customer customer) {
        return new CustomerDto(customer.getId(), customer.getFirstname(), customer.getLastname(), customer.getEmail(), toAddressDto(customer.getAddress()), customer.getPhoneNumber());
    }

    private Address toAddress(AddressDto dto) {
        return new Address(dto.streetName(), dto.streetNumber(), dto.postalCode(),  dto.city());
    }

    private AddressDto toAddressDto(Address address) {
        return new AddressDto(address.streetName(),  address.streetNumber(), address.postalCode(), address.city());
    }


}
