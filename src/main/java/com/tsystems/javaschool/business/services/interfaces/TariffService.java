package com.tsystems.javaschool.business.services.interfaces;

import com.tsystems.javaschool.business.dto.TariffDto;
import com.tsystems.javaschool.db.entities.Tariff;

import java.util.List;
import java.util.Map;

/**
 * Created by alex on 21.08.16.
 */
public interface TariffService extends GenericService<TariffDto, Integer> {
    /**
     * Load tariff by key with additional fields
     * @param key id of tariff
     * @param dependencies entity map with dependencies
     * @return founded tariff
     */
    TariffDto loadByKey(Integer key, Map<String, Object> dependencies);
}
