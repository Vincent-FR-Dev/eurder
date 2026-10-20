package com.switchfully.eurder.api.controller;

import com.switchfully.eurder.api.dto.CreateEurderDto;
import com.switchfully.eurder.api.dto.EurderDto;
import com.switchfully.eurder.api.mapper.EurderMapper;
import com.switchfully.eurder.domain.Eurder;
import com.switchfully.eurder.service.EurderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/eurders")
public class EurderController {

    private final EurderService eurderService;
    private final EurderMapper eurderMapper;

    public EurderController(EurderService eurderService, EurderMapper eurderMapper) {
        this.eurderService = eurderService;
        this.eurderMapper = eurderMapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EurderDto createEurder(@RequestBody @Valid CreateEurderDto createEurderDto) {
        Eurder eurder = eurderService.createEurder(createEurderDto.customerId(), eurderMapper.toItemGroupRequests(createEurderDto));
        return eurderMapper.toEurderDto(eurder);
    }
}
