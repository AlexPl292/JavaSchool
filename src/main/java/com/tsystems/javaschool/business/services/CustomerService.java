package com.tsystems.javaschool.business.services;

import com.tsystems.javaschool.db.entities.Customer;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

/**
 * Created by alex on 17.08.16.
 */
public class CustomerService {

    private EntityManager em = Persistence.createEntityManagerFactory("JavaSchool").createEntityManager();

    public Customer add(Customer customer) {
        em.getTransaction().begin();
        Customer newCustomer = em.merge(customer);
        em.getTransaction().commit();
        return newCustomer;
    }
}
