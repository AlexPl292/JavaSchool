package com.tsystems.javaschool.db.entities;

import com.google.gson.annotations.Expose;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by alex on 17.08.16.
 *
 * Entity for access customer table
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
    private Integer isBlocked;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "customer", cascade = CascadeType.ALL)
    private Set<Contract> contracts;

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

    public Integer getIsBlocked() {
        return isBlocked;
    }

    public void setIsBlocked(Integer isBlocked) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        if (!super.equals(o))
            return false;

        Customer customer = (Customer) o;

        if (isBlocked != customer.isBlocked)
            return false;
        if (dateOfBirth != null ? !dateOfBirth.equals(customer.dateOfBirth) : customer.dateOfBirth != null)
            return false;
        if (passportNumber != null ? !passportNumber.equals(customer.passportNumber) : customer.passportNumber != null)
            return false;
        if (passportData != null ? !passportData.equals(customer.passportData) : customer.passportData != null)
            return false;
        if (address != null ? !address.equals(customer.address) : customer.address != null)
            return false;
        return contracts != null ? contracts.equals(customer.contracts) : customer.contracts == null;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (dateOfBirth != null ? dateOfBirth.hashCode() : 0);
        result = 31 * result + (passportNumber != null ? passportNumber.hashCode() : 0);
        result = 31 * result + (passportData != null ? passportData.hashCode() : 0);
        result = 31 * result + (address != null ? address.hashCode() : 0);
        result = 31 * result + isBlocked;
        result = 31 * result + (contracts != null ? contracts.hashCode() : 0);
        return result;
    }
}
