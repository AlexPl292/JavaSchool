package com.tsystems.javaschool.business.services.interfaces;

import com.tsystems.javaschool.db.entities.Tariff;

import java.util.List;
import java.util.Map;

/**
 * Created by alex on 21.08.16.
 */
public interface TariffService extends GenericService<Tariff, Integer> {
    Tariff loadByKey(Integer key, Map<String, Object> dependencies);
    Tariff addNew(Tariff tariff, List<Integer> optionsIds);
}
