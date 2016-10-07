package com.tsystems.javaschool.business.dto;

import com.tsystems.javaschool.db.entities.Option;
import org.apache.commons.lang3.ObjectUtils;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * Created by alex on 08.09.16.
 */
public class OptionDto implements DtoMapper<Option>, Comparable<OptionDto> {

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

    @Size(max = 255)
    private String description;
    private TreeSet<OptionDto> requiredFrom = new TreeSet<>();
    private TreeSet<OptionDto> requiredMe = new TreeSet<>();
    private TreeSet<OptionDto> forbiddenWith = new TreeSet<>();
    private TreeSet<TariffDto> possibleTariffsOfOption = new TreeSet<>();

    public OptionDto() {
    }

    public OptionDto(Option option) {
        convertToDto(option);
    }

    @Override
    public OptionDto addDependencies(Option option) {
        if (option == null)
            return this;

        if (option.getRequired() != null)
            requiredFrom = option.getRequired().stream()
                    .map(OptionDto::new)
                    .collect(Collectors.toCollection(TreeSet::new));
        if (option.getRequiredMe() != null)
            requiredMe = option.getRequiredMe().stream()
                    .map(OptionDto::new)
                    .collect(Collectors.toCollection(TreeSet::new));
        if (option.getForbidden() != null)
            forbiddenWith = option.getForbidden().stream()
                    .map(OptionDto::new)
                    .collect(Collectors.toCollection(TreeSet::new));
        if (option.getPossibleTariffsOfOption() != null)
            possibleTariffsOfOption = option.getPossibleTariffsOfOption().stream()
                    .map(TariffDto::new)
                    .collect(Collectors.toCollection(TreeSet::new));

        return this;
    }

    @Override
    public Option convertToEntity() {
        Option option = new Option(id, name, cost, connectCost, description);
        if (requiredFrom != null)
            option.setRequired(requiredFrom.stream().map(OptionDto::convertToEntity).collect(Collectors.toSet()));
        if (requiredMe != null)
            option.setRequiredMe(requiredMe.stream().map(OptionDto::convertToEntity).collect(Collectors.toSet()));
        if (forbiddenWith != null)
            option.setForbidden(forbiddenWith.stream().map(OptionDto::convertToEntity).collect(Collectors.toSet()));
        if (possibleTariffsOfOption != null)
            option.setPossibleTariffsOfOption(possibleTariffsOfOption.stream().map(TariffDto::convertToEntity).collect(Collectors.toSet()));
        return option;
    }

    @Override
    public void convertToDto(Option entity) {
        if (entity == null)
            return;
        this.id = entity.getId();
        this.name = entity.getName();
        this.cost = entity.getCost();
        this.connectCost = entity.getConnectCost();
        this.description = entity.getDescription();
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

    public TreeSet<OptionDto> getRequiredFrom() {
        return requiredFrom;
    }

    public void setRequiredFrom(TreeSet<OptionDto> requiredFrom) {
        this.requiredFrom = requiredFrom;
    }

    public TreeSet<OptionDto> getRequiredMe() {
        return requiredMe;
    }

    public void setRequiredMe(TreeSet<OptionDto> requiredMe) {
        this.requiredMe = requiredMe;
    }

    public TreeSet<OptionDto> getForbiddenWith() {
        return forbiddenWith;
    }

    public void setForbiddenWith(TreeSet<OptionDto> forbiddenWith) {
        this.forbiddenWith = forbiddenWith;
    }

    public TreeSet<TariffDto> getPossibleTariffsOfOption() {
        return possibleTariffsOfOption;
    }

    public void setPossibleTariffsOfOption(TreeSet<TariffDto> possibleTariffsOfOption) {
        this.possibleTariffsOfOption = possibleTariffsOfOption;
    }

    @Override
    public int compareTo(OptionDto o) {
        return ObjectUtils.compare(this.id, o.getId());
    }
}
