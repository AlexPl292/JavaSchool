package com.tsystems.javaschool.business.services.interfaces;

import com.tsystems.javaschool.exceptions.JSException;

import java.io.Serializable;
import java.util.List;

/**
 * Created by alex on 23.08.16.
 *
 * @param <T>  Entity of this dao
 * @param <PK> primary key
 */
public interface GenericService<T, PK extends Serializable> {

    /**
     * Add new entity to database
     *
     * @param entity entity to add
     */
    T addNew(T entity) throws JSException;

    /**
     * Find entity by id
     *
     * @param key id of entity
     * @return loaded entity
     */
    T loadByKey(PK key);

    /**
     * Remove entity by id
     *
     * @param key id of entity
     */
    void remove(PK key);

    /**
     * Load all entries
     *
     * @return all entries
     */
    List<T> loadAll();
}
