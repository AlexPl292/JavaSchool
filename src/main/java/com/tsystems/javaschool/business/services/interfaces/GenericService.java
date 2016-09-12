package com.tsystems.javaschool.business.services.interfaces;

import javax.persistence.EntityGraph;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by alex on 23.08.16.
 * @param <T> Entity of this dao
 * @param <PK> primary key
 */
public interface GenericService<T, PK extends Serializable> {

    /**
     * Add new entity to database
     * @param entity entity to add
     * @return added entity
     */
    void addNew(T entity);

    /**
     * @param key id of entity
     * @return loaded entity
     */
    T loadByKey(PK key);

    /**
     * Used for load or not lazy fetched fields
     * @return entity graph
     */
    EntityGraph getEntityGraph();

    void remove(PK key);

    List<T> load(Map<String, Object> kwargs);

    long count(Map<String, Object> kwargs);

    /**
            * Load all entryes
     * @return all entries
     */
    List<T> loadAll();
}
