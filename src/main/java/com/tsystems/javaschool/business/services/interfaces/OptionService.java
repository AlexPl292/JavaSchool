package com.tsystems.javaschool.business.services.interfaces;

import com.tsystems.javaschool.db.entities.Option;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by alex on 27.08.16.
 */
public interface OptionService extends GenericService<Option, Integer> {

    /**
     * Load tariff by key with additional fields
     * @param key id of tariff
     * @param hints entity map with dependencies
     * @return founded tariff
     */
    Option loadByKey(Integer key, Map<String, Object> hints);

    /**
     * Add new option with dependencies
     * @param option option to add
     * @param dependencies dependencies
     * @return added option
     */
    Option addNew(Option option, Map<String, String[]> dependencies);

    /**
     * Load options that contains in all this tariffs.
     * If new option is available for all this tariffs,
     * then it could have references only with options, that contains in all this tariffs.
     * @param tariffs list of tariffs
     * @return list of options
     */
    List<Option> loadOptionsByTariffs(List<Integer> tariffs);

}
