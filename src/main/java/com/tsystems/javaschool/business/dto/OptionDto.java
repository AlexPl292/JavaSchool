package com.tsystems.javaschool.business.dto;

import com.tsystems.javaschool.db.entities.Contract;
import com.tsystems.javaschool.db.entities.Option;
import com.tsystems.javaschool.db.entities.Tariff;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by alex on 08.09.16.
 */
public class OptionDto {

    private int id;
    private String name;
    private BigDecimal cost;
    private BigDecimal connectCost;
    private String description;
    private Set<OptionDto> required = new HashSet<>();
    private Set<OptionDto> requiredMe = new HashSet<>();
    private Set<OptionDto> forbidden = new HashSet<>();
    private Set<TariffDto> possibleTariffsOfOption = new HashSet<>();
    private Set<ContractDto> contractsThoseUseOption = new HashSet<>();

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
        required = option.getRequired().stream().map(OptionDto::new).collect(Collectors.toSet());
        requiredMe = option.getRequiredMe().stream().map(OptionDto::new).collect(Collectors.toSet());
        forbidden = option.getForbidden().stream().map(OptionDto::new).collect(Collectors.toSet());
        possibleTariffsOfOption = option.getPossibleTariffsOfOption().stream().map(TariffDto::new).collect(Collectors.toSet());
        contractsThoseUseOption = option.getContractsThoseUseOption().stream().map(ContractDto::new).collect(Collectors.toSet());
    }


    public Option getOptionEntity() {
        Option option = new Option(id, name, cost, connectCost, description);
        option.setRequired(required.stream().map(OptionDto::getOptionEntity).collect(Collectors.toSet()));
        option.setRequiredMe(requiredMe.stream().map(OptionDto::getOptionEntity).collect(Collectors.toSet()));
        option.setForbidden(forbidden.stream().map(OptionDto::getOptionEntity).collect(Collectors.toSet()));
        option.setPossibleTariffsOfOption(possibleTariffsOfOption.stream().map(TariffDto::getTariffEntityNoConvert).collect(Collectors.toSet()));
        option.setContractsThoseUseOption(contractsThoseUseOption.stream().map(ContractDto::getContractEntity).collect(Collectors.toSet()));
        return option;
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

    public Set<OptionDto> getRequired() {
        return required;
    }

    public void setRequired(Set<OptionDto> required) {
        this.required = required;
    }

    public Set<OptionDto> getRequiredMe() {
        return requiredMe;
    }

    public void setRequiredMe(Set<OptionDto> requiredMe) {
        this.requiredMe = requiredMe;
    }

    public Set<OptionDto> getForbidden() {
        return forbidden;
    }

    public void setForbidden(Set<OptionDto> forbidden) {
        this.forbidden = forbidden;
    }

    public Set<TariffDto> getPossibleTariffsOfOption() {
        return possibleTariffsOfOption;
    }

    public void setPossibleTariffsOfOption(Set<TariffDto> possibleTariffsOfOption) {
        this.possibleTariffsOfOption = possibleTariffsOfOption;
    }

    public Set<ContractDto> getContractsThoseUseOption() {
        return contractsThoseUseOption;
    }

    public void setContractsThoseUseOption(Set<ContractDto> contractsThoseUseOption) {
        this.contractsThoseUseOption = contractsThoseUseOption;
    }
}
