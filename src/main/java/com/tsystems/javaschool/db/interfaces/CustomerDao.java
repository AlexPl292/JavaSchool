package com.tsystems.javaschool.db.interfaces;

import com.tsystems.javaschool.db.entities.Customer;

/**
 * Created by alex on 19.08.16.
 *
 * Search fields are name and surname
 * Order is not important. Could contain two words
 */
public interface CustomerDao extends GenericDao<Customer, Integer>{
}
