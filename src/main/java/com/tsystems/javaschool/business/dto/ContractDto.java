package com.tsystems.javaschool.business.dto;

import com.tsystems.javaschool.db.entities.Contract;
import com.tsystems.javaschool.db.entities.Option;
import com.tsystems.javaschool.db.interfaces.CustomerDao;
import com.tsystems.javaschool.db.interfaces.OptionDao;
import com.tsystems.javaschool.db.interfaces.TariffDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by alex on 08.09.16.
 */
public class ContractDto {
    private Integer id;
    private String number;
    private Integer isBlocked;
    private CustomerDto customer;
    private TariffDto tariff;
    private BigDecimal balance;
    private Set<OptionDto> usedOptions = new HashSet<>();

    public ContractDto() {

    }

    public ContractDto(Contract contract) {
        this.id = contract.getId();
        this.number = contract.getNumber();
        this.isBlocked = contract.getIsBlocked();
        this.balance = contract.getBalance();
        this.customer = new CustomerDto(contract.getCustomer());
        this.tariff = new TariffDto(contract.getTariff());
    }

    public Contract getContractEntity(TariffDao tariffDao, OptionDao optionDao, CustomerDao customerDao) {
        Contract contract = new Contract(id, number, customerDao.read(customer.getId()), tariffDao.read(tariff.getId()), isBlocked, balance);
        contract.setUsedOptions(usedOptions.stream().map(e -> optionDao.read(e.getId())).collect(Collectors.toSet()));
        return contract;
    }

    public Contract getContractEntity() {
        Contract contract = new Contract(id, number, customer.getCustomerEntity(), tariff.getTariffEntity(), isBlocked, balance);
        contract.setUsedOptions(usedOptions.stream().map(OptionDto::getOptionEntity).collect(Collectors.toSet()));
        return contract;
    }

    public void setDependencies(Contract contract) {
        this.usedOptions = contract.getUsedOptions().stream().map(OptionDto::new).collect(Collectors.toSet());
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Integer getIsBlocked() {
        return isBlocked;
    }

    public void setIsBlocked(Integer isBlocked) {
        this.isBlocked = isBlocked;
    }

    public CustomerDto getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerDto customer) {
        this.customer = customer;
    }

    public TariffDto getTariff() {
        return tariff;
    }

    public void setTariff(TariffDto tariff) {
        this.tariff = tariff;
    }

    public Set<OptionDto> getUsedOptions() {
        return usedOptions;
    }

    public void setUsedOptions(Set<OptionDto> usedOptions) {
        this.usedOptions = usedOptions;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
