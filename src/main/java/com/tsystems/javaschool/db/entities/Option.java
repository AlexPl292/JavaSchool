package com.tsystems.javaschool.db.entities;

import com.google.gson.annotations.Expose;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by alex on 25.08.16.
 *
 * Entity for access option table
 */
@Entity
@Table(name = "Options", schema = "eCare")
public class Option {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    @Expose
    private int id;

    @Basic
    @Column(name = "name", nullable = true, length = 45)
    @Expose
    private String name;

    @Basic
    @Column(name = "cost", nullable = true, precision = 2)
    @Expose
    private BigDecimal cost;

    @Basic
    @Column(name = "connect_cost", nullable = true, precision = 2)
    @Expose
    private BigDecimal connectCost;

    @Basic
    @Column(name = "description", nullable = true, length = 255)
    @Expose
    private String description;

    @JoinTable(name = "Required_option_relationships", joinColumns = {
        @JoinColumn(name = "id_first", referencedColumnName = "id")}, inverseJoinColumns = {
        @JoinColumn(name = "id_second", referencedColumnName = "id")
    })
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Option> required = new HashSet<>();

    @JoinTable(name = "Required_option_relationships", joinColumns = { // By using 'mappedby' there dependencies
            @JoinColumn(name = "id_second", referencedColumnName = "id")}, inverseJoinColumns = {  // will not persist
            @JoinColumn(name = "id_first", referencedColumnName = "id")
    })
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Option> requiredMe = new HashSet<>();

    @JoinTable(name = "Forbidden_option_relationships", joinColumns = {
        @JoinColumn(name = "id_first", referencedColumnName = "id", nullable = false)}, inverseJoinColumns = {
        @JoinColumn(name = "id_second", referencedColumnName = "id", nullable = false)
    })
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Option> forbidden = new HashSet<>();

    @JoinTable(name = "Possible_options_of_tariffs", joinColumns = {
            @JoinColumn(name = "option_id", referencedColumnName = "id", nullable = false)}, inverseJoinColumns = {
            @JoinColumn(name = "tariff_id", referencedColumnName = "id", nullable = false)
    })
    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    private Set<Tariff> possibleTariffsOfOption = new HashSet<>();

    @JoinTable(name = "Used_options_of_tariff", joinColumns = {
            @JoinColumn(name = "option_id", referencedColumnName = "id", nullable = false)}, inverseJoinColumns = {
            @JoinColumn(name = "contract_id", referencedColumnName = "id", nullable = false)
    })
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Contract> contractsThoseUseOption = new HashSet<>();

    /**
     * Constructor with only important data
     * Used in Dao select queries
     * @param id id of new entity
     * @param name name of new entity
     * @param cost cost of new entity
     * @param connectCost connection cost of new eity
     * @param description description of new entity
     */
    public Option(Integer id, String name, BigDecimal cost, BigDecimal connectCost, String description) {
        this.id = id;
        this.name = name;
        this.cost = cost;
        this.connectCost = connectCost;
        this.description = description;
    }

    /**
     * Default constructor
     */
    public Option() {
        required = new HashSet<>();
        forbidden = new HashSet<>();
        possibleTariffsOfOption = new HashSet<>();
        contractsThoseUseOption = new HashSet<>();
    }

    /**
     * Add option, this option required from
     * @param option required option
     */
    public void addRequiredFromOptions(Option option) {
        this.getRequired().add(option);
    }

    /**
     * Add option, that requires this
     * @param option option, that requires this
     */
    public void addRequiredMeOptions(Option option) {
        this.getRequiredMe().add(option);
    }

    /**
     * Add incompatible dependency with this option
     * This dependency works in both sides
     * @param option incompatible option
     */
    public void addForbiddenWithOptions(Option option) {
        this.getForbidden().add(option);
        option.getForbidden().add(this);
    }

    /**
     * Add set of options, this option required from
     * @param options required options
     */
    public void addRequiredFromOptions(Set<Option> options) {
        this.getRequired().addAll(options);
    }

    /**
     * Add options, those requires this
     * @param options options, those requires this
     */
    public void addRequiredMeOptions(Set<Option> options) {
        this.getRequiredMe().addAll(options);
    }

    /**
     * Add incompatible dependencies with this option
     * This dependencies work in both sides
     * @param options incompatible options
     */
    public void addForbiddenWithOptions(Set<Option> options) {
        this.getForbidden().addAll(options);
        for (Option opt : options)
           opt.getForbidden().add(this);
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

    public Set<Option> getRequiredMe() {
        return requiredMe;
    }

    public void setRequiredMe(Set<Option> requiredMe) {
        this.requiredMe = requiredMe;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Option that = (Option) o;

        return id == that.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return "Option{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", cost=" + cost +
                ", connectCost=" + connectCost +
                ", description='" + description + '\'' +
                '}';
    }
}
