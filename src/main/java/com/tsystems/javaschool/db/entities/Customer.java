package com.tsystems.javaschool.db.entities;

import com.google.gson.annotations.Expose;

import javax.persistence.*;
import java.util.*;

/**
 * Created by alex on 17.08.16.
 *
 * Customer entity class.
 */

@Entity
@Table(name = "Customers")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    @Expose
    private Integer id;

    @Column(name = "name")
    @Expose
    private String name;

    @Column(name = "surname")
    @Expose
    private String surname;

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

    @Column(name = "email")
    @Expose
    private String email;

    @Column(name = "password")
    @Expose
    private String password;

    @Column(name = "salt")
    @Expose
    private String salt;

    @Column(name = "is_blocked")
    @Expose
    private int isBlocked;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "customer")
    private List<Contract> contracts;

    /**
     * Constructor with only important data
     * Used in Dao select queries
     * @param id id of new entity
     * @param name name of new entity
     * @param surname surname of new entity
     * @param email email of new entity
     * @param isBlocked is this customer blocked or not
     */
    public Customer(Integer id, String name, String surname, String email, int isBlocked, String passportNumber) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.isBlocked = isBlocked;
        this.passportNumber = passportNumber;
    }


    /**
     * Empty constructor
     */
    public Customer() {
        contracts = new ArrayList<>();
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

    public int getIsBlocked() {
        return isBlocked;
    }

    public void setIsBlocked(int isBlocked) {
        this.isBlocked = isBlocked;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    public List<Contract> getContracts() {
        return contracts;
    }

    public void setContracts(List<Contract> contracts) {
        this.contracts = contracts;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", day_of_birth=" + dateOfBirth +
                ", passport_data='" + passportData + '\'' +
                ", passport_number='" + passportNumber + '\'' +
                ", address='" + address + '\'' +
                ", email='" + email + '\'' +
                ", is_blocked=" + isBlocked +
                '}';
    }
}
