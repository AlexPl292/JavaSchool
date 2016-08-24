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
    public long countOfEntities() {
        return (long) em.createQuery("SELECT count(c.id) FROM Customer c").getSingleResult();
    }

    @Override
    public List<Customer> importantSearchFromTo(int maxEntries, int firstIndex, String importantWhere) {
        String[] wheres = importantWhere.split("\\s+");
        if (wheres.length == 1) {
            String query = "SELECT NEW Customer(c.name, c.surname, c.email, c.is_blocked) FROM Customer c WHERE c.name LIKE :first OR c.surname LIKE :first";
            return em.createQuery(query, Customer.class)
                    .setParameter("first", "%"+wheres[0]+"%")
                    .setFirstResult(firstIndex)
                    .setMaxResults(maxEntries)
                    .getResultList();
        } else {
            String query = "SELECT NEW Customer(c.name, c.surname, c.email, c.is_blocked) FROM Customer c " +
                    "WHERE c.name LIKE :first AND c.surname LIKE :second OR c.name LIKE :second AND c.surname LIKE :first";
            return em.createQuery(query, Customer.class)
                    .setParameter("first", "%"+wheres[0]+"%")
                    .setParameter("second", "%"+wheres[1]+"%")
                    .setFirstResult(firstIndex)
                    .setMaxResults(maxEntries)
                    .getResultList();
        }
    }

    @Override
    public long countOfImportantSearch(String importantWhere) {
        String[] wheres = importantWhere.split("\\s+");
        if (wheres.length == 1) {
            String query = "SELECT COUNT(c.id) FROM Customer c WHERE c.name LIKE :first OR c.surname LIKE :first";
            return (long) em.createQuery(query)
                    .setParameter("first", "%"+wheres[0]+"%")
                    .getSingleResult();
        } else {
            String query = "SELECT COUNT(c.id) FROM Customer c " +
                    "WHERE c.name LIKE :first AND c.surname LIKE :second OR c.name LIKE :second AND c.surname LIKE :first";
            return (long) em.createQuery(query)
                    .setParameter("first", "%"+wheres[0]+"%")
                    .setParameter("second", "%"+wheres[1]+"%")
                    .getSingleResult();
        }
    }
}
