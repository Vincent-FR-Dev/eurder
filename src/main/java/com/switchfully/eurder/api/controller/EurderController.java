package com.switchfully.eurder.api.controller;

import com.switchfully.eurder.api.dto.CreateEurderDto;
import com.switchfully.eurder.api.dto.CustomerDto;
import com.switchfully.eurder.api.dto.EurderDto;
import com.switchfully.eurder.api.dto.ReorderDto;
import com.switchfully.eurder.api.mapper.EurderMapper;
import com.switchfully.eurder.domain.Eurder;
import com.switchfully.eurder.service.EurderService;
import jakarta.validation.Valid;
import jakarta.validation.ValidatorFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/eurders")
public class EurderController {

    private final EurderService eurderService;
    private final EurderMapper eurderMapper;
    private final ValidatorFactory validatorFactory;

    public EurderController(EurderService eurderService, EurderMapper eurderMapper, ValidatorFactory validatorFactory) {
        this.eurderService = eurderService;
        this.eurderMapper = eurderMapper;
        this.validatorFactory = validatorFactory;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EurderDto createEurder(@RequestBody @Valid CreateEurderDto createEurderDto) {
        Eurder eurder = eurderService.createEurder(createEurderDto.customerId(), eurderMapper.toItemGroupRequests(createEurderDto));
        return eurderMapper.toEurderDto(eurder);
    }

    @PostMapping("/{id}/reorder")
    @ResponseStatus(HttpStatus.CREATED)
    public EurderDto reorder(@PathVariable UUID id, @RequestBody @Valid ReorderDto reorderDto){
        Eurder eurder = eurderService.reorder(id, reorderDto.customerId());
        return eurderMapper.toEurderDto(eurder);
    }
}
