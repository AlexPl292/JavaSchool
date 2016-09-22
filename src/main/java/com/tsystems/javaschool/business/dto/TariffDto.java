package com.tsystems.javaschool.business.dto;

import com.tsystems.javaschool.db.entities.Tariff;

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

    @Size(min = 3, max = 45)
    @NotNull
    private String name;

    @Digits(integer = 8, fraction = 2)
    @NotNull
    private BigDecimal cost;

    private String description;
    private Set<OptionDto> possibleOptions;
    private Set<Integer> possibleOptionsId;

    public TariffDto(){
        possibleOptions = new HashSet<>();
        possibleOptionsId = new HashSet<>();
    }

    public TariffDto(Tariff tariff) {
        this.id = tariff.getId();
        this.name = tariff.getName();
        this.cost = tariff.getCost();
        this.description = tariff.getDescription();
        this.possibleOptions = new HashSet<>();
        possibleOptions = new HashSet<>();
        possibleOptionsId = new HashSet<>();
    }

    public void setDependencies(Tariff tariff) {
        possibleOptions = tariff.getPossibleOptions().stream().map(OptionDto::new).collect(Collectors.toSet());
    }

    public Tariff getTariffEntity() {
        Tariff tariff = new Tariff(id, name, cost, description);
        tariff.setPossibleOptions(possibleOptions.stream().map(OptionDto::getOptionEntity).collect(Collectors.toSet()));
        return tariff;
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

    public Set<OptionDto> getPossibleOptions() {
        return possibleOptions;
    }

    public void setPossibleOptions(Set<OptionDto> possibleOptions) {
        this.possibleOptions = possibleOptions;
    }

    public Set<Integer> getPossibleOptionsId() {
        return possibleOptionsId;
    }

    public void setPossibleOptionsId(Set<Integer> possibleOptionsId) {
        this.possibleOptionsId = possibleOptionsId;
    }
}
