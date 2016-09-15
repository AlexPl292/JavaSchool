package com.tsystems.javaschool.db.interfaces;

import com.tsystems.javaschool.db.entities.Option;

import java.util.List;
import java.util.Set;

/**
 * Created by alex on 25.08.16.
 *
 * Search field is name
 */
public interface OptionDao extends GenericDao<Option, Integer> {

    /**
     * Load options that contains in all this tariffs.
     * If new option is available for all this tariffs,
     * then it could have references only with options, that contains in all this tariffs.
     * @param tariffs list of tariffs
     * @return list of options
     */
    List<Option> getOptionsOfTariffs(List<Integer> tariffs);

    /**
     * Load set of options by ids
     * @param ids List of ids of options
     * @return set of options
     */
    Set<Option> loadOptionsByIds(List<Integer> ids);
}
