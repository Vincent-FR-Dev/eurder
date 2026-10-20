package com.switchfully.eurder.repository;

import com.switchfully.eurder.domain.Eurder;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
public class EurderRepository {

    private final Map<UUID, Eurder> eurders = new HashMap<>();

    public Optional<Eurder> getById(UUID id) {
        return Optional.ofNullable(eurders.get(id));
    }

    public Eurder save(Eurder eurder) {
        eurders.put(eurder.getId(), eurder);
        return eurder;
    }

}
