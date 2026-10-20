package com.switchfully.eurder.api.mapper;

import com.switchfully.eurder.api.dto.CreateItemDto;
import com.switchfully.eurder.api.dto.ItemDto;
import com.switchfully.eurder.domain.Item;
import org.springframework.stereotype.Component;

@Component
public class ItemMapper {

    public Item toItem(CreateItemDto dto) {
        return new Item(dto.name(), dto.description(), dto.price(), dto.stock());
    }

    public ItemDto toItemDto(Item item) {
        return new ItemDto(item.getId(), item.getName(), item.getDescription(), item.getPrice(),  item.getStock());
    }
}
