package com.tsystems.javaschool.util;

import com.tsystems.javaschool.business.dto.ContractDto;
import com.tsystems.javaschool.business.dto.CustomerDto;
import com.tsystems.javaschool.business.dto.OptionDto;
import com.tsystems.javaschool.business.dto.TariffDto;
import com.tsystems.javaschool.business.services.interfaces.ContractService;
import com.tsystems.javaschool.business.services.interfaces.CustomerService;
import com.tsystems.javaschool.business.services.interfaces.OptionService;
import com.tsystems.javaschool.business.services.interfaces.TariffService;
import com.tsystems.javaschool.exceptions.UniqueFieldDuplicateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by alex on 06.10.16.
 * <p>
 * Static validators for DataBase entries
 */
@Component
public class DataBaseValidator {

    static private TariffService tariffService;
    static private OptionService optionService;
    static private ContractService contractService;
    static private CustomerService customerService;

    @Autowired
    public DataBaseValidator(TariffService tariffService, OptionService optionService, ContractService contractService, CustomerService customerService) {
        DataBaseValidator.tariffService = tariffService;
        DataBaseValidator.optionService = optionService;
        DataBaseValidator.contractService = contractService;
        DataBaseValidator.customerService = customerService;
    }

    public static void checkUnique(TariffDto tariffDto) throws UniqueFieldDuplicateException {
        List<TariffDto> existings = tariffService.findByName(tariffDto.getName());
        if (existings != null && existings.size() > 0) {
            throw new UniqueFieldDuplicateException("Name", tariffDto.getName(), "/rest/tariffs/" + existings.get(0).getId());
        }
    }

    public static void checkUnique(OptionDto optionDto) throws UniqueFieldDuplicateException {
        List<OptionDto> existings = optionService.findByName(optionDto.getName());
        if (existings != null && existings.size() > 0) {
            throw new UniqueFieldDuplicateException("Name", optionDto.getName(), "/rest/options/" + existings.get(0).getId());
        }
    }

    public static void checkUnique(ContractDto contractDto) throws UniqueFieldDuplicateException {
        List<ContractDto> existings = contractService.findByNumber(contractDto.getNumber());
        if (existings != null && existings.size() > 0) {
            throw new UniqueFieldDuplicateException("Name", contractDto.getNumber(), "/rest/contracts/" + existings.get(0).getId());
        }
    }

    public static void checkUnique(CustomerDto customerDto) throws UniqueFieldDuplicateException {
        List<CustomerDto> existings = customerService.findByPassportNumberOrEmail(customerDto.getPassportNumber(), customerDto.getEmail());
        if (existings != null && existings.size() > 0) {
            if (existings.get(0).getEmail().equalsIgnoreCase(customerDto.getEmail()))
                throw new UniqueFieldDuplicateException("Email", customerDto.getEmail(), "/rest/options/" + existings.get(0).getId());
            else
                throw new UniqueFieldDuplicateException("PassportNumber", customerDto.getPassportNumber(), "/rest/options/" + existings.get(0).getId());
        }

        if (customerDto.getContracts() != null) {
            for (ContractDto contract : customerDto.getContracts()) {
                List<ContractDto> existingsContracts = contractService.findByNumber(contract.getNumber());
                if (existingsContracts != null && existingsContracts.size() > 0) {
                    throw new UniqueFieldDuplicateException("Name", contract.getNumber(), "/rest/contracts/" + existingsContracts.get(0).getId());
                }
            }
        }
    }
}
