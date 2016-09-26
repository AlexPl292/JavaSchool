package com.tsystems.javaschool.business.dto;

import com.tsystems.javaschool.db.entities.Customer;
import com.tsystems.javaschool.db.interfaces.ContractDao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by alex on 08.09.16.
 */
public class CustomerDto {

    private Integer id;
    private String name;
    private String surname;
    private Date dateOfBirth;
    private String passportNumber;
    private String passportData;
    private String address;
    private String email;
    private int isBlocked;
    private List<ContractDto> contracts = new ArrayList<>();

    public CustomerDto() {

    }

    public CustomerDto(Customer customer) {
        this.id = customer.getId();
        this.name = customer.getName();
        this.surname = customer.getSurname();
        this.dateOfBirth = customer.getDateOfBirth();
        this.passportNumber = customer.getPassportNumber();
        this.passportData = customer.getPassportData();
        this.address = customer.getAddress();
        this.email = customer.getEmail();
        this.isBlocked = customer.getIsBlocked();
    }

    public Customer convertCustomerEntity() {
        Customer customer = new Customer();
        customer.setId(id);
        customer.setName(name);
        customer.setSurname(surname);
        customer.setEmail(email);
        customer.setIsBlocked(isBlocked);
        customer.setPassportNumber(passportNumber);
        customer.setPassportData(passportData);
        customer.setDateOfBirth(dateOfBirth);
        customer.setAddress(address);
        if (contracts != null)
            customer.setContracts(contracts.stream().map(ContractDto::convertContractEntity).collect(Collectors.toSet()));
        return customer;
    }

    public Customer convertCustomerEntity(ContractDao contractDao) {
        Customer customer = new Customer();
        customer.setId(id);
        customer.setName(name);
        customer.setSurname(surname);
        customer.setEmail(email);
        customer.setIsBlocked(isBlocked);
        customer.setPassportNumber(passportNumber);
        customer.setPassportData(passportData);
        customer.setDateOfBirth(dateOfBirth);
        customer.setAddress(address);
        if (contracts != null && contractDao != null)
            customer.setContracts(contracts.stream().map(e -> contractDao.read(e.getId())).collect(Collectors.toSet()));
        return customer;
    }

    public void setDependencies(Customer customer) {
        if (customer != null && customer.getContracts() != null)
            this.contracts = customer.getContracts().stream().map(ContractDto::new).collect(Collectors.toList());
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    public String getPassportData() {
        return passportData;
    }

    public void setPassportData(String passportData) {
        this.passportData = passportData;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getIsBlocked() {
        return isBlocked;
    }

    public void setIsBlocked(int isBlocked) {
        this.isBlocked = isBlocked;
    }

    public List<ContractDto> getContracts() {
        return contracts;
    }

    public void setContracts(List<ContractDto> contracts) {
        this.contracts = contracts;
    }
}
