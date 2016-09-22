package com.tsystems.javaschool.business.dto;

import com.tsystems.javaschool.db.entities.Tariff;
import com.tsystems.javaschool.db.interfaces.OptionDao;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by alex on 22.09.16.
 */
public class TariffDto {
    private int id;

    @Size(min = 2, max = 45)
    @NotNull
    private String name;

    @Digits(integer = 8, fraction = 2)
    @NotNull
    private BigDecimal cost;

    private String description;
    private Set<OptionDto> possibleOptionsEntities;
    private Set<Integer> possibleOptions;

    public TariffDto(){
        possibleOptionsEntities = new HashSet<>();
        possibleOptions = new HashSet<>();
    }

    public TariffDto(Tariff tariff) {
        this.id = tariff.getId();
        this.name = tariff.getName();
        this.cost = tariff.getCost();
        this.description = tariff.getDescription();
        this.possibleOptionsEntities = new HashSet<>();
        possibleOptionsEntities = new HashSet<>();
        possibleOptions = new HashSet<>();
    }

    public void setDependencies(Tariff tariff) {
        possibleOptionsEntities = tariff.getPossibleOptions().stream().map(OptionDto::new).collect(Collectors.toSet());
    }

    public Tariff getTariffEntity() {
        Tariff tariff = new Tariff(id, name, cost, description);
        tariff.setPossibleOptions(possibleOptionsEntities.stream().map(OptionDto::getOptionEntity).collect(Collectors.toSet()));
        return tariff;
    }

    public void convertIdToEntities(OptionDao dao) {
        possibleOptionsEntities = possibleOptions.stream().map(dao::read).map(OptionDto::new).collect(Collectors.toSet());
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

    public Set<OptionDto> getPossibleOptionsEntities() {
        return possibleOptionsEntities;
    }

    public void setPossibleOptionsEntities(Set<OptionDto> possibleOptionsEntities) {
        this.possibleOptionsEntities = possibleOptionsEntities;
    }

    public Set<Integer> getPossibleOptions() {
        return possibleOptions;
    }

    public void setPossibleOptions(Set<Integer> possibleOptions) {
        this.possibleOptions = possibleOptions;
    }
}
