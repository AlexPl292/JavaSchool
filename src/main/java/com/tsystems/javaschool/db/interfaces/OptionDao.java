package com.tsystems.javaschool.db.interfaces;

import com.tsystems.javaschool.db.entities.Option;

import java.util.List;
import java.util.Map;

/**
 * Created by alex on 25.08.16.
 */
public interface OptionDao extends GenericDao<Option, Integer> {

    Option readWithDependencies(Integer key, Map<String, Object> hints);
    List<Option> getOptionsOfTariffs(List<Integer> tariffs);
}
