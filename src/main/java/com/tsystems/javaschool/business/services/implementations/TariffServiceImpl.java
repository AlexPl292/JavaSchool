package com.tsystems.javaschool.business.services.implementations;

import com.tsystems.javaschool.business.services.interfaces.TariffService;
import com.tsystems.javaschool.db.entities.Tariff;
import com.tsystems.javaschool.db.implemetations.TariffDaoImpl;
import com.tsystems.javaschool.db.interfaces.TariffDao;

import java.util.List;

/**
 * Created by alex on 21.08.16.
 */
public class TariffServiceImpl implements TariffService{

    private TariffDao tariffDao = new TariffDaoImpl();

    @Override
    public void addNew(Tariff tariff) {
        tariffDao.create(tariff);
    }

    @Override
    public List<Tariff> getNEntries(int maxResult, int firstResult) {
        return tariffDao.selectFromTo(maxResult, firstResult);
    }

    @Override
    public long countOfEntries() {
        return tariffDao.countOfTariffs();
    }
}