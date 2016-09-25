package com.tsystems.javaschool.db.entities;

import com.google.gson.annotations.Expose;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.Set;

/**
 * Created by alex on 21.08.16.
 *
 * Entity for access contract table
 *
 * Block levels:
 * 0 - unblocked
 * 1 - blocked by customer
 * 2 - blocked by staff
 */
@Entity
@Table(name = "Contracts", schema = "eCare")
public class Contract {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Expose
    private Integer id;
    @Column(name = "number")
    @Expose
    private String number;
    @Column(name = "is_blocked")
    @Expose
    private Integer isBlocked;

    @Column(name = "balance")
    @Expose
    private BigDecimal balance;

    @ManyToOne
    @JoinColumn(name = "customer")
    @Expose
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "tariff")
    @Expose
    private Tariff tariff;

    @JoinTable(name = "Used_options_of_tariff", joinColumns = {
            @JoinColumn(name = "contract_id", referencedColumnName = "id", nullable = false)}, inverseJoinColumns = {
            @JoinColumn(name = "option_id", referencedColumnName = "id", nullable = false)
    })
    @ManyToMany(fetch = FetchType.LAZY)
    private Set<Option> usedOptions;

    public Contract() {
        //Empty constructor
    }

    /**
     * Constructor with only important data
     * Used in Dao select queries
     * @param id id of new entity
     * @param number number of new entity
     * @param customer customer of new entity
     * @param tariff tariff of new entity
     * @param isBlocked if this contract blocked or not
     * @param balance balance of new contract
     */
    public Contract(Integer id, String number, Customer customer, Tariff tariff, Integer isBlocked, BigDecimal balance) {
        this.id = id;
        this.number = number;
        this.isBlocked = isBlocked;
        this.customer = customer;
        this.tariff = tariff;
        this.balance = balance;
    }

    public Integer getId() {
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

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Contract contract = (Contract) o;

        if (id != null ? !id.equals(contract.id) : contract.id != null) return false;
        if (number != null ? !number.equals(contract.number) : contract.number != null) return false;
        if (isBlocked != null ? !isBlocked.equals(contract.isBlocked) : contract.isBlocked != null) return false;
        if (balance != null ? !balance.equals(contract.balance) : contract.balance != null) return false;
        return true;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (number != null ? number.hashCode() : 0);
        result = 31 * result + (isBlocked != null ? isBlocked.hashCode() : 0);
        result = 31 * result + (balance != null ? balance.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Contract{" +
                "id=" + id +
                ", number='" + number + '\'' +
                ", isBlocked=" + isBlocked +
                ", balance=" + balance +
                ", customer=" + customer +
                ", tariff=" + tariff +
                ", usedOptions=" + usedOptions +
                '}';
    }
}
