package com.tsystems.javaschool.db.implemetations;

import com.tsystems.javaschool.db.entities.Tariff;
import com.tsystems.javaschool.db.interfaces.TariffDao;

import java.util.List;

/**
 * Created by alex on 21.08.16.
 */
public class TariffDaoImpl extends GenericDaoImpl<Tariff, Integer> implements TariffDao{
    @Override
    public List<Tariff> selectFromTo(int maxEntries, int firstIndex) {
        return em.createQuery("SELECT NEW Tariff(c.name, c.cost, c.description) FROM Tariff c", Tariff.class)
                .setFirstResult(firstIndex)
                .setMaxResults(maxEntries)
                .getResultList();
    }

    @Override
    public long countOfTariffs() {
        return (long) em.createQuery("SELECT count(c.id) FROM Tariff c").getSingleResult();
    }
}
