package com.tsystems.javaschool.business.dto;

import com.tsystems.javaschool.db.entities.Contract;
import com.tsystems.javaschool.db.entities.Customer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
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
    private String password;
    private String salt;
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

    public Customer getCustomerEntity() {
        Customer customer = new Customer(id, name, surname, email, isBlocked, passportNumber);
        customer.setPassportData(passportData);
        customer.setDateOfBirth(dateOfBirth);
        customer.setPassword(password);
        customer.setSalt(salt);
        customer.setAddress(address);
        customer.setContracts(contracts.stream().map(ContractDto::getContractEntity).collect(Collectors.toSet()));
        return customer;
    }

    public void setDependencies(Customer customer) {
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
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
