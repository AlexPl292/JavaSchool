package com.tsystems.javaschool.business.dto;

import com.tsystems.javaschool.db.entities.Tariff;
import org.apache.commons.lang3.ObjectUtils;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * Created by alex on 22.09.16.
 */
public class TariffDto implements DtoMapper<Tariff>, Comparable<TariffDto> {
    private Integer id;

    @Size(min = 2, max = 45)
    @NotNull
    private String name;

    @Digits(integer = 8, fraction = 2)
    @NotNull
    private BigDecimal cost;

    @Size(max = 255)
    private String description;
    private TreeSet<OptionDto> possibleOptions = new TreeSet<>();

    public TariffDto() {}

    public TariffDto(Tariff tariff) {
        convertToDto(tariff);
    }

    @Override
    public TariffDto addDependencies(Tariff tariff) {
        if (tariff != null && tariff.getPossibleOptions() != null)
            this.possibleOptions = tariff.getPossibleOptions().stream()
                    .map(OptionDto::new)
                    .collect(Collectors.toCollection(TreeSet::new));
        return this;
    }

    @Override
    public Tariff convertToEntity() {
        Tariff tariff = new Tariff(id, name, cost, description);
        if (possibleOptions != null)
            tariff.setPossibleOptions(possibleOptions.stream().map(OptionDto::convertToEntity).collect(Collectors.toSet()));
        return tariff;
    }

    @Override
    public void convertToDto(Tariff entity) {
        if (entity == null)
            return;
        this.id = entity.getId();
        this.name = entity.getName();
        this.cost = entity.getCost();
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TreeSet<OptionDto> getPossibleOptions() {
        return possibleOptions;
    }

    public void setPossibleOptions(TreeSet<OptionDto> possibleOptions) {
        this.possibleOptions = possibleOptions;
    }

    @Override
    public int compareTo(TariffDto o) {
        return ObjectUtils.compare(this.id, o.getId());
    }
}
