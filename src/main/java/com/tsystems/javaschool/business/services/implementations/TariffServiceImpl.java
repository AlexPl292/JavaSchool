package com.tsystems.javaschool.business.services.implementations;

import com.tsystems.javaschool.business.services.interfaces.OptionService;
import com.tsystems.javaschool.business.services.interfaces.TariffService;
import com.tsystems.javaschool.db.entities.Tariff;
import com.tsystems.javaschool.db.implemetations.TariffDaoImpl;
import com.tsystems.javaschool.db.interfaces.TariffDao;

import javax.persistence.EntityGraph;
import javax.persistence.EntityTransaction;
import java.util.List;
import java.util.Map;

/**
 * Created by alex on 21.08.16.
 */
public class TariffServiceImpl implements TariffService{

    private TariffDao tariffDao = new TariffDaoImpl();

    @Override
    public Tariff addNew(Tariff tariff) {
        EntityTransaction transaction = tariffDao.getTransaction();
        transaction.begin();
        Tariff newTariff = tariffDao.create(tariff);
        transaction.commit();
        return newTariff;
    }

    @Override
    public List<Tariff> getNEntries(int maxResult, int firstResult) {
        return tariffDao.selectFromTo(maxResult, firstResult);
    }

    @Override
    public long countOfEntries() {
        return tariffDao.countOfEntities();
    }

    @Override
    public List<Tariff> getNEntries(int maxEntries, int firstIndex, String searchQuery) {
        if ("".equals(searchQuery))
            return getNEntries(maxEntries, firstIndex);
        return tariffDao.importantSearchFromTo(maxEntries, firstIndex, searchQuery);
    }

    @Override
    public long countOfEntries(String searchQuery) {
        if ("".equals(searchQuery))
            return countOfEntries();
        return tariffDao.countOfImportantSearch(searchQuery);
    }

    @Override
    public List<Tariff> loadAll() {
        return tariffDao.getAll();
    }

    @Override
    public Tariff loadByKey(Integer key) {
        return tariffDao.read(key);
    }

    @Override
    public EntityGraph getEntityGraph() {
        return tariffDao.getEntityGraph();
    }

    @Override
    public Tariff loadByKey(Integer key, Map<String, Object> hints) {
        return tariffDao.read(key, hints);
    }

    @Override
    public Tariff addNew(Tariff tariff, List<Integer> optionsIds) {
        EntityTransaction transaction = tariffDao.getTransaction();
        OptionService optionService = new OptionServiceImpl();
        transaction.begin();
        tariff.setPossibleOptions(optionService.loadOptionsByIds(optionsIds));
        tariffDao.create(tariff);
        transaction.commit();
        return tariff;
    }
}
