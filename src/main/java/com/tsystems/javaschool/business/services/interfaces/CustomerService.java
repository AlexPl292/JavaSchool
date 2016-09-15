package com.tsystems.javaschool.business.services.interfaces;

import com.tsystems.javaschool.db.entities.Customer;

import java.util.Map;

/**
 * Created by alex on 19.08.16.
 */
public interface CustomerService extends GenericService<Customer, Integer> {
    /**
     * Load customer by key with dependencies
     * @param key id of customer
     * @param hints dependencies
     * @return customer with id = key
     */
    Customer loadByKey(Integer key, Map<String, Object> hints);
}
