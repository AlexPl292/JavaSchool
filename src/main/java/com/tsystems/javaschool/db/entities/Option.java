package com.tsystems.javaschool.db.entities;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Set;
import java.util.HashSet;

/**
 * Created by alex on 25.08.16.
 */
@Entity
@Table(name = "Options", schema = "eCare")
public class Option {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private int id;

    @Basic
    @Column(name = "name", nullable = true, length = 45)
    private String name;

    @Basic
    @Column(name = "cost", nullable = true, precision = 2)
    private BigDecimal cost;

    @Basic
    @Column(name = "connect_cost", nullable = true, precision = 2)
    private BigDecimal connectCost;

    @Basic
    @Column(name = "description", nullable = true, length = 255)
    private String description;

    @JoinTable(name = "Required_option_relationships", joinColumns = {
        @JoinColumn(name = "id_first", referencedColumnName = "id")}, inverseJoinColumns = {
        @JoinColumn(name = "id_second", referencedColumnName = "id")
    })
    @ManyToMany(cascade = CascadeType.ALL)
    private Set<Option> required;

    @JoinTable(name = "Forbidden_option_relationships", joinColumns = {
        @JoinColumn(name = "id_first", referencedColumnName = "id", nullable = false)}, inverseJoinColumns = {
        @JoinColumn(name = "id_second", referencedColumnName = "id", nullable = false)
    })
    @ManyToMany(cascade = CascadeType.ALL)
    private Set<Option> forbidden;

    @JoinTable(name = "Possible_options_of_tariffs", joinColumns = {
            @JoinColumn(name = "option_id", referencedColumnName = "id", nullable = false)}, inverseJoinColumns = {
            @JoinColumn(name = "tariff_id", referencedColumnName = "id", nullable = false)
    })
    @ManyToMany(cascade = CascadeType.ALL)
    private Set<Tariff> possibleTariffsOfOption;

    @JoinTable(name = "Used_options_of_tariff", joinColumns = {
            @JoinColumn(name = "option_id", referencedColumnName = "id", nullable = false)}, inverseJoinColumns = {
            @JoinColumn(name = "contract_id", referencedColumnName = "id", nullable = false)
    })
    @ManyToMany(cascade = CascadeType.ALL)
    private Set<Contract> contractsThoseUseOption;

    public Option() {
        required = new HashSet<>();
        forbidden = new HashSet<>();
        possibleTariffsOfOption = new HashSet<>();
        contractsThoseUseOption = new HashSet<>();
    }

    public void addToRequiredOptions(Option option) {
        this.getRequired().add(option);
        option.getRequired().add(this);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public BigDecimal getConnectCost() {
        return connectCost;
    }

    public void setConnectCost(BigDecimal connectCost) {
        this.connectCost = connectCost;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Option> getRequired() {
        return required;
    }

    public void setRequired(Set<Option> required) {
        this.required = required;
    }

    public Set<Option> getForbidden() {
        return forbidden;
    }

    public void setForbidden(Set<Option> forbidden) {
        this.forbidden = forbidden;
    }

    public Set<Tariff> getPossibleTariffsOfOption() {
        return possibleTariffsOfOption;
    }

    public void setPossibleTariffsOfOption(Set<Tariff> possibleTariffsOfOption) {
        this.possibleTariffsOfOption = possibleTariffsOfOption;
    }

    public Set<Contract> getContractsThoseUseOption() {
        return contractsThoseUseOption;
    }

    public void setContractsThoseUseOption(Set<Contract> contractsThoseUseOption) {
        this.contractsThoseUseOption = contractsThoseUseOption;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Option that = (Option) o;

        if (id != that.id)
            return false;
        if (name != null ? !name.equals(that.name) : that.name != null)
            return false;
        if (cost != null ? !cost.equals(that.cost) : that.cost != null)
            return false;
        if (connectCost != null ? !connectCost.equals(that.connectCost) : that.connectCost != null)
            return false;
        if (description != null ? !description.equals(that.description) : that.description != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (cost != null ? cost.hashCode() : 0);
        result = 31 * result + (connectCost != null ? connectCost.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }
}
