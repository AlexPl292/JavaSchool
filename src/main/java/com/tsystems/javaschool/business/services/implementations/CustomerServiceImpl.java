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
import com.tsystems.javaschool.util.Email;
import com.tsystems.javaschool.util.PassGen;
import org.apache.commons.codec.binary.Hex;

import javax.persistence.EntityGraph;
import javax.persistence.EntityTransaction;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * Created by alex on 17.08.16.
 */
public class CustomerServiceImpl implements CustomerService {

    private CustomerDao customerDao = new CustomerDaoImpl();

    @Override
    public Customer addNew(Customer customer) {
        /*
        Пароль НЕ должен быть введен сотрудником.
        Будем его вручную генерировать, а потом посылать с помощью email или sms
         */
        String password = new PassGen(10).nextPassword();
        MessageDigest md = null;
        // TODO заменить эту дербендю на апаче хэш
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        assert md != null;
        md.update(password.getBytes());
        String hashed = Hex.encodeHexString(md.digest());
        String salt = new PassGen(8).nextPassword();
        md.update((hashed+salt).getBytes());

        Email.sendSimpleEmail(customer.getEmail(), password); // Это заглушка. На самом деле просто вывод на консоль
        customer.setPassword(Hex.encodeHexString(md.digest()));
        customer.setSalt(salt);
        EntityTransaction transaction = customerDao.getTransaction();
        transaction.begin();
        customer = customerDao.create(customer);
        transaction.commit();
        return customer;
    }

    @Override
    public List<Customer> getNEntries(int maxResult, int firstResult) {
        return customerDao.selectFromTo(maxResult, firstResult);
    }

    @Override
    public long countOfEntries() {
        return customerDao.countOfEntities();
    }

    @Override
    public List<Customer> getNEntries(int maxEntries, int firstIndex, String searchQuery) {
        if ("".equals(searchQuery))
            return getNEntries(maxEntries, firstIndex);
        return customerDao.importantSearchFromTo(maxEntries, firstIndex, searchQuery);
    }

    @Override
    public long countOfEntries(String searchQuery) {
        if ("".equals(searchQuery))
            return countOfEntries();
        return customerDao.countOfImportantSearch(searchQuery);
    }

    @Override
    public List<Customer> loadAll() {
        return customerDao.getAll();
    }

    @Override
    public Customer loadByKey(Integer key) {
        return customerDao.read(key);
    }

    @Override
    public EntityGraph getEntityGraph() {
        return customerDao.getEntityGraph();
    }

    @Override
    public void remove(Integer key) {
        EntityTransaction transaction = customerDao.getTransaction();
        transaction.begin();
        customerDao.delete(key);
        transaction.commit();
    }

/*    @Override
    public void createCustomerAndContract(Customer customer, Contract contract, List<Integer> contractOptionsIds) {
        EntityTransaction transaction = customerDao.getTransaction();
        ContractDao contractDao = new ContractDaoImpl();

        transaction.begin();
        OptionService optionService = new OptionServiceImpl();
        contract.setUsedOptions(optionService.loadOptionsByIds(contractOptionsIds));

        Customer newCustomer = customerDao.create(customer);
        contract.setCustomer(newCustomer);
        contractDao.create(contract);
        transaction.commit();
    }*/
}
