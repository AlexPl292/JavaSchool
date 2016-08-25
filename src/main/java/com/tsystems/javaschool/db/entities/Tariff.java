package com.tsystems.javaschool.db.entities;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Set;

/**
 * Created by alex on 21.08.16.
 */
@Entity
@Table(name = "Tariffs", schema = "eCare")
public class Tariff {

    // TODO add validation annotations

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Basic
    @Column(name = "name", nullable = true, length = 45)
    private String name;

    @Basic
    @Column(name = "cost", nullable = true, precision = 2)
    private BigDecimal cost;

    @Basic
    @Column(name = "description", nullable = true, length = 255)
    private String description;

    @ManyToMany(mappedBy = "possibleTariffsOfOption")
    private Set<Option> possibleOptions;


    public Tariff() {
    }

    public Tariff(Integer id, String name, BigDecimal cost, String description) {
        this.id = id;
        this.name = name;
        this.cost = cost;
        this.description = description;
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

        if (id != that.id)
            return false;
        if (name != null ? !name.equals(that.name) : that.name != null)
            return false;
        if (cost != null ? !cost.equals(that.cost) : that.cost != null)
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
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }
}
