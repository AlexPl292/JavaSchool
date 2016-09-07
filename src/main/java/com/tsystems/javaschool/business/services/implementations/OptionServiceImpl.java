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
        EMU.beginTransaction();
        optionDao.create(entity);
        EMU.commit();
    }

    @Override
    public List<Option> getNEntries(int maxResult, int firstResult) {
//        EMU.beginTransaction();
        List<Option> options = optionDao.selectFromTo(maxResult, firstResult);
//        EMU.commit();
        return options;
    }

    @Override
    public long countOfEntries() {
//        EMU.beginTransaction();
        long res = optionDao.countOfEntities();
//        EMU.commit();
        return res;
    }

    @Override
    public List<Option> getNEntries(int maxEntries, int firstIndex, String searchQuery) {
        if (searchQuery == null || "".equals(searchQuery))
            return getNEntries(maxEntries, firstIndex);
//        EMU.beginTransaction();
        List<Option> options = optionDao.importantSearchFromTo(maxEntries, firstIndex, searchQuery);
//        EMU.commit();
        return options;
    }

    @Override
    public long countOfEntries(String searchQuery) {
        if (searchQuery == null || "".equals(searchQuery))
            return countOfEntries();
//        EMU.beginTransaction();
        long res = optionDao.countOfImportantSearch(searchQuery);
//        EMU.commit();
        return res;
    }

    @Override
    public List<Option> loadAll() {
//        EMU.beginTransaction();
        List<Option> options = optionDao.getAll();
//        EMU.commit();
        return options;
    }

    @Override
    public Option loadByKey(Integer key) {
//        EMU.beginTransaction();
        Option option = optionDao.read(key);
//        EMU.commit();
        return option;
    }

    @Override
    public EntityGraph getEntityGraph() {
        return optionDao.getEntityGraph();
    }

    @Override
    public void remove(Integer key) {
        EMU.beginTransaction();
        optionDao.delete(key);
        EMU.commit();
    }

    @Override
    public Option loadByKey(Integer key, Map<String, Object> hints) {
//        EMU.beginTransaction();
        Option option = optionDao.read(key, hints);
//        EMU.commit();
        return option;
    }

    @Override
    public Option addNew(Option option, Map<String, String[]> dependencies) {

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
    }

    @Override
    public List<Option> loadOptionsByTariffs(List<Integer> tariffs) {
        EMU.beginTransaction();
        List<Option> options = optionDao.getOptionsOfTariffs(tariffs);
        EMU.commit();
        return options;
    }

}
