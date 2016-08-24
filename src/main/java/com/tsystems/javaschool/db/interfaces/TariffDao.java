package com.tsystems.javaschool.db.interfaces;

import com.tsystems.javaschool.db.entities.Tariff;

import java.util.List;

/**
 * Created by alex on 21.08.16.
 */
public interface TariffDao extends GenericDao<Tariff, Integer>{
    List<Tariff> selectFromTo(int maxEntries, int firstIndex);

    long countOfTariffs();

    List<Tariff> importantSearchFromTo(int maxEntries, int firstIndex, String importantWhere);

    long countOfImportantSearch(String importantWhere);
}
