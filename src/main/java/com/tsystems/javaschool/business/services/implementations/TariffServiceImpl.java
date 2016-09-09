package com.tsystems.javaschool.business.services.implementations;

import com.tsystems.javaschool.business.services.interfaces.OptionService;
import com.tsystems.javaschool.business.services.interfaces.TariffService;
import com.tsystems.javaschool.db.entities.Tariff;
import com.tsystems.javaschool.db.implemetations.OptionDaoImpl;
import com.tsystems.javaschool.db.implemetations.TariffDaoImpl;
import com.tsystems.javaschool.db.interfaces.TariffDao;
import com.tsystems.javaschool.util.EMU;

import javax.persistence.EntityGraph;
import javax.persistence.EntityTransaction;
import java.util.List;
import java.util.Map;

/**
 * Created by alex on 21.08.16.
 */
public class TariffServiceImpl implements TariffService{

    private TariffDao tariffDao = TariffDaoImpl.getInstance();

    private TariffServiceImpl() {}

    private static class TariffServiceHolder {
        private final static TariffServiceImpl instance = new TariffServiceImpl();
    }

    public static TariffServiceImpl getInstance() {
        return TariffServiceHolder.instance;
    }

    @Override
    public void addNew(Tariff tariff) {
        try {
            EMU.beginTransaction();
            tariffDao.create(tariff);
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
    public List<Tariff> getNEntries(int maxResult, int firstResult) {
        List<Tariff> tariffs = tariffDao.selectFromTo(maxResult, firstResult);
        EMU.closeEntityManager();
        return tariffs;
    }

    @Override
    public long countOfEntries() {
        long res = tariffDao.countOfEntities();
        EMU.closeEntityManager();
        return res;
    }

    @Override
    public List<Tariff> getNEntries(int maxEntries, int firstIndex, String searchQuery) {
        if ("".equals(searchQuery))
            return getNEntries(maxEntries, firstIndex);
        List<Tariff> tariffs = tariffDao.importantSearchFromTo(maxEntries, firstIndex, searchQuery);
        EMU.closeEntityManager();
        return tariffs;
    }

    @Override
    public long countOfEntries(String searchQuery) {
        if ("".equals(searchQuery))
            return countOfEntries();
        long res = tariffDao.countOfImportantSearch(searchQuery);
        EMU.closeEntityManager();
        return res;

    }

    @Override
    public List<Tariff> loadAll() {
        List<Tariff> tariffs = tariffDao.getAll();
        EMU.closeEntityManager();
        return tariffs;
    }

    @Override
    public Tariff loadByKey(Integer key) {
        Tariff tariff = tariffDao.read(key);
        EMU.closeEntityManager();
        return tariff;
    }

    @Override
    public EntityGraph getEntityGraph() {
        return tariffDao.getEntityGraph();
    }

    @Override
    public void remove(Integer key) {
        try {
            EMU.beginTransaction();
            tariffDao.delete(key);
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
    public Tariff loadByKey(Integer key, Map<String, Object> hints) {
        Tariff tariff = tariffDao.read(key, hints);
        EMU.closeEntityManager();
        return tariff;
    }

    @Override
    public Tariff addNew(Tariff tariff, List<Integer> optionsIds) {
        try {
            EMU.beginTransaction();
            tariff.setPossibleOptions(OptionDaoImpl.getInstance().loadOptionsByIds(optionsIds));
            tariffDao.create(tariff);
            EMU.commit();
            return tariff;
        } catch (RuntimeException re) {
            if (EMU.getEntityManager() != null && EMU.getEntityManager().isOpen())
                EMU.rollback();
            throw re;
        } finally {
            EMU.closeEntityManager();
        }
    }
}
