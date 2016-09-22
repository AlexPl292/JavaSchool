package com.tsystems.javaschool.business.services.implementations;

import com.tsystems.javaschool.business.services.interfaces.OptionService;
import com.tsystems.javaschool.db.entities.Option;
import com.tsystems.javaschool.db.entities.Tariff;
import com.tsystems.javaschool.db.implemetations.OptionDaoImpl;
import com.tsystems.javaschool.db.implemetations.TariffDaoImpl;
import com.tsystems.javaschool.db.interfaces.OptionDao;
import com.tsystems.javaschool.db.interfaces.TariffDao;
import com.tsystems.javaschool.util.EMU;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityGraph;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by alex on 27.08.16.
 */
public class OptionServiceImpl implements OptionService{

    private static final Logger logger = Logger.getLogger(OptionServiceImpl.class);
    private OptionDao optionDao = OptionDaoImpl.getInstance();
    @Autowired
    private TariffDao tariffDao;

    private OptionServiceImpl() {}

    private static class OptionServiceHolder {
        private static final OptionServiceImpl instance = new OptionServiceImpl();
        private OptionServiceHolder() {}
    }

    public static OptionServiceImpl getInstance() {
        return OptionServiceHolder.instance;
    }

    @Override
    public void addNew(Option entity) {
        try {
            EMU.beginTransaction();
            optionDao.create(entity);
            EMU.commit();
            logger.info("New option is created. Id = "+entity.getId());
        } catch (RuntimeException re) {
            if (EMU.getEntityManager() != null && EMU.getEntityManager().isOpen())
                EMU.rollback();
            throw re;
        } finally {
            EMU.closeEntityManager();
        }
    }

    @Override
    public Option loadByKey(Integer key) {
        Option option = optionDao.read(key);
        EMU.closeEntityManager();
        return option;
    }

    @Override
    public EntityGraph getEntityGraph() {
        return optionDao.getEntityGraph();
    }

    @Override
    public void remove(Integer key) {
        try {
            EMU.beginTransaction();
            optionDao.delete(key);
            EMU.commit();
            logger.info("Option is removed. Id = "+key);
        } catch (RuntimeException re) {
            if (EMU.getEntityManager() != null && EMU.getEntityManager().isOpen())
                EMU.rollback();
            throw re;
        } finally {
            EMU.closeEntityManager();
        }
    }

    @Override
    public Option loadByKey(Integer key, Map<String, Object> hints) {
        Option option = optionDao.read(key, hints);
        EMU.closeEntityManager();
        return option;
    }

    @Override
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
                option.addRequiredFromOptions(reqFOpt.getRequired());
                option.addForbiddenWithOptions(reqFOpt.getForbidden());
            }

            graph = getEntityGraph();
            graph.addAttributeNodes("required", "forbidden", "requiredMe");
            hints.put("javax.persistence.loadgraph", graph);
            for (String reqM : forbiddenWith) {
                Integer forbId = Integer.parseInt(reqM);

                Option forbOpt = optionDao.read(forbId, hints);
                option.addForbiddenWithOptions(forbOpt);
                option.addForbiddenWithOptions(forbOpt.getRequired());
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
    }

    @Override
    public List<Option> loadOptionsByTariffs(List<Integer> tariffs) {
        try {
            EMU.beginTransaction();
            List<Option> options = optionDao.getOptionsOfTariffs(tariffs);
            EMU.commit();
            return options;
        } catch (RuntimeException re) {
            if (EMU.getEntityManager() != null && EMU.getEntityManager().isOpen())
                EMU.rollback();
            throw re;
        } finally {
            EMU.closeEntityManager();
        }
    }


    @Override
    public List<Option> load(Map<String, Object> kwargs) {
        List<Option> options = optionDao.read(kwargs);
        EMU.closeEntityManager();
        return options;
    }

    @Override
    public long count(Map<String, Object> kwargs) {
        long count = optionDao.count(kwargs);
        EMU.closeEntityManager();
        return count;
    }

    @Override
    public List<Option> loadAll() {
        return load(new HashMap<>());
    }
}
