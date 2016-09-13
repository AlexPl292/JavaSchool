package com.tsystems.javaschool.business.services.implementations;

import com.tsystems.javaschool.business.services.interfaces.TariffService;
import com.tsystems.javaschool.db.entities.Tariff;
import com.tsystems.javaschool.db.implemetations.OptionDaoImpl;
import com.tsystems.javaschool.db.implemetations.TariffDaoImpl;
import com.tsystems.javaschool.db.interfaces.TariffDao;
import com.tsystems.javaschool.util.EMU;
import org.apache.log4j.Logger;

import javax.persistence.EntityGraph;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by alex on 21.08.16.
 */
public class TariffServiceImpl implements TariffService{

    private TariffDao tariffDao = TariffDaoImpl.getInstance();
    private final static Logger logger = Logger.getLogger(TariffServiceImpl.class);

    private TariffServiceImpl() {}

    private static class TariffServiceHolder {
        private final static TariffServiceImpl instance = new TariffServiceImpl();
        private TariffServiceHolder() {}
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
            logger.info("New tariff is created. Id = "+tariff.getId());
        } catch (RuntimeException re) {
            if (EMU.getEntityManager() != null && EMU.getEntityManager().isOpen())
                EMU.rollback();
            throw re;
        } finally {
            EMU.closeEntityManager();
        }
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
            logger.info("Tariff is removed. Id = "+ key);
        } catch (RuntimeException re) {
            if (EMU.getEntityManager() != null && EMU.getEntityManager().isOpen())
                EMU.rollback();
            throw re;
        } finally {
            EMU.closeEntityManager();
        }
    }

    @Override
    public List<Tariff> load(Map<String, Object> kwargs) {
        List<Tariff> tariffs = tariffDao.read(kwargs);
        EMU.closeEntityManager();
        return tariffs;
    }

    @Override
    public long count(Map<String, Object> kwargs) {
        long count = tariffDao.count(kwargs);
        EMU.closeEntityManager();
        return count;
    }

    @Override
    public List<Tariff> loadAll() {
        return load(new HashMap<>());
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
            logger.info("New tariff with options is created. Id = "+tariff.getId());
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
