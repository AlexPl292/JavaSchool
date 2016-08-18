package com.tsystems.javaschool.business.services.implementations;

import com.tsystems.javaschool.business.services.interfaces.CustomerService;
import com.tsystems.javaschool.db.entities.Customer;
import com.tsystems.javaschool.db.implemetations.CustomerDaoImpl;
import com.tsystems.javaschool.db.interfaces.CustomerDao;

/**
 * Created by alex on 17.08.16.
 */
public class CustomerServiceImpl implements CustomerService {

    private CustomerDao customerDao = new CustomerDaoImpl();

    public void addCustomer(Customer customer) {
        customerDao.create(customer);
    }
}
