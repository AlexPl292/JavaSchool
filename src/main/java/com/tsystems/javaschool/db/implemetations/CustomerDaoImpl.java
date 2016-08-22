package com.tsystems.javaschool.db.implemetations;

import com.tsystems.javaschool.db.entities.Customer;
import com.tsystems.javaschool.db.interfaces.CustomerDao;

import java.util.List;

/**
 * Created by alex on 19.08.16.
 */
public class CustomerDaoImpl extends GenericDaoImpl<Customer, Integer> implements CustomerDao {
    @Override
    public List<Customer> selectFromTo(int maxResults, int firstResult) {
        return em.createQuery("SELECT NEW Customer(c.name, c.surname, c.email, c.is_blocked) FROM Customer c", Customer.class)
                .setFirstResult(firstResult)
                .setMaxResults(maxResults)
                .getResultList();
    }

    @Override
    public long countOfCustomers() {
        return (long) em.createQuery("SELECT count(c.id) FROM Customer c").getSingleResult();
    }
}
