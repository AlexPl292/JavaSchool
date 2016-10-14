package com.tsystems.javaschool.db.repository;

import com.tsystems.javaschool.db.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by alex on 28.09.16.
 */
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    List<Customer> findByPassportNumberOrEmail(String passportNumber, String email);
    Customer findByContracts_Number(String number);
}
