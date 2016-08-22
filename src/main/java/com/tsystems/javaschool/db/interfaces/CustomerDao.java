package com.tsystems.javaschool.db.interfaces;

import com.tsystems.javaschool.db.entities.Customer;

import java.util.List;

/**
 * Created by alex on 19.08.16.
 */
public interface CustomerDao extends GenericDao<Customer, Integer>{
    List<Customer> selectFromTo(int from, int to);

    long countOfCustomers();
}
