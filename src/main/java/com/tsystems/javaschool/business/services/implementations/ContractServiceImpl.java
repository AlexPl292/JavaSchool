package com.tsystems.javaschool.business.services.implementations;

import com.tsystems.javaschool.business.dto.ContractDto;
import com.tsystems.javaschool.business.services.interfaces.ContractService;
import com.tsystems.javaschool.db.entities.Contract;
import com.tsystems.javaschool.db.entities.Option;
import com.tsystems.javaschool.db.entities.Tariff;
import com.tsystems.javaschool.db.repository.ContractRepository;
import com.tsystems.javaschool.exceptions.JSException;
import com.tsystems.javaschool.util.DataBaseValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by alex on 24.08.16.
 *
 * Contract service
 */
@Service
@Transactional
public class ContractServiceImpl implements ContractService {

    private final ContractRepository repository;

    @Autowired
    public ContractServiceImpl(ContractRepository repository) {
        this.repository = repository;
    }

    @Override
    public ContractDto addNew(ContractDto contractDto) throws JSException {
        DataBaseValidator.check(contractDto);

        Contract contract = contractDto.convertToEntity();
        contract.setBalance(new BigDecimal("100.00"));
        contract.setIsBlocked(0);
        return new ContractDto(repository.saveAndFlush(contract));
    }

    @Override
    @Transactional(readOnly = true)
    public ContractDto loadByKey(Integer key) {
        Contract contract = repository.findOne(key);
        return new ContractDto(contract).addDependencies(contract);
    }

    @Override
    public void remove(Integer key) {
        repository.delete(key);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ContractDto> loadAll() {
        return repository
                .findAll()
                .stream()
                .map(e -> new ContractDto(e).addDependencies(e))
                .collect(Collectors.toList());
    }

    @Override
    public ContractDto setBlock(Integer id, Integer blockLevel) {
        Contract contract = repository.findOne(id);
        contract.setIsBlocked(blockLevel);
        return new ContractDto(contract);
    }

    @Override
    public ContractDto updateContract(Integer contractId, Integer tariffId, List<Integer> optionIds) throws JSException {
        DataBaseValidator.checkAllOptions(tariffId, optionIds);

        Contract contract = repository.findOne(contractId);
        if (contract == null) {
            return new ContractDto();
        }

        contract.getUsedOptions().size();
        Set<Option> oldOptions = contract.getUsedOptions();

        Tariff tariff = new Tariff();
        tariff.setId(tariffId);
        contract.setTariff(tariff);

        Set<Option> options = new HashSet<>();
        if (optionIds != null) {
            for (Integer id : optionIds) {
                Option opt = new Option();
                opt.setId(id);
                options.add(opt);
            }
        }
        contract.setUsedOptions(options);
        contract = repository.saveAndFlush(contract);

        Set<Option> newOptions = contract.getUsedOptions();
        BigDecimal summ = newOptions.stream()
                .filter(e -> !oldOptions.contains(e))
                .map(Option::getConnectCost)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        contract.setBalance(contract.getBalance().subtract(summ));

        return new ContractDto(contract).addDependencies(contract);
    }

    @Override
    @Transactional(readOnly = true)
    public ContractDto findByNumber(String number) {
        return new ContractDto(repository.findByNumber(number));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ContractDto> findByTariffName(String name) {
        return repository
                .findByTariff_Name(name)
                .stream()
                .map(e -> new ContractDto(e).addDependencies(e))
                .collect(Collectors.toList());
    }
}
