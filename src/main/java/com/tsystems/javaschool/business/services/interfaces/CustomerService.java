package com.tsystems.javaschool.business.services.interfaces;

import com.tsystems.javaschool.db.entities.Customer;

import java.util.List;

/**
 * Created by alex on 19.08.16.
 */
public interface CustomerService {
    void addCustomer(Customer customer);

    List<Customer> getNCustomers(int maxResult, int firstResult);

    long countOfCustomers();
}
