package com.tsystems.javaschool.db.entities;

import com.google.gson.annotations.Expose;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.Set;

/**
 * Created by alex on 21.08.16.
 *
 * Entity for tariff table access
 */
@Entity
@Table(name = "Tariffs", schema = "eCare")
public class Tariff {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Expose
    private Integer id;

    @Basic
    @Column(name = "name", nullable = true, length = 45)
    @Expose
    private String name;

    @Basic
    @Column(name = "cost", nullable = true, precision = 2)
    @Expose
    private BigDecimal cost;

    @Basic
    @Column(name = "description", nullable = true, length = 255)
    @Expose
    private String description;

    @JoinTable(name = "Possible_options_of_tariffs", joinColumns = {
            @JoinColumn(name = "tariff_id", referencedColumnName = "id", nullable = false)}, inverseJoinColumns = {
            @JoinColumn(name = "option_id", referencedColumnName = "id", nullable = false)
    })
    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    private Set<Option> possibleOptions;


    public Tariff() {
        // Empty constructor
    }

    /**
     * Constructor with only important data
     * Used in Dao select queries
     * @param id id of new entity
     * @param name name of new entity
     * @param cost cost of new entity
     * @param description description of new entity
     */
    public Tariff(Integer id, String name, BigDecimal cost, String description) {
        this.id = id;
        this.name = name;
        this.cost = cost;
        this.description = description;
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

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Option> getPossibleOptions() {
        return possibleOptions;
    }

    public void setPossibleOptions(Set<Option> possibleOptions) {
        this.possibleOptions = possibleOptions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Tariff that = (Tariff) o;

        if (!Objects.equals(id, that.id))
            return false;
        if (name != null ? !name.equals(that.name) : that.name != null)
            return false;
        if (cost != null ? !cost.equals(that.cost) : that.cost != null)
            return false;
        return description != null ? description.equals(that.description) : that.description == null;

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (cost != null ? cost.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }
}
