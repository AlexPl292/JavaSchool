package com.tsystems.javaschool.business.services.interfaces;

import com.tsystems.javaschool.db.entities.Tariff;

import java.util.List;
import java.util.Map;

/**
 * Created by alex on 21.08.16.
 */
public interface TariffService extends GenericService<Tariff, Integer> {
    /**
     * Load tariff by key with additional fields
     * @param key id of tariff
     * @param dependencies entity map with dependencies
     * @return founded tariff
     */
    Tariff loadByKey(Integer key, Map<String, Object> dependencies);
    /**
     * Add new tariff with options, that are defined by ids
     * @param tariff tariff to add
     * @param optionsIds list of ids of options
     * @return added tariff
     */
    Tariff addNew(Tariff tariff, List<Integer> optionsIds);
}
