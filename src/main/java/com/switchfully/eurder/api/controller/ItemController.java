package com.switchfully.eurder.api.controller;

import com.switchfully.eurder.api.dto.CreateItemDto;
import com.switchfully.eurder.api.dto.ItemDto;
import com.switchfully.eurder.api.mapper.ItemMapper;
import com.switchfully.eurder.domain.Item;
import com.switchfully.eurder.service.ItemService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;
    private final ItemMapper itemMapper;

    public ItemController(ItemService itemService, ItemMapper itemMapper) {
        this.itemService = itemService;
        this.itemMapper = itemMapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDto createItem(@RequestBody @Valid CreateItemDto createItemDto) {
        Item item = itemMapper.toItem(createItemDto);
        Item createdItem = itemService.createItem(item);
        return itemMapper.toItemDto(createdItem);

    }
}
