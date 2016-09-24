package com.tsystems.javaschool.business.services.implementations;

import com.tsystems.javaschool.business.dto.OptionDto;
import com.tsystems.javaschool.business.services.interfaces.OptionService;
import com.tsystems.javaschool.db.entities.Option;
import com.tsystems.javaschool.db.interfaces.ContractDao;
import com.tsystems.javaschool.db.interfaces.OptionDao;
import com.tsystems.javaschool.db.interfaces.TariffDao;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityGraph;
import java.util.*;

/**
 * Created by alex on 27.08.16.
 */
@Service
@Transactional
public class OptionServiceImpl implements OptionService{

    private static final Logger logger = Logger.getLogger(OptionServiceImpl.class);
    private final OptionDao optionDao;
    private final TariffDao tariffDao;
    private final ContractDao contractDao;

    @Autowired
    public OptionServiceImpl(TariffDao tariffDao, OptionDao optionDao, ContractDao contractDao) {
        this.tariffDao = tariffDao;
        this.optionDao = optionDao;
        this.contractDao = contractDao;
    }

    @Override
    public void addNew(OptionDto entity) {
        Option option = entity.convertToOptionEntity(optionDao, tariffDao);
        optionDao.create(option);
        logger.info("New option is created. Id = "+entity.getId());
    }

    @Override
    public OptionDto loadByKey(Integer key) {
        return new OptionDto(optionDao.read(key));
    }

    @Override
    public EntityGraph getEntityGraph() {
        return optionDao.getEntityGraph();
    }

    @Override
    public void remove(Integer key) {
        optionDao.delete(key);
        logger.info("Option is removed. Id = "+key);
    }

    @Override
    public OptionDto loadByKey(Integer key, Map<String, Object> hints) {
        Option option = optionDao.read(key, hints);
        OptionDto optionDto = new OptionDto(option);
        optionDto.setDependencies(option);
        return optionDto;
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

    @Override
    public List<OptionDto> loadOptionsByTariffs(List<Integer> tariffs) {
        if (tariffs == null) {
            return new ArrayList<>();
        }
        List<Option> options = optionDao.getOptionsOfTariffs(tariffs);
        List<OptionDto> optionDtos = new ArrayList<>();
        for (Option o:options) {
            OptionDto od = new OptionDto(o);
            od.setDependencies(o);
            optionDtos.add(od);
        }
        return optionDtos;
    }


    @Override
    public List<OptionDto> load(Map<String, Object> kwargs) {
        List<Option> options = optionDao.read(kwargs);
        List<OptionDto> optionDtos = new ArrayList<>();
        for (Option o:options) {
            OptionDto od = new OptionDto(o);
            od.setDependencies(o);
            optionDtos.add(od);
        }
        return optionDtos;
    }

    @Override
    public long count(Map<String, Object> kwargs) {
        return optionDao.count(kwargs);
    }

    @Override
    public List<OptionDto> loadAll() {
        List<Option> options = optionDao.read(new HashMap<>());
        List<OptionDto> optionDtos = new ArrayList<>();
        for (Option o:options) {
            OptionDto od = new OptionDto(o);
            optionDtos.add(od);
        }
        return optionDtos;
    }
}
