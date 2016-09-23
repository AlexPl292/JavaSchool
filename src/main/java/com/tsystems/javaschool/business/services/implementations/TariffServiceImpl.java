package com.tsystems.javaschool.business.services.implementations;

import com.tsystems.javaschool.business.dto.TariffDto;
import com.tsystems.javaschool.business.services.interfaces.TariffService;
import com.tsystems.javaschool.db.entities.Tariff;
import com.tsystems.javaschool.db.interfaces.OptionDao;
import com.tsystems.javaschool.db.interfaces.TariffDao;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityGraph;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by alex on 21.08.16.
 */
@Service
@Transactional
public class TariffServiceImpl implements TariffService{

    private final TariffDao tariffDao;// = TariffDaoImpl.getInstance();
    private final OptionDao optionDao;
    private static final Logger logger = Logger.getLogger(TariffServiceImpl.class);

    @Autowired
    public TariffServiceImpl(TariffDao tariffDao, OptionDao optionDao) {
        this.tariffDao = tariffDao;
        this.optionDao = optionDao;
    }

/*
    private static class TariffServiceHolder {
        private static final TariffServiceImpl instance = new TariffServiceImpl();
        private TariffServiceHolder() {}
    }

    public static TariffServiceImpl getInstance() {
        return TariffServiceHolder.instance;
    }
*/

    @Override
    public void addNew(TariffDto tariffDto) {
        Tariff tariff = tariffDto.getTariffEntity(optionDao);

        tariffDao.create(tariff);
        logger.info("New tariff is created. Id = "+tariff.getId());
    }

    @Override
    public TariffDto loadByKey(Integer key) {
/*        Tariff tariff = tariffDao.read(key);
        EMU.closeEntityManager();
        return tariff;*/
        return new TariffDto(tariffDao.read(key));
    }

    @Override
    public EntityGraph getEntityGraph() {
        return tariffDao.getEntityGraph();
    }

    @Override
    public void remove(Integer key) {
        tariffDao.delete(key);
        logger.info("Tariff is removed. Id = "+ key);
    }

    @Override
    public List<TariffDto> load(Map<String, Object> kwargs) {
/*        List<Tariff> tariffs = tariffDao.read(kwargs);
        EMU.closeEntityManager();
        return tariffs;*/
        List<Tariff> tariff = tariffDao.read(kwargs);
        List<TariffDto> tariffDtos = new ArrayList<>();
        for (Tariff t:tariff) {
            TariffDto td = new TariffDto(t);
            td.setDependencies(t);
            tariffDtos.add(td);
        }
        return tariffDtos;
    }

    @Override
    public long count(Map<String, Object> kwargs) {
        long count = tariffDao.count(kwargs);
        return count;
    }

    @Override
    public List<TariffDto> loadAll() {
        return load(new HashMap<>());
    }

    @Override
    public TariffDto loadByKey(Integer key, Map<String, Object> hints) {
/*        Tariff tariff = tariffDao.read(key, hints);
        EMU.closeEntityManager();
        return tariff;*/
        Tariff tariff = tariffDao.read(key, hints);
        TariffDto tariffDto = new TariffDto(tariff);
        tariffDto.setDependencies(tariff);
        return tariffDto;
    }

/*    @Override
    @Transactional
    public Tariff addNew(Tariff tariff, List<Integer> optionsIds) {
        try {
//            EMU.beginTransaction();
            tariff.setPossibleOptionsEntities(OptionDaoImpl.getInstance().loadOptionsByIds(optionsIds));
            tariffDao.create(tariff);
//            EMU.commit();
            logger.info("New tariff with options is created. Id = "+tariff.getId());
            return tariff;
        } catch (RuntimeException re) {
//            if (EMU.getEntityManager() != null && EMU.getEntityManager().isOpen())
//                EMU.rollback();
            throw re;
        } finally {
//            EMU.closeEntityManager();
        }
    }*/
}
