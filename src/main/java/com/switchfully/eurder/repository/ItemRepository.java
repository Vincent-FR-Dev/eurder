package com.switchfully.eurder.repository;

import com.switchfully.eurder.domain.Item;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
public class ItemRepository {

    private final Map<UUID, Item> items = new HashMap<>();

    public Optional<Item> getById(UUID id){
        return Optional.ofNullable(items.get(id));
    }

    public Item save(Item item){
        items.put(item.getId(), item);
        return item;
    }
}
