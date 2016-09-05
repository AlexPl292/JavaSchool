package com.tsystems.javaschool.business.services.interfaces;

import com.tsystems.javaschool.db.entities.Contract;
import com.tsystems.javaschool.db.entities.Customer;

import java.util.List;

/**
 * Created by alex on 19.08.16.
 */
public interface CustomerService extends GenericService<Customer, Integer> {
    /**
     * Add new customer and new contract in one transaction
     * To contract could be added options from contractOptionsIds
     * @param customer customer to add
     * @param contract contract to add
     * @param contractOptionsIds list of ids of options for adding to contract
     */
//    void createCustomerAndContract(Customer customer, Contract contract, List<Integer> contractOptionsIds);
}
