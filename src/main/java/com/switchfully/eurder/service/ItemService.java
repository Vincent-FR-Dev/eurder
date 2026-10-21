package com.switchfully.eurder.service;

import com.switchfully.eurder.domain.Item;
import com.switchfully.eurder.exception.ItemNotFoundException;
import com.switchfully.eurder.repository.ItemRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class ItemService {

    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public Item createItem(Item item) {
        return itemRepository.save(item);
    }

    public Item getItemById(UUID id) {
        return itemRepository.getById(id).orElseThrow(() -> new ItemNotFoundException(id));
    }

    public Item updateItem(UUID id, String name, String description, BigDecimal price, int stock) {
        Item item = getItemById(id);
        item.update(name, description, price, stock);
        return item;
    }
}