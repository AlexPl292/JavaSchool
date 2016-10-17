package com.tsystems.javaschool.business.services.implementations;

import com.tsystems.javaschool.business.dto.TariffDto;
import com.tsystems.javaschool.business.services.interfaces.TariffService;
import com.tsystems.javaschool.db.entities.Tariff;
import com.tsystems.javaschool.db.repository.TariffRepository;
import com.tsystems.javaschool.exceptions.JSException;
import com.tsystems.javaschool.util.DataBaseValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by alex on 21.08.16.
 *
 *  Tariff service implementation
 */
@Service
@Transactional
public class TariffServiceImpl implements TariffService {

    private final TariffRepository repository;

    @Autowired
    public TariffServiceImpl(TariffRepository tariffRepository) {
        this.repository = tariffRepository;
    }

    @Override
    public TariffDto addNew(TariffDto tariffDto) throws JSException {
        // Validate new tariff data
        DataBaseValidator.check(tariffDto);

        return new TariffDto(repository.saveAndFlush(tariffDto.convertToEntity()));
    }

    @Override
    @Transactional(readOnly = true)
    public TariffDto loadByKey(Integer key) {
        Tariff tariff = repository.findOne(key);
        return new TariffDto(tariff).addDependencies(tariff);
    }

    @Override
    public void remove(Integer key) {
        repository.delete(key);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TariffDto> loadAll() {
        // Get all options. Translate to DTO objects with dependencies
        return repository
                .findAll()
                .stream()
                .map(e -> new TariffDto(e).addDependencies(e))
                .collect(Collectors.toList());
    }
}
