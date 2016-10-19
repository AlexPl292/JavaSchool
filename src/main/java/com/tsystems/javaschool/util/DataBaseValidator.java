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
import com.tsystems.javaschool.exceptions.NoEntityInDBException;
import com.tsystems.javaschool.exceptions.OptionNotAvailableForTariffException;
import com.tsystems.javaschool.exceptions.UniqueFieldDuplicateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by alex on 14.10.16.
 *
 * Validation entities with database queries
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

    /**
     * Validate tariff
     * @param tariff tariff DTO
     * @throws JSException validation fail
     */
    static public void check(TariffDto tariff) throws JSException {
        // Check if tariff name already exists
        Tariff existings = tariffRepository.findByName(tariff.getName());
        if (existings != null) {
            throw new UniqueFieldDuplicateException("Name", tariff.getName(), "/rest/tariffs/" + existings.getId());
        }

        // Validate options
        checkAllOptions(tariff.getPossibleOptions());
    }

    /**
     * Validate option
     * @param option option DTO
     * @throws JSException validation fail
     */
    static public void check(OptionDto option) throws JSException {
        // Check if option name already exists
        Option existings = optionRepository.findByName(option.getName());
        if (existings != null) {
            throw new UniqueFieldDuplicateException("Name", option.getName(), "/rest/options/" + existings.getId());
        }

        // Validate options and tariffs
        Set<TariffDto> possibleTariffs = option.getPossibleTariffsOfOption();
        checkAllTariffs(possibleTariffs);
        checkAllOptions(option.getRequiredFrom(), possibleTariffs);
        checkAllOptions(option.getRequiredMe(), possibleTariffs);
        checkAllOptions(option.getForbiddenWith());
    }

    /**
     * Validate customer
     * @param customer customer DTo
     * @throws JSException validation fail
     */
    static public void check(CustomerDto customer) throws JSException {
        // Check if customer email or password number already exists
        List<Customer> existings = customerRepository.findByPassportNumberOrEmail(customer.getPassportNumber(), customer.getEmail());
        if (existings != null && existings.size() > 0) {
            if (existings.get(0).getEmail().equalsIgnoreCase(customer.getEmail()))
                throw new UniqueFieldDuplicateException("Email", customer.getEmail(), "/rest/options/" + existings.get(0).getId());
            else
                throw new UniqueFieldDuplicateException("PassportNumber", customer.getPassportNumber(), "/rest/options/" + existings.get(0).getId());
        }

        // Check if contract number already exists
        if (customer.getContracts() != null) {
            for (ContractDto contract : customer.getContracts()) {
                Contract existingsContracts = contractRepository.findByNumber(contract.getNumber());
                if (existingsContracts != null) {
                    throw new UniqueFieldDuplicateException("Number", contract.getNumber(), "/rest/contracts/" + existingsContracts.getId());
                }
            }
        }

        // Check all tariffs and options in contract
        if (customer.getContracts() != null && customer.getContracts().size() > 0) {
            for (ContractDto contract : customer.getContracts()) {
                checkAllTariffs(contract.getTariff());
                checkAllOptions(contract.getUsedOptions(), contract.getTariff());
            }
        }
    }

    /**
     * Validate contract
     * @param contract contract dto
     * @throws JSException validation fail
     */
    static public void check(ContractDto contract) throws JSException {
        // Check if contract number already exists
        Contract existings = contractRepository.findByNumber(contract.getNumber());
        if (existings != null) {
            throw new UniqueFieldDuplicateException("Number", contract.getNumber(), "/rest/contracts/" + existings.getId());
        }

        // Check customer existing
        if (contract.getCustomer() == null || customerRepository.findOne(contract.getCustomer().getId()) == null) {
            throw new NoEntityInDBException("No customer with id = " + contract.getCustomer().getId() + " in database");
        }

        // Check all tariffs and options
        checkAllTariffs(contract.getTariff());
        checkAllOptions(contract.getUsedOptions(), contract.getTariff());
    }

    /**
     * Validate all tariff for existing
     * @param tariffs tariff DTO set
     * @throws NoEntityInDBException one of tariffs dont exists
     */
    private static void checkAllTariffs(Set<TariffDto> tariffs) throws NoEntityInDBException {
        if (tariffs != null && tariffs.size() > 0) {
            // Iterate over tariffs
            for (TariffDto tariffDto : tariffs) {
                if (tariffRepository.findOne(tariffDto.getId()) == null)
                    throw new NoEntityInDBException("No tariff with id = " + tariffDto.getId() + " in database");
            }
        }
    }

    /**
     * Validate one tariff for existing
     * @param tariff tariff DTO
     * @throws NoEntityInDBException tariff dont exists
     */
    private static void checkAllTariffs(TariffDto tariff) throws NoEntityInDBException {
        // Create set of tariff and call check all tariffs
        checkAllTariffs(Collections.singleton(tariff));
    }

    private static void checkAllOptionsAbstract(Set<OptionDto> options, Set<TariffDto> possibleTariff) throws JSException {
        if (options != null && options.size() > 0) {
            // Iterate over options
            for (OptionDto option : options) {
                // If option dont exists
                Option oneOption = optionRepository.findOne(option.getId());
                if (oneOption == null)
                    throw new NoEntityInDBException("No option with id = " + option.getId() + " in database");
                else {
                    // If option exists
                    if (possibleTariff != null) {
                        // Check if option is available for ALL there tariffs
                        for (TariffDto tariffCheck : possibleTariff) {
                            if (!oneOption.getPossibleTariffsOfOption().stream().anyMatch(e -> e.getId().equals(tariffCheck.getId()))) {
                                throw new OptionNotAvailableForTariffException("Option with id = " + oneOption.getId() +
                                        " is not available for there tariffs");
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Check options existing and availability for tariff
     * @param options set of options
     * @param possibleTariff tariff
     * @throws JSException validation fail
     */
    private static void checkAllOptions(Set<OptionDto> options, TariffDto possibleTariff) throws JSException {
        checkAllOptionsAbstract(options, Collections.singleton(possibleTariff));
    }

    /**
     * Check options existing
     * @param options set of options
     * @throws JSException validation fail
     */
    private static void checkAllOptions(Set<OptionDto> options) throws JSException {
        checkAllOptionsAbstract(options, null);
    }

    /**
     * Check options existing and availability for all tariffs
     * @param options set of options
     * @param possibleTariff tariff
     * @throws JSException validation fail
     */
    private static void checkAllOptions(Set<OptionDto> options, Set<TariffDto> possibleTariff) throws JSException {
        checkAllOptionsAbstract(options, possibleTariff);
    }

    /**
     * Check options existing and availability for tariff by id
     * @param tariff set of options
     * @param options tariff
     * @throws JSException validation fail
     */
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
