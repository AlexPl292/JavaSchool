package com.tsystems.javaschool.business.dto;

import com.tsystems.javaschool.db.entities.Option;
import com.tsystems.javaschool.db.interfaces.ContractDao;
import com.tsystems.javaschool.db.interfaces.OptionDao;
import com.tsystems.javaschool.db.interfaces.TariffDao;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by alex on 08.09.16.
 */
public class OptionDto {

    private Integer id;

    @NotNull
    @Size(min = 2, max = 45)
    private String name;

    @Digits(integer = 8, fraction = 2)
    @NotNull
    private BigDecimal cost;

    @Digits(integer = 8, fraction = 2)
    @NotNull
    private BigDecimal connectCost;

    private String description;
    private Set<OptionDto> requiredFrom = new HashSet<>();
    private Set<OptionDto> requiredMe = new HashSet<>();
    private Set<OptionDto> forbiddenWith = new HashSet<>();
    private Set<TariffDto> possibleTariffsOfOption = new HashSet<>();

    public OptionDto() {

    }

    public OptionDto(Option option) {
        this.id = option.getId();
        this.name = option.getName();
        this.cost = option.getCost();
        this.connectCost = option.getConnectCost();
        this.description = option.getDescription();
    }

    public void setDependencies(Option option) {
        if (option != null) {
            if (option.getRequired() != null)
                requiredFrom = option.getRequired().stream().map(OptionDto::new).collect(Collectors.toSet());
            if (option.getRequiredMe() != null)
                requiredMe = option.getRequiredMe().stream().map(OptionDto::new).collect(Collectors.toSet());
            if (option.getForbidden() != null)
                forbiddenWith = option.getForbidden().stream().map(OptionDto::new).collect(Collectors.toSet());
            if (option.getPossibleTariffsOfOption() != null)
                possibleTariffsOfOption = option.getPossibleTariffsOfOption().stream().map(TariffDto::new).collect(Collectors.toSet());
        }
    }


    public Option convertToOptionEntity(OptionDao optionDao, TariffDao tariffDao) {
        Option option = new Option(id, name, cost, connectCost, description);

        if (optionDao != null) {
            if (requiredFrom != null)
                option.setRequired(requiredFrom.stream().map(e -> optionDao.read(e.getId())).collect(Collectors.toSet()));
            if (requiredMe != null)
                option.setRequiredMe(requiredMe.stream().map(e -> optionDao.read(e.getId())).collect(Collectors.toSet()));
            if (forbiddenWith != null)
                option.setForbidden(forbiddenWith.stream().map(e -> optionDao.read(e.getId())).collect(Collectors.toSet()));
        }
        if (tariffDao != null) {
            if (possibleTariffsOfOption != null)
                option.setPossibleTariffsOfOption(possibleTariffsOfOption.stream().map(e -> tariffDao.read(e.getId())).collect(Collectors.toSet()));
        }
        return option;
    }

    public Option convertToOptionEntity() {
        Option option = new Option(id, name, cost, connectCost, description);
        if (requiredFrom != null)
            option.setRequired(requiredFrom.stream().map(OptionDto::convertToOptionEntity).collect(Collectors.toSet()));
        if (requiredMe != null)
            option.setRequiredMe(requiredMe.stream().map(OptionDto::convertToOptionEntity).collect(Collectors.toSet()));
        if (forbiddenWith != null)
            option.setForbidden(forbiddenWith.stream().map(OptionDto::convertToOptionEntity).collect(Collectors.toSet()));
        if (possibleTariffsOfOption != null)
            option.setPossibleTariffsOfOption(possibleTariffsOfOption.stream().map(TariffDto::convertTariffEntity).collect(Collectors.toSet()));
        return option;
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

    public Set<OptionDto> getRequiredFrom() {
        return requiredFrom;
    }

    public void setRequiredFrom(Set<OptionDto> requiredFrom) {
        this.requiredFrom = requiredFrom;
    }

    public Set<OptionDto> getRequiredMe() {
        return requiredMe;
    }

    public void setRequiredMe(Set<OptionDto> requiredMe) {
        this.requiredMe = requiredMe;
    }

    public Set<OptionDto> getForbiddenWith() {
        return forbiddenWith;
    }

    public void setForbiddenWith(Set<OptionDto> forbiddenWith) {
        this.forbiddenWith = forbiddenWith;
    }

    public Set<TariffDto> getPossibleTariffsOfOption() {
        return possibleTariffsOfOption;
    }

    public void setPossibleTariffsOfOption(Set<TariffDto> possibleTariffsOfOption) {
        this.possibleTariffsOfOption = possibleTariffsOfOption;
    }
}
