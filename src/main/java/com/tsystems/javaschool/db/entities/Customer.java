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
    private long id;

    private String name;

    private String surname;

    private Date date_of_birth;

    private String passport_data;

    private String address;

    private String email;

    private int password;

    private int is_blocked;

    public Customer(String name, String surname, Date date_of_birth, String passport_data, String address, String email, int password, int is_blocked) {
        this.name = name;
        this.surname = surname;
        this.date_of_birth = date_of_birth;
        this.passport_data = passport_data;
        this.address = address;
        this.email = email;
        this.password = password;
        this.is_blocked = is_blocked;
    }

    public Customer() {}

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public Date getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(Date day_of_birth) {
        this.date_of_birth = day_of_birth;
    }

    public String getPassport_data() {
        return passport_data;
    }

    public void setPassport_data(String passport_data) {
        this.passport_data = passport_data;
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

    public int getPassword() {
        return password;
    }

    public void setPassword(int password) {
        this.password = password;
    }

    public int getIs_blocked() {
        return is_blocked;
    }

    public void setIs_blocked(int is_blocked) {
        this.is_blocked = is_blocked;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", day_of_birth=" + date_of_birth +
                ", passport_data='" + passport_data + '\'' +
                ", address='" + address + '\'' +
                ", email='" + email + '\'' +
                ", password=" + password +
                ", is_blocked=" + is_blocked +
                '}';
    }
}
