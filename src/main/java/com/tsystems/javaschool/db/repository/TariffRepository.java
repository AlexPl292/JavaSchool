package com.tsystems.javaschool.db.repository;

import com.tsystems.javaschool.db.entities.Tariff;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by alex on 27.09.16.
 *
 * Repository for tariffs
 */
public interface TariffRepository extends JpaRepository<Tariff, Integer> {
    /**
     * Search for tariff by name
     * @param name name
     * @return tariff with name
     */
    Tariff findByName(String name);
}
