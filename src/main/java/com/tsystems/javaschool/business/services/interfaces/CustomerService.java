package com.tsystems.javaschool.business.services.interfaces;

import com.tsystems.javaschool.db.entities.Contract;
import com.tsystems.javaschool.db.entities.Customer;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * Created by alex on 19.08.16.
 */
public interface CustomerService extends GenericService<Customer, Integer> {
    void createCustomerAndContract(Customer customer, Contract contract);
}
