package com.tsystems.javaschool.db.entities;

import com.sun.xml.internal.ws.developer.UsesJAXBContext;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

/**
 * Created by alex on 21.08.16.
 */
@Entity
@Table(name = "Contracts", schema = "eCare")
public class Contract {

    // TODO add validation annotations

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;
    @Column(name = "number")
    private String number;
    @Column(name = "is_blocked")
    private Integer isBlocked;

    @ManyToOne
    @JoinColumn(name = "customer")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "tariff")
    private Tariff tariff;

    @ManyToMany(mappedBy = "contractsThoseUseOption")
    private Set<Option> usedOptions;

    public Contract() { }

    public Contract(Integer id, String number, Customer customer, Tariff tariff, Integer isBlocked) {
        this.id = id;
        this.number = number;
        this.isBlocked = isBlocked;
        this.customer = customer;
        this.tariff = tariff;
    }

    public int getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Tariff getTariff() {
        return tariff;
    }

    public void setTariff(Tariff tariff) {
        this.tariff = tariff;
    }

    public Set<Option> getUsedOptions() {
        return usedOptions;
    }

    public void setUsedOptions(Set<Option> usedOptions) {
        this.usedOptions = usedOptions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Contract that = (Contract) o;

        if (!Objects.equals(id, that.id))
            return false;
        if (number != null ? !number.equals(that.number) : that.number != null)
            return false;
        return isBlocked != null ? isBlocked.equals(that.isBlocked) : that.isBlocked == null;

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (number != null ? number.hashCode() : 0);
        result = 31 * result + (isBlocked != null ? isBlocked.hashCode() : 0);
        return result;
    }
}