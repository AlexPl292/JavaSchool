package com.tsystems.javaschool.util;

import com.tsystems.javaschool.business.dto.ContractDto;
import com.tsystems.javaschool.business.dto.CustomerDto;
import com.tsystems.javaschool.business.dto.OptionDto;
import com.tsystems.javaschool.business.dto.TariffDto;
import com.tsystems.javaschool.db.entities.Contract;
import com.tsystems.javaschool.db.entities.Customer;
import com.tsystems.javaschool.db.entities.Option;
import com.tsystems.javaschool.db.entities.Tariff;
import com.tsystems.javaschool.db.repository.ContractRepository;
import com.tsystems.javaschool.db.repository.CustomerRepository;
import com.tsystems.javaschool.db.repository.OptionRepository;
import com.tsystems.javaschool.db.repository.TariffRepository;
import com.tsystems.javaschool.exceptions.JSException;
import com.tsystems.javaschool.exceptions.NoEntityInDB;
import com.tsystems.javaschool.exceptions.OptionNotAvailableForTariff;
import com.tsystems.javaschool.exceptions.UniqueFieldDuplicateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by alex on 14.10.16.
 */
@Component
public class DataBaseValidator {

    private static TariffRepository tariffRepository;
    private static OptionRepository optionRepository;
    private static CustomerRepository customerRepository;
    private static ContractRepository contractRepository;

    @Autowired
    public DataBaseValidator(TariffRepository tariffRepository, OptionRepository optionRepository, CustomerRepository customerRepository, ContractRepository contractRepository) {
        DataBaseValidator.tariffRepository = tariffRepository;
        DataBaseValidator.optionRepository = optionRepository;
        DataBaseValidator.customerRepository = customerRepository;
        DataBaseValidator.contractRepository = contractRepository;
    }

    static public void check(TariffDto tariff) throws JSException {
        List<Tariff> existings = tariffRepository.findByName(tariff.getName());
        if (existings != null && existings.size() > 0) {
            throw new UniqueFieldDuplicateException("Name", tariff.getName(), "/rest/tariffs/" + existings.get(0).getId());
        }
        checkAllOptions(tariff.getPossibleOptions());
    }

    static public void check(OptionDto option) throws JSException {
        Option existings = optionRepository.findByName(option.getName());
        if (existings != null) {
            throw new UniqueFieldDuplicateException("Name", option.getName(), "/rest/options/" + existings.getId());
        }

        Set<TariffDto> possibleTariffs = option.getPossibleTariffsOfOption();
        checkAllTariffs(possibleTariffs);
        checkAllOptions(option.getRequiredFrom(), possibleTariffs);
        checkAllOptions(option.getRequiredMe(), possibleTariffs);
        checkAllOptions(option.getForbiddenWith());
    }

    static public void check(CustomerDto customer) throws JSException {
        List<Customer> existings = customerRepository.findByPassportNumberOrEmail(customer.getPassportNumber(), customer.getEmail());
        if (existings != null && existings.size() > 0) {
            if (existings.get(0).getEmail().equalsIgnoreCase(customer.getEmail()))
                throw new UniqueFieldDuplicateException("Email", customer.getEmail(), "/rest/options/" + existings.get(0).getId());
            else
                throw new UniqueFieldDuplicateException("PassportNumber", customer.getPassportNumber(), "/rest/options/" + existings.get(0).getId());
        }

        if (customer.getContracts() != null) {
            for (ContractDto contract : customer.getContracts()) {
                Contract existingsContracts = contractRepository.findByNumber(contract.getNumber());
                if (existingsContracts != null) {
                    throw new UniqueFieldDuplicateException("Number", contract.getNumber(), "/rest/contracts/" + existingsContracts.getId());
                }
            }
        }

        if (customer.getContracts() != null && customer.getContracts().size() > 0) {
            for (ContractDto contract : customer.getContracts()) {
                checkAllTariffs(contract.getTariff());
                checkAllOptions(contract.getUsedOptions(), contract.getTariff());
            }
        }
    }

    static public void check(ContractDto contract) throws JSException {
        Contract existings = contractRepository.findByNumber(contract.getNumber());
        if (existings != null) {
            throw new UniqueFieldDuplicateException("Number", contract.getNumber(), "/rest/contracts/" + existings.getId());
        }

        if (contract.getCustomer() == null || customerRepository.findOne(contract.getCustomer().getId()) == null) {
            throw new NoEntityInDB("No customer with id = " + contract.getCustomer().getId() + " in database");
        }

        checkAllTariffs(contract.getTariff());
        checkAllOptions(contract.getUsedOptions(), contract.getTariff());
    }

    private static void checkAllTariffs(Set<TariffDto> tariffs) throws NoEntityInDB {
        if (tariffs != null && tariffs.size() > 0) {
            for (TariffDto tariffDto : tariffs) {
                if (tariffRepository.findOne(tariffDto.getId()) == null)
                    throw new NoEntityInDB("No tariff with id = " + tariffDto.getId() + " in database");
            }
        }
    }

    private static void checkAllTariffs(TariffDto tariff) throws NoEntityInDB {
        Set<TariffDto> tariffs = new HashSet<>();
        tariffs.add(tariff);
        checkAllTariffs(tariffs);
    }

    private static void checkAllOptionsAbstract(Set<OptionDto> options, Set<TariffDto> possibleTariff) throws JSException {
        if (options != null && options.size() > 0) {
            for (OptionDto option : options) {
                Option oneOption = optionRepository.findOne(option.getId());
                if (oneOption == null)
                    throw new NoEntityInDB("No option with id = " + option.getId() + " in database");
                else {
                    if (possibleTariff != null) {
                        for (TariffDto tariffCheck : possibleTariff) {
                            if (!oneOption.getPossibleTariffsOfOption().stream().anyMatch(e -> e.getId().equals(tariffCheck.getId()))) {
                                throw new OptionNotAvailableForTariff("Option with id = " + oneOption.getId() +
                                        " is not available for there tariffs");
                            }
                        }
                    }
                }
            }
        }
    }

    private static void checkAllOptions(Set<OptionDto> options, TariffDto possibleTariff) throws JSException {
        Set<TariffDto> tariffs = new HashSet<>();
        tariffs.add(possibleTariff);
        checkAllOptionsAbstract(options, tariffs);
    }

    private static void checkAllOptions(Set<OptionDto> options) throws JSException {
        checkAllOptionsAbstract(options, null);
    }

    private static void checkAllOptions(Set<OptionDto> options, Set<TariffDto> possibleTariff) throws JSException {
        checkAllOptionsAbstract(options, possibleTariff);
    }

    public static void checkAllOptions(Integer tariff, List<Integer> options) throws JSException {
        TariffDto tariffDto = new TariffDto();
        tariffDto.setId(tariff);
        if (options != null) {
            Set<OptionDto> optionDtos = new HashSet<>();
            for (Integer optionId : options) {
                OptionDto optionDto = new OptionDto();
                optionDto.setId(optionId);
                optionDtos.add(optionDto);
            }
            checkAllOptions(optionDtos, tariffDto);
        }
        checkAllTariffs(tariffDto);
    }
}
