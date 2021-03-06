package com.tsystems.javaschool.business.dto;

import com.tsystems.javaschool.db.entities.Contract;
import org.apache.commons.lang3.ObjectUtils;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * Created by alex on 08.09.16.
 *
 * Data transfer object for Contract entity
 */
public class ContractDto implements DtoMapper<Contract>, Comparable<ContractDto> {
    private Integer id;

    @NotNull
    @Pattern(regexp = "^(\\+\\d{1,2}\\s)?\\(?\\d{3}\\)?[\\s.-]\\d{3}[\\s.-]\\d{4}$")
    private String number;

    @Min(0)
    @Max(2)
    private Integer isBlocked;

    private CustomerDto customer;
    private TariffDto tariff;
    private BigDecimal balance;
    private TreeSet<OptionDto> usedOptions = new TreeSet<>();

    public ContractDto() {
        // Empty
    }

    /**
     * Create dto object from entity
     * @param contract entity to convert
     */
    public ContractDto(Contract contract) {
        this.id = contract.getId();
        this.number = contract.getNumber();
        this.isBlocked = contract.getIsBlocked();
        this.balance = contract.getBalance();
        if (contract.getCustomer() != null)
            this.customer = new CustomerDto(contract.getCustomer());
        if (contract.getTariff() != null)
            this.tariff = new TariffDto(contract.getTariff());
    }

    @Override
    public Contract convertToEntity() {
        Contract contract = new Contract();
        contract.setId(id);
        contract.setNumber(number);
        contract.setIsBlocked(isBlocked);
        contract.setBalance(balance);
        if (customer != null)
            contract.setCustomer(customer.convertToEntity());
        if (tariff != null)
            contract.setTariff(tariff.convertToEntity());
        if (usedOptions != null)
            contract.setUsedOptions(usedOptions.stream().map(OptionDto::convertToEntity).collect(Collectors.toSet()));
        return contract;
    }

    @Override
    public void convertToDto(Contract entity) {
        if (entity == null)
            return;

        this.id = entity.getId();
        this.number = entity.getNumber();
        this.isBlocked = entity.getIsBlocked();
        this.balance = entity.getBalance();
        if (entity.getCustomer() != null)
            this.customer = new CustomerDto(entity.getCustomer());
        if (entity.getTariff() != null)
            this.tariff = new TariffDto(entity.getTariff());
    }

    @Override
    public ContractDto addDependencies(Contract entity) {
        if (entity != null && entity.getUsedOptions() != null)
            this.usedOptions = entity.getUsedOptions().stream()
                    .map(OptionDto::new)
                    .collect(Collectors.toCollection(TreeSet::new));
        return this;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Integer getIsBlocked() {
        return isBlocked;
    }

    public void setIsBlocked(Integer isBlocked) {
        this.isBlocked = isBlocked;
    }

    public CustomerDto getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerDto customer) {
        this.customer = customer;
    }

    public TariffDto getTariff() {
        return tariff;
    }

    public void setTariff(TariffDto tariff) {
        this.tariff = tariff;
    }

    public TreeSet<OptionDto> getUsedOptions() {
        return usedOptions;
    }

    public void setUsedOptions(TreeSet<OptionDto> usedOptions) {
        this.usedOptions = usedOptions;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    @Override
    public int compareTo(ContractDto o) {
        return ObjectUtils.compare(this.id, o.getId());
    }

    @Override
    public String toString() {
        return "ContractDto{" +
                "id=" + id +
                ", number='" + number + '\'' +
                ", isBlocked=" + isBlocked +
                ", customer=" + customer +
                ", tariff=" + tariff +
                ", balance=" + balance +
                ", usedOptions=" + usedOptions.stream().map(OptionDto::getId).collect(Collectors.toList())+
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ContractDto that = (ContractDto) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (number != null ? !number.equals(that.number) : that.number != null) return false;
        if (isBlocked != null ? !isBlocked.equals(that.isBlocked) : that.isBlocked != null) return false;
        if (customer != null ? !customer.equals(that.customer) : that.customer != null) return false;
        if (tariff != null ? !tariff.equals(that.tariff) : that.tariff != null) return false;
        return balance != null ? balance.equals(that.balance) : that.balance == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (number != null ? number.hashCode() : 0);
        result = 31 * result + (isBlocked != null ? isBlocked.hashCode() : 0);
        result = 31 * result + (customer != null ? customer.hashCode() : 0);
        result = 31 * result + (tariff != null ? tariff.hashCode() : 0);
        result = 31 * result + (balance != null ? balance.hashCode() : 0);
        return result;
    }
}
