package com.tsystems.javaschool.business.dto;

import com.tsystems.javaschool.db.entities.Option;
import com.tsystems.javaschool.db.interfaces.ContractDao;
import com.tsystems.javaschool.db.interfaces.OptionDao;
import com.tsystems.javaschool.db.interfaces.TariffDao;

import javax.validation.constraints.NotNull;
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
    private String name;

    private BigDecimal cost;
    private BigDecimal connectCost;
    private String description;
    private Set<OptionDto> requiredFrom = new HashSet<>();
    private Set<OptionDto> requiredMe = new HashSet<>();
    private Set<OptionDto> forbiddenWith = new HashSet<>();
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
        requiredFrom = option.getRequired().stream().map(OptionDto::new).collect(Collectors.toSet());
        requiredMe = option.getRequiredMe().stream().map(OptionDto::new).collect(Collectors.toSet());
        forbiddenWith = option.getForbidden().stream().map(OptionDto::new).collect(Collectors.toSet());
        possibleTariffsOfOption = option.getPossibleTariffsOfOption().stream().map(TariffDto::new).collect(Collectors.toSet());
        contractsThoseUseOption = option.getContractsThoseUseOption().stream().map(ContractDto::new).collect(Collectors.toSet());
    }


    public Option getOptionEntity(OptionDao optionDao, ContractDao contractDao, TariffDao tariffDao) {
        Option option = new Option(id, name, cost, connectCost, description);
        option.setRequired(requiredFrom.stream().map(OptionDto::getId).filter(Objects::nonNull).map(optionDao::read).collect(Collectors.toSet()));
        option.setRequiredMe(requiredMe.stream().map(e -> optionDao.read(e.getId())).collect(Collectors.toSet()));
        option.setForbidden(forbiddenWith.stream().map(e -> optionDao.read(e.getId())).collect(Collectors.toSet()));
        option.setPossibleTariffsOfOption(possibleTariffsOfOption.stream().map(e -> tariffDao.read(e.getId())).collect(Collectors.toSet()));
        option.setContractsThoseUseOption(contractsThoseUseOption.stream().map(e -> contractDao.read(e.getId())).collect(Collectors.toSet()));
        return option;
    }

    public Option getOptionEntity() {
        Option option = new Option(id, name, cost, connectCost, description);
        option.setRequired(requiredFrom.stream().map(OptionDto::getOptionEntity).collect(Collectors.toSet()));
        option.setRequiredMe(requiredMe.stream().map(OptionDto::getOptionEntity).collect(Collectors.toSet()));
        option.setForbidden(forbiddenWith.stream().map(OptionDto::getOptionEntity).collect(Collectors.toSet()));
        option.setPossibleTariffsOfOption(possibleTariffsOfOption.stream().map(TariffDto::getTariffEntity).collect(Collectors.toSet()));
        option.setContractsThoseUseOption(contractsThoseUseOption.stream().map(ContractDto::getContractEntity).collect(Collectors.toSet()));
        return option;
    }

/*    public Option getOptionEntityNoConvert() {
        Option option = new Option(id, name, cost, connectCost, description);
        option.setRequired(requiredFrom.stream().map(OptionDto::getOptionEntityNoConvert).collect(Collectors.toSet()));
        option.setRequiredMe(requiredMe.stream().map(OptionDto::getOptionEntityNoConvert).collect(Collectors.toSet()));
        option.setForbidden(forbiddenWith.stream().map(OptionDto::getOptionEntityNoConvert).collect(Collectors.toSet()));
        option.setPossibleTariffsOfOption(possibleTariffsOfOption.stream().map(TariffDto::getTariffEntityNoConvert).collect(Collectors.toSet()));
        option.setContractsThoseUseOption(contractsThoseUseOption.stream().map(ContractDto::getContractEntity).collect(Collectors.toSet()));
        return option;
    }*/
/*
    public void convertIdToEntities(OptionDao optionDao, ContractDao contractDao, TariffDao tariffDao) {
        if (optionDao != null) {
            requiredFrom.addAll(requiredFrom.stream().map(e -> new OptionDto(optionDao.read(e))).collect(Collectors.toSet()));
            requiredMe.addAll(requiredMe.stream().map(e -> new OptionDto(optionDao.read(e))).collect(Collectors.toSet()));
            forbiddenWith.addAll(forbiddenWith.stream().map(e -> new OptionDto(optionDao.read(e))).collect(Collectors.toSet()));
        }
        if (contractDao != null) {
            contractsThoseUseOption.addAll(contractsThoseUseOption.stream().map(e -> new ContractDto(contractDao.read(e))).collect(Collectors.toSet()));
        }
        if (tariffDao != null) {
            possibleTariffsOfOption.addAll(forTariffs.stream().map(e -> new TariffDto(tariffDao.read(e))).collect(Collectors.toSet()));
        }
    }*/

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

    public Set<ContractDto> getContractsThoseUseOption() {
        return contractsThoseUseOption;
    }

    public void setContractsThoseUseOption(Set<ContractDto> contractsThoseUseOption) {
        this.contractsThoseUseOption = contractsThoseUseOption;
    }
}
