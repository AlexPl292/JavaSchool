package com.tsystems.javaschool.business.services.implementations;

import com.tsystems.javaschool.business.services.interfaces.ContractService;
import com.tsystems.javaschool.business.services.interfaces.CustomerService;
import com.tsystems.javaschool.business.services.interfaces.OptionService;
import com.tsystems.javaschool.db.entities.Contract;
import com.tsystems.javaschool.db.entities.Customer;
import com.tsystems.javaschool.db.implemetations.ContractDaoImpl;
import com.tsystems.javaschool.db.implemetations.CustomerDaoImpl;
import com.tsystems.javaschool.db.interfaces.ContractDao;
import com.tsystems.javaschool.db.interfaces.CustomerDao;
import com.tsystems.javaschool.util.EMU;
import com.tsystems.javaschool.util.Email;
import com.tsystems.javaschool.util.PassGen;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import javax.persistence.EntityGraph;
import javax.persistence.EntityTransaction;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by alex on 17.08.16.
 */
public class CustomerServiceImpl implements CustomerService {

    private CustomerDao customerDao = CustomerDaoImpl.getInstance();

    private CustomerServiceImpl() {}

    private static class CustomerServiceHolder {
        private static final CustomerServiceImpl instance = new CustomerServiceImpl();
    }

    public static CustomerServiceImpl getInstance() {
        return CustomerServiceHolder.instance;
    }

    @Override
    public void addNew(Customer customer) {
        /*
        Пароль НЕ должен быть введен сотрудником.
        Будем его вручную генерировать, а потом посылать с помощью email или sms
         */
        String password = new PassGen(10).nextPassword();
        String hashed = DigestUtils.sha256Hex(password);
        String salt = new PassGen(8).nextPassword();

        Email.sendSimpleEmail(customer.getEmail(), password); // Это заглушка. На самом деле просто вывод на консоль
        customer.setPassword(DigestUtils.sha256Hex(hashed + salt));
        customer.setSalt(salt);
        try {
            EMU.beginTransaction();
            customerDao.create(customer);
            EMU.commit();
        } catch (RuntimeException re) {
            if (EMU.getEntityManager() != null && EMU.getEntityManager().isOpen())
                EMU.rollback();
            throw re;
        } finally {
            EMU.closeEntityManager();
        }
    }

/*    @Override
    public List<Customer> getNEntries(int maxResult, int firstResult) {
        List<Customer> customers = customerDao.selectFromTo(maxResult, firstResult);
        EMU.closeEntityManager();
        return customers;
    }

    @Override
    public long countOfEntries() {
        long res = customerDao.countOfEntities();
        EMU.closeEntityManager();
        return res;
    }

    @Override
    public List<Customer> getNEntries(int maxEntries, int firstIndex, String searchQuery) {
        if ("".equals(searchQuery))
            return getNEntries(maxEntries, firstIndex);
        List<Customer> customers = customerDao.importantSearchFromTo(maxEntries, firstIndex, searchQuery);
        EMU.closeEntityManager();
        return customers;
    }

    @Override
    public long countOfEntries(String searchQuery) {
        if ("".equals(searchQuery))
            return countOfEntries();
        long res = customerDao.countOfImportantSearch(searchQuery);
        EMU.closeEntityManager();
        return res;
    }

    @Override
    public List<Customer> loadAll() {
        List<Customer> customers = customerDao.getAll();
        EMU.closeEntityManager();
        return customers;
    }*/

    @Override
    public Customer loadByKey(Integer key) {
        Customer customer = customerDao.read(key);
        EMU.closeEntityManager();
        return customer;
    }

    @Override
    public EntityGraph getEntityGraph() {
        return customerDao.getEntityGraph();
    }

    @Override
    public void remove(Integer key) {
        try {
            EMU.beginTransaction();
            customerDao.delete(key);
            EMU.commit();
        } catch (RuntimeException re) {
            if (EMU.getEntityManager() != null && EMU.getEntityManager().isOpen())
                EMU.rollback();
            throw re;
        } finally {
            EMU.closeEntityManager();
        }
    }

    @Override
    public Customer loadByKey(Integer key, Map<String, Object> hints) {
        Customer customer = customerDao.read(key, hints);
        EMU.closeEntityManager();
        return customer;
    }

    @Override
    public List<Customer> load(Map<String, Object> kwargs) {
        List<Customer> customers = customerDao.read(kwargs);
        EMU.closeEntityManager();
        return customers;
    }

    @Override
    public long count(Map<String, Object> kwargs) {
        long count = customerDao.count(kwargs);
        EMU.closeEntityManager();
        return count;
    }

    @Override
    public List<Customer> loadAll() {
        return load(new HashMap<>());
    }
}
