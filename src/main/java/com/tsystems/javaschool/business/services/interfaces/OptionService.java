package com.tsystems.javaschool.business.services.interfaces;

import com.tsystems.javaschool.db.entities.Option;
import com.tsystems.javaschool.db.interfaces.GenericDao;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by alex on 27.08.16.
 */
public interface OptionService extends GenericService<Option, Integer> {

    Option loadWithDependecies(Integer key, Map<String, Object> hints);

    Option addWithDependencies(Option option, HashMap<String, String[]> dependencies);
}
