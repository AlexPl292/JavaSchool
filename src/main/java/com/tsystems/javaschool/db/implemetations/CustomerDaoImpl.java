package com.tsystems.javaschool.db.implemetations;

import com.tsystems.javaschool.db.entities.Customer;
import com.tsystems.javaschool.db.interfaces.CustomerDao;
import com.tsystems.javaschool.util.EMU;

import java.util.List;
import java.util.Map;

/**
 * Created by alex on 19.08.16.
 */
public class CustomerDaoImpl extends GenericDaoImpl<Customer, Integer> implements CustomerDao {

    private CustomerDaoImpl() {}

    private static class CustomerDaoHolder {
        private static final CustomerDaoImpl instance = new CustomerDaoImpl();
    }

    public static CustomerDaoImpl getInstance() {
        return CustomerDaoHolder.instance;
    }

        @Override
    public List<Customer> selectFromTo(int maxResults, int firstResult) {
        return EMU.getEntityManager().createQuery("SELECT NEW Customer(c.id, c.name, c.surname, c.email, c.isBlocked, c.passportNumber) FROM Customer c", Customer.class)
                .setFirstResult(firstResult)
                .setMaxResults(maxResults)
                .getResultList();
    }

    @Override
    public long countOfEntities() {
        return (long) EMU.getEntityManager().createQuery("SELECT count(c.id) FROM Customer c").getSingleResult();
    }

    @Override
    public List<Customer> importantSearchFromTo(int maxEntries, int firstIndex, String searchQuery) {
        String[] wheres = searchQuery.split("\\s+");
        if (wheres.length == 1) {
            String query = "SELECT NEW Customer(c.id, c.name, c.surname, c.email, c.isBlocked, c.passportNumber) FROM Customer c WHERE c.name LIKE :first OR c.surname LIKE :first";
            return EMU.getEntityManager().createQuery(query, Customer.class)
                    .setParameter("first", "%"+wheres[0]+"%")
                    .setFirstResult(firstIndex)
                    .setMaxResults(maxEntries)
                    .getResultList();
        } else {
            String query = "SELECT NEW Customer(c.id, c.name, c.surname, c.email, c.isBlocked, c.passportNumber) FROM Customer c " +
                    "WHERE c.name LIKE :first AND c.surname LIKE :second OR c.name LIKE :second AND c.surname LIKE :first";
            return EMU.getEntityManager().createQuery(query, Customer.class)
                    .setParameter("first", "%"+wheres[0]+"%")
                    .setParameter("second", "%"+wheres[1]+"%")
                    .setFirstResult(firstIndex)
                    .setMaxResults(maxEntries)
                    .getResultList();
        }
    }

    @Override
    public long countOfImportantSearch(String searchQuery) {
        String[] wheres = searchQuery.split("\\s+");
        if (wheres.length == 1) {
            String query = "SELECT COUNT(c.id) FROM Customer c WHERE c.name LIKE :first OR c.surname LIKE :first";
            return (long) EMU.getEntityManager().createQuery(query)
                    .setParameter("first", "%"+wheres[0]+"%")
                    .getSingleResult();
        } else {
            String query = "SELECT COUNT(c.id) FROM Customer c " +
                    "WHERE c.name LIKE :first AND c.surname LIKE :second OR c.name LIKE :second AND c.surname LIKE :first";
            return (long) EMU.getEntityManager().createQuery(query)
                    .setParameter("first", "%"+wheres[0]+"%")
                    .setParameter("second", "%"+wheres[1]+"%")
                    .getSingleResult();
        }
    }

    @Override
    public List<Customer> getAll() {
        return EMU.getEntityManager().createQuery("SELECT NEW Customer(c.id, c.name, c.surname, c.email, c.isBlocked, c.passportNumber) FROM Customer c", Customer.class)
                .getResultList();
    }

    @Override
    public Customer read(Integer key, Map<String, Object> hints) {
        return EMU.getEntityManager().find(Customer.class, key, hints);
    }
}
