package com.tsystems.javaschool.business.services.implementations;

import com.tsystems.javaschool.business.dto.OptionDto;
import com.tsystems.javaschool.business.services.interfaces.OptionService;
import com.tsystems.javaschool.db.entities.Option;
import com.tsystems.javaschool.db.repository.OptionRepository;
import com.tsystems.javaschool.exceptions.JSException;
import com.tsystems.javaschool.exceptions.WrongOptionConfigurationException;
import com.tsystems.javaschool.util.DataBaseValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by alex on 27.08.16.
 */
@Service
@Transactional
public class OptionServiceImpl implements OptionService {

    private final OptionRepository repository;

    @Autowired
    public OptionServiceImpl(OptionRepository optionRepository) {
        this.repository = optionRepository;
    }

    @Override
    public OptionDto addNew(OptionDto entity) throws JSException {
        DataBaseValidator.check(entity);

        Option option = entity.convertToEntity();

        Set<Option> requireds = option.getRequired().stream().map(e -> repository.findOne(e.getId())).collect(Collectors.toSet());
        Set<Option> forbiddens = option.getForbidden().stream().map(e -> repository.findOne(e.getId())).collect(Collectors.toSet());

        option.setRequired(new HashSet<>());
        option.setForbidden(new HashSet<>());

        for (Option req : requireds) {
            option.addRequiredFromOptions(req);
            option.addRequiredFromOptions(req.getRequired());
            option.addForbiddenWithOptions(req.getForbidden());

            if (forbiddens.contains(req))
                throw new WrongOptionConfigurationException(1);
            if (req.getForbidden().stream().anyMatch(requireds::contains))
                throw new WrongOptionConfigurationException(2);
            if (req.getRequired().stream().anyMatch(forbiddens::contains))
                throw new WrongOptionConfigurationException(3);
        }

        for (Option forb : forbiddens) {
            option.addForbiddenWithOptions(forb);
            option.addForbiddenWithOptions(forb.getRequiredMe());

            // This case exists already in required for block, but let it be
            if (forb.getRequiredMe().stream().anyMatch(requireds::contains))
                throw new WrongOptionConfigurationException(3);
        }

        Option saved = repository.saveAndFlush(option);
        return new OptionDto(saved).addDependencies(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public OptionDto loadByKey(Integer key) {
        Option option = repository.findOne(key);
        return new OptionDto(option).addDependencies(option);
    }

    @Override
    public void remove(Integer key) {
        repository.delete(key);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OptionDto> loadAll() {
        return repository
                .findAll()
                .stream()
                .map(e -> new OptionDto(e).addDependencies(e))
                .collect(Collectors.toList());
    }
}
