package com.tsystems.javaschool.db.repository;

import com.tsystems.javaschool.db.entities.Tariff;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by alex on 27.09.16.
 */
public interface TariffRepository extends JpaRepository<Tariff, Integer> {
    List<Tariff> findByName(String name);
}
