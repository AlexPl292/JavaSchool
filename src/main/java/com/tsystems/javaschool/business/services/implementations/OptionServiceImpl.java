package com.tsystems.javaschool.business.services.implementations;

import com.tsystems.javaschool.business.dto.OptionDto;
import com.tsystems.javaschool.business.services.interfaces.OptionService;
import com.tsystems.javaschool.db.entities.Option;
import com.tsystems.javaschool.db.repository.OptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by alex on 27.08.16.
 */
@Service
@Transactional
public class OptionServiceImpl implements OptionService{

    private final OptionRepository repository;

    @Autowired
    public OptionServiceImpl(OptionRepository optionRepository) {
        this.repository = optionRepository;
    }

    @Override
    public OptionDto addNew(OptionDto entity) {
        return new OptionDto(repository.saveAndFlush(entity.convertToEntity()));
    }

    @Override
    public OptionDto loadByKey(Integer key) {
        Option option = repository.findOne(key);
        return new OptionDto(option).addDependencies(option);
    }

    @Override
    public void remove(Integer key) {
        repository.delete(key);
    }

    @Override
    public List<OptionDto> loadAll() {
        return repository.findAll().stream().map(e -> new OptionDto(e).addDependencies(e)).collect(Collectors.toList());
    }

/*    @Override
    public Option addNew(Option option, Map<String, String[]> dependencies) {
        try {
            EMU.beginTransaction();
            Set<Tariff> tariffs = Arrays.stream(dependencies.get("forTariffs")) // Convert array of tariff ids to set of tariffs
                    .map(s -> tariffDao.read(Integer.parseInt(s)))
                    .collect(Collectors.toSet());
            option.setPossibleTariffsOfOption(tariffs);

            String[] requiredFrom = dependencies.get("requiredFrom");
            String[] forbiddenWith = dependencies.get("forbiddenWith");

            EntityGraph<Option> graph = getEntityGraph();
            graph.addAttributeNodes("required", "forbidden");  //Fetch this fields
            Map<String, Object> hints = new HashMap<>();
            hints.put("javax.persistence.loadgraph", graph);

            for (String reqF : requiredFrom) {
                Integer reqFId = Integer.parseInt(reqF);

                Option reqFOpt = optionDao.read(reqFId, hints);
                option.addRequiredFromOptions(reqFOpt);
                option.addRequiredFromOptions(reqFOpt.getRequiredFrom());
                option.addForbiddenWithOptions(reqFOpt.getForbiddenWith());
            }

            graph = getEntityGraph();
            graph.addAttributeNodes("required", "forbidden", "requiredMe");
            hints.put("javax.persistence.loadgraph", graph);
            for (String reqM : forbiddenWith) {
                Integer forbId = Integer.parseInt(reqM);

                Option forbOpt = optionDao.read(forbId, hints);
                option.addForbiddenWithOptions(forbOpt);
                option.addForbiddenWithOptions(forbOpt.getRequiredFrom());
                option.addForbiddenWithOptions(forbOpt.getRequiredMe());
            }

            optionDao.create(option);
            EMU.commit();
            logger.info("New option is created. Id = "+option.getId());
            return option;
        } catch (RuntimeException re) {
            if (EMU.getEntityManager() != null && EMU.getEntityManager().isOpen())
                EMU.rollback();
            throw re;
        } finally {
            EMU.closeEntityManager();
        }
    }*/
}
