package com.tsystems.javaschool.business.services.implementations;

import com.tsystems.javaschool.business.dto.ContractDto;
import com.tsystems.javaschool.business.services.interfaces.ContractService;
import com.tsystems.javaschool.db.entities.Contract;
import com.tsystems.javaschool.db.repository.ContractRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by alex on 24.08.16.
 */
@Service
@Transactional
public class ContractServiceImpl implements ContractService{

    private final ContractRepository repository;

    @Autowired
    public ContractServiceImpl(ContractRepository repository) {
        this.repository = repository;
    }

    @Override
    public ContractDto addNew(ContractDto contractDto) {
        return new ContractDto(repository.saveAndFlush(contractDto.convertToEntity()));
    }

    @Override
    public ContractDto loadByKey(Integer key) {
        Contract contract = repository.findOne(key);
        return new ContractDto(contract).addDependencies(contract);
    }

    @Override
    public void remove(Integer key) {
        repository.delete(key);
    }

    @Override
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
    public List<ContractDto> findByNumber(String number) {
        return repository
                .findByNumber(number)
                .stream()
                .map(ContractDto::new)
                .collect(Collectors.toList());
    }
}
