package com.switchfully.eurder.repository;

import com.switchfully.eurder.domain.Eurder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EurderRepository extends JpaRepository<Eurder, UUID> {


}
