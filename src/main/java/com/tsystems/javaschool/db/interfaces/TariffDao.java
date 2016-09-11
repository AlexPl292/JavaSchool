package com.tsystems.javaschool.db.interfaces;

import com.tsystems.javaschool.db.entities.Tariff;

import java.util.Map;

/**
 * Created by alex on 21.08.16.
 *
 * Search field is name
 */
public interface TariffDao extends GenericDao<Tariff, Integer>{
/*    *//**
     * Load by key with lazy fetched fields
     * @param key id of entity
     * @param hints entity graph with fields
     * @return entity
     *//*
    Tariff read(Integer key, Map<String, Object> hints);*/
}
