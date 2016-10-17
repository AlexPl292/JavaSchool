package com.tsystems.javaschool.db.repository;

import com.tsystems.javaschool.db.entities.Contract;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by alex on 28.09.16.
 */
public interface ContractRepository extends JpaRepository<Contract, Integer> {
    List<Contract> findByNumber(String number);

    List<Contract> findByTariff_Name(String name);
}
