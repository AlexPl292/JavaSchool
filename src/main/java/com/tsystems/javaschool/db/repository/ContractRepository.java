package com.tsystems.javaschool.db.repository;

import com.tsystems.javaschool.db.entities.Contract;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by alex on 28.09.16.
 *
 * Repository for contract
 */
public interface ContractRepository extends JpaRepository<Contract, Integer> {
    /**
     * Search for contract by number
     * @param number search number
     * @return contract entity
     */
    Contract findByNumber(String number);

    /**
     * Search for contracts by tariff name
     * @param name tariff name
     * @return list of cintract entities
     */
    List<Contract> findByTariff_Name(String name);
}
