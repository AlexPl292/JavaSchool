package com.tsystems.javaschool.db.implemetations;

import com.tsystems.javaschool.db.entities.Customer;
import com.tsystems.javaschool.db.interfaces.CustomerDao;
import com.tsystems.javaschool.util.EMU;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Map;

/**
 * Created by alex on 19.08.16.
 *
 * JPA implementation of CustomerDao
 */
public class CustomerDaoImpl extends GenericDaoImpl<Customer, Integer> implements CustomerDao {

    private CustomerDaoImpl() {}

    private static class CustomerDaoHolder {
        private static final CustomerDaoImpl instance = new CustomerDaoImpl();
        private CustomerDaoHolder() {}
    }

    public static CustomerDaoImpl getInstance() {
        return CustomerDaoHolder.instance;
    }

    @Override
    public List<Customer> read(Map<String, Object> kwargs) {
        String queryStr = "SELECT NEW Customer(c.id, c.name, c.surname, c.email, c.isBlocked, c.passportNumber) FROM Customer c";
        String search = (String) kwargs.get("search");
        Integer maxEntries = (Integer) kwargs.get("maxEntries");
        Integer firstIndex = (Integer) kwargs.get("firstIndex");
        Object graph = kwargs.get("graph");
        String[] where = null;
        if (search != null && !"".equals(search)) {
            where = search.split("\\s+");
            if (where.length == 1) {
                queryStr += " WHERE c.name LIKE :first OR c.surname LIKE :first";
            } else {
                queryStr += " WHERE c.name LIKE :first AND c.surname LIKE :second OR c.name LIKE :second AND c.surname LIKE :first";
            }
        }

        TypedQuery<Customer> query = EMU.getEntityManager().createQuery(queryStr, Customer.class);
        if (where != null) {
            if (where.length == 1) {
                query.setParameter("first", "%" + where[0] + "%");
            } else {
                query.setParameter("first", "%" + where[0] + "%");
                query.setParameter("second", "%" + where[1] + "%");
            }
        }
        if (maxEntries != null)
            query.setMaxResults(maxEntries);
        if (firstIndex != null)
            query.setFirstResult(firstIndex);
        if (graph != null)
            query.setHint("javax.persistence.loadgraph", graph);
        return query.getResultList();
    }

    @Override
    public long count(Map<String, Object> kwargs) {
        String queryStr = "SELECT COUNT(c.id) FROM Customer c";
        String search = (String) kwargs.get("search");
        String[] where = null;
        if (search != null && !"".equals(search)) {
            where = search.split("\\s+");
            if (where.length == 1) {
                queryStr += " WHERE c.name LIKE :first OR c.surname LIKE :first";
            } else {
                queryStr += " WHERE c.name LIKE :first AND c.surname LIKE :second OR c.name LIKE :second AND c.surname LIKE :first";
            }
        }

        Query query = EMU.getEntityManager().createQuery(queryStr);
        if (where != null) {
            if (where.length == 1) {
                query.setParameter("first", "%" + where[0] + "%");
            } else {
                query.setParameter("first", "%" + where[0] + "%");
                query.setParameter("second", "%" + where[1] + "%");
            }
        }
        return (long) query.getSingleResult();
    }
}
