package com.tsystems.javaschool.business.services.implementations;

import com.tsystems.javaschool.business.services.interfaces.TariffService;
import com.tsystems.javaschool.db.entities.Tariff;
import com.tsystems.javaschool.db.implemetations.TariffDaoImpl;
import com.tsystems.javaschool.db.interfaces.TariffDao;

import javax.persistence.EntityGraph;
import java.util.List;

/**
 * Created by alex on 21.08.16.
 */
public class TariffServiceImpl implements TariffService{

    private TariffDao tariffDao = new TariffDaoImpl();

    @Override
    public Tariff addNew(Tariff tariff) {
        return tariffDao.create(tariff);
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
}
