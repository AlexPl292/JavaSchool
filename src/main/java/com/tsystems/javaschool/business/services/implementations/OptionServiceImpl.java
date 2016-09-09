package com.tsystems.javaschool.business.services.implementations;

import com.tsystems.javaschool.business.services.interfaces.OptionService;
import com.tsystems.javaschool.business.services.interfaces.TariffService;
import com.tsystems.javaschool.db.entities.Option;
import com.tsystems.javaschool.db.entities.Tariff;
import com.tsystems.javaschool.db.implemetations.OptionDaoImpl;
import com.tsystems.javaschool.db.implemetations.TariffDaoImpl;
import com.tsystems.javaschool.db.interfaces.OptionDao;
import com.tsystems.javaschool.util.EMU;

import javax.persistence.EntityGraph;
import javax.persistence.EntityTransaction;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by alex on 27.08.16.
 */
public class OptionServiceImpl implements OptionService{

    private OptionServiceImpl() {}

    private static class OptionServiceHolder {
        private final static OptionServiceImpl instance = new OptionServiceImpl();
    }

    public static OptionServiceImpl getInstance() {
        return OptionServiceHolder.instance;
    }

    private OptionDao optionDao = OptionDaoImpl.getInstance();

    @Override
    public void addNew(Option entity) {
        try {
            EMU.beginTransaction();
            optionDao.create(entity);
            EMU.commit();
        } catch (RuntimeException re) {
            if (EMU.getEntityManager() != null && EMU.getEntityManager().isOpen())
                EMU.rollback();
            throw re;
        } finally {
            EMU.closeEntityManager();
        }
    }

    @Override
    public List<Option> getNEntries(int maxResult, int firstResult) {
        List<Option> options = optionDao.selectFromTo(maxResult, firstResult);
        EMU.closeEntityManager();
        return options;
    }

    @Override
    public long countOfEntries() {
        long res = optionDao.countOfEntities();
        EMU.closeEntityManager();
        return res;
    }

    @Override
    public List<Option> getNEntries(int maxEntries, int firstIndex, String searchQuery) {
        if (searchQuery == null || "".equals(searchQuery))
            return getNEntries(maxEntries, firstIndex);
        List<Option> options = optionDao.importantSearchFromTo(maxEntries, firstIndex, searchQuery);
        EMU.closeEntityManager();
        return options;
    }

    @Override
    public long countOfEntries(String searchQuery) {
        if (searchQuery == null || "".equals(searchQuery))
            return countOfEntries();
        long res = optionDao.countOfImportantSearch(searchQuery);
        EMU.closeEntityManager();
        return res;
    }

    @Override
    public List<Option> loadAll() {
        List<Option> options = optionDao.getAll();
        EMU.closeEntityManager();
        return options;
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
                    .map(s -> TariffDaoImpl.getInstance().read(Integer.parseInt(s)))
                    .collect(Collectors.toSet());
            option.setPossibleTariffsOfOption(tariffs);

            String[] requiredFrom = dependencies.get("requiredFrom");
            String[] requiredMe = dependencies.get("requiredMe");
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
            graph.addAttributeNodes("required", "forbidden");
            hints.put("javax.persistence.loadgraph", graph);
            for (String reqM : forbiddenWith) {
                Integer forbId = Integer.parseInt(reqM);

                Option forbOpt = optionDao.read(forbId, hints);
                option.addForbiddenWithOptions(forbOpt);
                option.addForbiddenWithOptions(forbOpt.getRequired());
                option.addForbiddenWithOptions(forbOpt.getRequiredMe());
            }

            graph = getEntityGraph();
            graph.addAttributeNodes("requiredMe");
            hints.put("javax.persistence.loadgraph", graph);
            for (String reqM : requiredMe) {
                Integer reqMId = Integer.parseInt(reqM);

                Option reqMOpt = optionDao.read(reqMId, hints);
                option.addRequiredMeOptions(reqMOpt);
                option.addRequiredMeOptions(reqMOpt.getRequiredMe());
            }

            optionDao.create(option);
            EMU.commit();
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

}
