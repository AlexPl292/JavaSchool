package com.tsystems.javaschool.business.services;

import com.tsystems.javaschool.db.entities.Customer;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

/**
 * Created by alex on 17.08.16.
 */
public class CustomerServiceTest {

    CustomerService service = new CustomerService();

    @Test
    public void add() throws Exception {
        Customer customer = new Customer();
        customer.setAddress("address");
        customer.setDay_of_birth(new Date());
        customer.setEmail("test@test.ru");
        customer.setIs_blocked(0);
        customer.setName("Martin");
        customer.setSurname("Joy");
        customer.setPassport_data("789097980");
        customer.setPassword(5);

        Customer newCustomer = service.add(customer);
        System.out.println(newCustomer);
    }

}