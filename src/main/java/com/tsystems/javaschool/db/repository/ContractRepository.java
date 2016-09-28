package com.tsystems.javaschool.db.repository;

import com.tsystems.javaschool.db.entities.Contract;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by alex on 28.09.16.
 */
public interface ContractRepository extends JpaRepository<Contract, Integer>{
}
