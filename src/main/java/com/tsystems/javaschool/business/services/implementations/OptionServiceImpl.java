package com.tsystems.javaschool.business.services.implementations;

import com.tsystems.javaschool.business.services.interfaces.OptionService;
import com.tsystems.javaschool.business.services.interfaces.TariffService;
import com.tsystems.javaschool.db.entities.Option;
import com.tsystems.javaschool.db.entities.Tariff;
import com.tsystems.javaschool.db.implemetations.OptionDaoImpl;
import com.tsystems.javaschool.db.interfaces.GenericDao;
import com.tsystems.javaschool.db.interfaces.OptionDao;

import javax.persistence.EntityGraph;
import javax.persistence.EntityTransaction;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by alex on 27.08.16.
 */
public class OptionServiceImpl implements OptionService{

    private OptionDao optionDao = new OptionDaoImpl();
    @Override
    public Option addNew(Option entity) {
        return optionDao.create(entity);
    }

    @Override
    public List<Option> getNEntries(int maxResult, int firstResult) {
        return optionDao.selectFromTo(maxResult, firstResult);
    }

    @Override
    public long countOfEntries() {
        return optionDao.countOfEntities();
    }

    @Override
    public List<Option> getNEntries(int maxEntries, int firstIndex, String searchQuery) {
        if (searchQuery == null || "".equals(searchQuery))
            return getNEntries(maxEntries, firstIndex);
        return optionDao.importantSearchFromTo(maxEntries, firstIndex, searchQuery);
    }

    @Override
    public long countOfEntries(String searchQuery) {
        if (searchQuery == null || "".equals(searchQuery))
            return countOfEntries();
        return optionDao.countOfImportantSearch(searchQuery);
    }

    @Override
    public List<Option> loadAll() {
        return optionDao.getAll();
    }

    @Override
    public Option loadByKey(Integer key) {
        return optionDao.read(key);
    }

    @Override
    public EntityGraph getEntityGraph() {
        return optionDao.getEntityGraph();
    }

    @Override
    public Option loadWithDependencies(Integer key, Map<String, Object> hints) {
        return optionDao.readWithDependencies(key, hints);
    }

    @Override
    public Option addWithDependencies(Option option, Map<String, String[]> dependencies) {
        EntityTransaction transaction = GenericDao.getTransaction();
        TariffService tariffService = new TariffServiceImpl();

        transaction.begin();
        Set<Tariff> tariffs = Arrays.stream(dependencies.get("forTariffs"))
                .map(s -> tariffService.loadByKey(Integer.parseInt(s)))
                .collect(Collectors.toSet());
        option.setPossibleTariffsOfOption(tariffs);

        String[] requiredFrom = dependencies.get("requiredFrom");
        String[] requiredMe = dependencies.get("requiredMe");
        String[] forbiddenWith = dependencies.get("forbiddenWith");

        EntityGraph<Option> graph = getEntityGraph();
        graph.addAttributeNodes("required", "forbidden");
        Map<String, Object> hints = new HashMap<>();
        hints.put("javax.persistence.loadgraph", graph);

        for (String reqF : requiredFrom) {
            Integer reqFId = Integer.parseInt(reqF);

            Option reqFOpt = optionDao.readWithDependencies(reqFId, hints);
            option.addRequiredFromOptions(reqFOpt);
            option.addRequiredFromOptions(reqFOpt.getRequired());
            option.addForbiddenWithOptions(reqFOpt.getForbidden());
        }

        graph = getEntityGraph();
        graph.addAttributeNodes("required", "forbidden");
        hints.put("javax.persistence.loadgraph", graph);
        for (String reqM : forbiddenWith) {
            Integer forbId = Integer.parseInt(reqM);

            Option forbOpt = optionDao.readWithDependencies(forbId, hints);
            option.addForbiddenWithOptions(forbOpt);
            option.addForbiddenWithOptions(forbOpt.getRequired());
            option.addForbiddenWithOptions(forbOpt.getRequiredMe());
        }

        graph = getEntityGraph();
        graph.addAttributeNodes("requiredMe");
        hints.put("javax.persistence.loadgraph", graph);
        for (String reqM : requiredMe) {
            Integer reqMId = Integer.parseInt(reqM);

            Option reqMOpt = optionDao.readWithDependencies(reqMId, hints);
            option.addRequiredMeOptions(reqMOpt);
            option.addRequiredMeOptions(reqMOpt.getRequiredMe());
        }

        option = optionDao.create(option);
        transaction.commit();
        return option;
    }

    @Override
    public List<Option> loadOptionsByTariffs(List<Integer> tariffs) {
        return optionDao.getOptionsOfTariffs(tariffs);
    }

    @Override
    public Set<Option> loadOptionsByIds(List<Integer> ids) {
        return ids.stream().map(optionDao::read).collect(Collectors.toSet());
    }
}
