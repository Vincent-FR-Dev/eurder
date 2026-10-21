package com.switchfully.eurder.api.controller;

import com.switchfully.eurder.api.dto.CreateEurderDto;
import com.switchfully.eurder.api.dto.EurderDto;
import com.switchfully.eurder.api.mapper.EurderMapper;
import com.switchfully.eurder.domain.Eurder;
import com.switchfully.eurder.service.CustomerService;
import com.switchfully.eurder.service.EurderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/eurders")
public class EurderController {

    private final EurderService eurderService;
    private final EurderMapper eurderMapper;
    private final CustomerService customerService;

    public EurderController(EurderService eurderService, EurderMapper eurderMapper, CustomerService customerService) {
        this.eurderService = eurderService;
        this.eurderMapper = eurderMapper;
        this.customerService = customerService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EurderDto createEurder(@RequestBody @Valid CreateEurderDto createEurderDto, Authentication authentication) {
        UUID customerId = getConnectedCustomerId(authentication);
        Eurder eurder = eurderService.createEurder(customerId, eurderMapper.toItemGroupRequests(createEurderDto));
        return eurderMapper.toEurderDto(eurder);
    }

    @PostMapping("/{id}/reorder")
    @ResponseStatus(HttpStatus.CREATED)
    public EurderDto reorder(@PathVariable UUID id, Authentication authentication) {
        UUID customerId = getConnectedCustomerId(authentication);
        Eurder eurder = eurderService.reorder(id, customerId);
        return eurderMapper.toEurderDto(eurder);
    }

    private UUID getConnectedCustomerId(Authentication authentication) {
        return customerService.getCustomerByEmail(authentication.getName()).getId();
    }
}