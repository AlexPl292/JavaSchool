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
        customer.setDate_of_birth(new Date());
        customer.setEmail("test@test.ru");
        customer.setIs_blocked(0);
        customer.setName("My");
        customer.setSurname("God");
        customer.setPassport_data("789097980");

        service.addNew(customer);
    }

}