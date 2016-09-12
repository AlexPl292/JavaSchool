package com.tsystems.javaschool.db.interfaces;

import javax.persistence.EntityGraph;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by alex on 18.08.16.
 * @param <T> Entity of this dao
 * @param <PK> primary key
 */
public interface GenericDao <T, PK extends Serializable> {

    /**
     * Persist new entity
     * @param newInstance entity to persist
     */
    void create(T newInstance);

    /**
     * Find entity by id
     * @param id id of entity
     * @return found entity
     */
    T read(PK id);

    /**
     * Update an entity
     * @param transientObject new entity
     * @return updated entity
     */
    T update(T transientObject);

    /**
     * Delete entity
     * @param id id of entity to delete
     */
    void delete(PK id);

    List<T> read(Map<String, Object> kwargs);

    long count(Map<String, Object> kwargs);

    /**
     * Used for load or not lazy fetched fields
     * @return entity graph
     */
    EntityGraph getEntityGraph();

    /**
     * Load by key with lazy fetched fields
     * @param key id of entity
     * @param hints entity graph with fields
     * @return entity
     */
    T read(Integer key, Map<String, Object> hints);
}
