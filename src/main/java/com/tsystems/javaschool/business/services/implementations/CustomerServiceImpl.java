package com.tsystems.javaschool.business.services.implementations;

import com.tsystems.javaschool.business.dto.ContractDto;
import com.tsystems.javaschool.business.dto.CustomerDto;
import com.tsystems.javaschool.business.services.interfaces.CustomerService;
import com.tsystems.javaschool.db.entities.Contract;
import com.tsystems.javaschool.db.entities.Customer;
import com.tsystems.javaschool.db.repository.CustomerRepository;
import com.tsystems.javaschool.exceptions.UniqueFieldDuplicateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by alex on 17.08.16.
 */
@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository repository;

    @Autowired
    public CustomerServiceImpl(CustomerRepository repository) {
        this.repository = repository;
    }

    @Override
    public CustomerDto addNew(CustomerDto customerDto) throws UniqueFieldDuplicateException {
        List<Customer> existings = repository.findByPassportNumberOrEmail(customerDto.getPassportNumber(), customerDto.getEmail());
        if (existings != null && existings.size() > 0) {
            if (existings.get(0).getEmail().equalsIgnoreCase(customerDto.getEmail()))
                throw new UniqueFieldDuplicateException("Email", customerDto.getEmail(), "/rest/options/" + existings.get(0).getId());
            else
                throw new UniqueFieldDuplicateException("PassportNumber", customerDto.getPassportNumber(), "/rest/options/" + existings.get(0).getId());
        }

        if (customerDto.getContracts() != null) {
            for (ContractDto contract : customerDto.getContracts()) {
                Customer existingsContracts = repository.findByContracts_Number(contract.getNumber());
                if (existingsContracts != null) {
                    throw new UniqueFieldDuplicateException("Number", contract.getNumber(), "/rest/customers/" + existingsContracts.getId());
                }
            }
        }
        /*
        Пароль НЕ должен быть введен сотрудником.
        Будем его вручную генерировать, а потом посылать с помощью email или sms
         */
        Customer customer = customerDto.convertToEntity();
        Contract contract = customerDto.getContracts().first().convertToEntity();
        contract.setCustomer(customer);
        contract.setBalance(new BigDecimal("100.00"));
        contract.setIsBlocked(0);

        Set<Contract> contracts = new HashSet<>();
        contracts.add(contract);
        customer.setContracts(contracts);

        customer.setPassword("no pass");

        return new CustomerDto(repository.saveAndFlush(customer));
    }

    @Override
    public CustomerDto loadByKey(Integer key) {
        Customer customer = repository.findOne(key);
        return new CustomerDto(customer).addDependencies(customer);
    }

    @Override
    public void remove(Integer key) {
        repository.delete(key);
    }

    @Override
    public List<CustomerDto> loadAll() {
        return repository
                .findAll()
                .stream().map(e -> new CustomerDto(e).addDependencies(e))
                .collect(Collectors.toList());
    }

    @Override
    public List<CustomerDto> findByPassportNumberOrEmail(String passportNumber, String email) {
        return repository
                .findByPassportNumberOrEmail(passportNumber, email)
                .stream()
                .map(CustomerDto::new)
                .collect(Collectors.toList());
    }
}
