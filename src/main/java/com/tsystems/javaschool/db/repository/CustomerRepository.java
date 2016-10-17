package com.tsystems.javaschool.db.repository;

import com.tsystems.javaschool.db.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by alex on 28.09.16.
 *
 * Repository for customers
 */
public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    /**
     * Search for customer by passport number or email
     * @param passportNumber passport number
     * @param email email
     * @return list of customers
     */
    List<Customer> findByPassportNumberOrEmail(String passportNumber, String email);

    /**
     * Search for customer by number
     * @param number number
     * @return customer with contract
     */
    Customer findByContracts_Number(String number);
}
