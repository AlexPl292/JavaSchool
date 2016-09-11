package com.tsystems.javaschool.db.entities;

import com.google.gson.annotations.Expose;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by alex on 17.08.16.
 *
 * Customer entity class.
 */

@Entity
@Table(name = "Customers")
public class Customer extends User{

    @Column(name = "date_of_birth")
    @Expose
    private Date dateOfBirth;

    @Column(name = "passport_number")
    @Expose
    private String passportNumber;

    @Column(name = "passport_data")
    @Expose
    private String passportData;

    @Column(name = "address")
    @Expose
    private String address;

    @Column(name = "is_blocked")
    @Expose
    private int isBlocked;

    @Column(name = "balance")
    @Expose
    private BigDecimal balance;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "customer")
    private Set<Contract> contracts;

    /**
     * Constructor with only important data
     * Used in Dao select queries
     * @param id id of new entity
     * @param name name of new entity
     * @param surname surname of new entity
     * @param email email of new entity
     * @param isBlocked is this customer blocked or not
     */
    public Customer(Integer id, String name, String surname, String email, int isBlocked, String passportNumber, BigDecimal balance) {
        this.setId(id);
        this.setName(name);
        this.setSurname(surname);
        this.setEmail(email);
        this.isBlocked = isBlocked;
        this.passportNumber = passportNumber;
        this.balance = balance;
    }


    /**
     * Empty constructor
     */
    public Customer() {
        contracts = new HashSet<>();
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dayOfBirth) {
        this.dateOfBirth = dayOfBirth;
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

    public int getIsBlocked() {
        return isBlocked;
    }

    public void setIsBlocked(int isBlocked) {
        this.isBlocked = isBlocked;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    public Set<Contract> getContracts() {
        return contracts;
    }

    public void setContracts(Set<Contract> contracts) {
        this.contracts = contracts;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", surname='" + getSurname() + '\'' +
                ", day_of_birth=" + dateOfBirth +
                ", passport_data='" + passportData + '\'' +
                ", passport_number='" + passportNumber + '\'' +
                ", address='" + address + '\'' +
                ", email='" + getEmail() + '\'' +
                ", is_blocked=" + isBlocked +
                '}';
    }
}
