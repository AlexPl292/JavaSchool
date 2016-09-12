package com.tsystems.javaschool.business.services.interfaces;

import com.tsystems.javaschool.db.entities.Customer;

import java.util.Map;

/**
 * Created by alex on 19.08.16.
 */
public interface CustomerService extends GenericService<Customer, Integer> {
    Customer loadByKey(Integer key, Map<String, Object> hints);
}
