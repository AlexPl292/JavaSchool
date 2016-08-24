package com.tsystems.javaschool.db.entities;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by alex on 17.08.16.
 */

@Entity
@Table(name = "Customers")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "date_of_birth")
    private Date dateOfBirth;

    @Column(name = "passport_data")
    private String passportData;

    @Column(name = "address")
    private String address;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "salt")
    private String salt;

    @Column(name = "is_blocked")
    private int isBlocked;

    public Customer(String name, String surname, Date dateOfBirth, String passportData, String address, String email, String password, String salt, int isBlocked) {
        this.name = name;
        this.surname = surname;
        this.dateOfBirth = dateOfBirth;
        this.passportData = passportData;
        this.address = address;
        this.email = email;
        this.password = password;
        this.salt = salt;
        this.isBlocked = isBlocked;
    }

    public Customer(String name, String surname, String email, int isBlocked) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.isBlocked = isBlocked;
    }

    public Customer() {}

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

    public void setDateOfBirth(Date day_of_birth) {
        this.dateOfBirth = day_of_birth;
    }

    public String getPassportData() {
        return passportData;
    }

    public void setPassportData(String passport_data) {
        this.passportData = passport_data;
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

    public void setIsBlocked(int is_blocked) {
        this.isBlocked = is_blocked;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", day_of_birth=" + dateOfBirth +
                ", passport_data='" + passportData + '\'' +
                ", address='" + address + '\'' +
                ", email='" + email + '\'' +
                ", password=" + password +
                ", is_blocked=" + isBlocked +
                '}';
    }
}
