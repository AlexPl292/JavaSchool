package com.tsystems.javaschool.db.interfaces;

import com.tsystems.javaschool.db.entities.Tariff;

import java.util.List;
import java.util.Map;

/**
 * Created by alex on 21.08.16.
 */
public interface TariffDao extends GenericDao<Tariff, Integer>{
    Tariff readWithDependencies(Integer key, Map<String, Object> hints);
}
