package com.tsystems.javaschool.business.services.implementations;

import com.tsystems.javaschool.business.dto.CustomerDto;
import com.tsystems.javaschool.business.services.interfaces.CustomerService;
import com.tsystems.javaschool.db.entities.Customer;
import com.tsystems.javaschool.db.interfaces.ContractDao;
import com.tsystems.javaschool.db.interfaces.CustomerDao;
import com.tsystems.javaschool.util.Email;
import com.tsystems.javaschool.util.PassGen;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityGraph;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by alex on 17.08.16.
 */
@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

    private final CustomerDao customerDao;
    private final ContractDao contractDao;
    private static final Logger logger = Logger.getLogger(CustomerServiceImpl.class);

    @Autowired
    public CustomerServiceImpl(CustomerDao customerDao, ContractDao contractDao) {
        this.customerDao = customerDao;
        this.contractDao = contractDao;
    }

    @Override
    public void addNew(CustomerDto customerDto) {
        /*
        Пароль НЕ должен быть введен сотрудником.
        Будем его вручную генерировать, а потом посылать с помощью email или sms
         */
        Customer customer = customerDto.convertCustomerEntity(contractDao);
        String password = new PassGen(10).nextPassword();
        String hashed = DigestUtils.sha256Hex(password);
        String salt = new PassGen(8).nextPassword();

        Email.sendSimpleEmail(customer.getEmail(), password); // Это заглушка.
        customer.setPassword(DigestUtils.sha256Hex(hashed + salt));
        customer.setSalt(salt);

        customerDao.create(customer);
        logger.info("New customer is created. Id = "+customer.getId());
    }

    @Override
    public CustomerDto loadByKey(Integer key) {
        Customer customer = customerDao.read(key);
        return new CustomerDto(customer);
    }

    @Override
    public EntityGraph getEntityGraph() {
        return customerDao.getEntityGraph();
    }

    @Override
    public void remove(Integer key) {
        customerDao.delete(key);
        logger.info("Customer is removed. Id = "+key);
    }

    @Override
    public CustomerDto loadByKey(Integer key, Map<String, Object> hints) {
        Customer customer = customerDao.read(key, hints);
        CustomerDto customerDto = new CustomerDto(customer);
        customerDto.setDependencies(customer);
        return customerDto;
    }

    @Override
    public List<CustomerDto> load(Map<String, Object> kwargs) {
        List<Customer> customers = customerDao.read(kwargs);
        List<CustomerDto> customerDtos = new ArrayList<>();
        for (Customer c:customers) {
            CustomerDto td = new CustomerDto(c);
            td.setDependencies(c);
            customerDtos.add(td);
        }
        return customerDtos;
    }

    @Override
    public long count(Map<String, Object> kwargs) {
        long count = customerDao.count(kwargs);
        return count;
    }

    @Override
    public List<CustomerDto> loadAll() {
        return load(new HashMap<>());
    }
}
