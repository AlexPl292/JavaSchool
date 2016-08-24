package com.tsystems.javaschool.business.services;

import com.tsystems.javaschool.business.services.implementations.CustomerServiceImpl;
import com.tsystems.javaschool.business.services.interfaces.CustomerService;
import com.tsystems.javaschool.db.entities.Customer;
import org.junit.Test;

import java.util.Date;

/**
 * Created by alex on 17.08.16.
 */
public class CustomerServiceImplTest {

    CustomerService service = new CustomerServiceImpl();

    @Test
    public void add() throws Exception {
        Customer customer = new Customer();
        customer.setAddress("address");
        customer.setDateOfBirth(new Date());
        customer.setEmail("test@test.ru");
        customer.setIsBlocked(0);
        customer.setName("My");
        customer.setSurname("God");
        customer.setPassportData("789097980");

        service.addNew(customer);
    }

}