package com.tsystems.javaschool.business.services.implementations;

import com.tsystems.javaschool.business.dto.TariffDto;
import com.tsystems.javaschool.business.services.interfaces.TariffService;
import com.tsystems.javaschool.db.entities.Tariff;
import com.tsystems.javaschool.db.repository.TariffRepository;
import com.tsystems.javaschool.exceptions.UniqueFieldDuplicateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by alex on 21.08.16.
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
    public TariffDto addNew(TariffDto tariffDto) throws UniqueFieldDuplicateException {
        List<Tariff> existings = repository.findByName(tariffDto.getName());
        if (existings != null && existings.size() > 0) {
            throw new UniqueFieldDuplicateException("Name", tariffDto.getName(), "/rest/tariffs/" + existings.get(0).getId());
        }

        return new TariffDto(repository.saveAndFlush(tariffDto.convertToEntity()));
    }

    @Override
    public TariffDto loadByKey(Integer key) {
        Tariff tariff = repository.findOne(key);
        return new TariffDto(tariff).addDependencies(tariff);
    }

    @Override
    public void remove(Integer key) {
        repository.delete(key);
    }

    @Override
    public List<TariffDto> loadAll() {
        return repository
                .findAll()
                .stream()
                .map(e -> new TariffDto(e).addDependencies(e))
                .collect(Collectors.toList());
    }

    @Override
    public List<TariffDto> findByName(String name) {
        return repository
                .findByName(name)
                .stream()
                .map(TariffDto::new)
                .collect(Collectors.toList());
    }
}
