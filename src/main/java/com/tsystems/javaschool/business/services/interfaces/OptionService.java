package com.tsystems.javaschool.business.services.interfaces;

import com.tsystems.javaschool.db.entities.Option;
import com.tsystems.javaschool.db.entities.Tariff;
import com.tsystems.javaschool.db.interfaces.GenericDao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by alex on 27.08.16.
 */
public interface OptionService extends GenericService<Option, Integer> {

    Option loadWithDependencies(Integer key, Map<String, Object> hints);

    Option addWithDependencies(Option option, Map<String, String[]> dependencies);

    List<Option> loadOptionsByTariffs(List<Integer> tariffs);

    Set<Option> loadOptionsByIds(List<Integer> ids);
}
