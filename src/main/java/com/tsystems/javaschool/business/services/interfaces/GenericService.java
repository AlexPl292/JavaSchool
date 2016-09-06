package com.tsystems.javaschool.business.services.interfaces;

import javax.persistence.EntityGraph;
import java.io.Serializable;
import java.util.List;

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
    T addNew(T entity);

    /**
     * Return entities with pagination
     * @param maxResult count of entries
     * @param firstResult first index of entity (index != id)
     * @return list of entities between (firstIndex) - (firstIndex+maxEntries)
     */
    List<T> getNEntries(int maxResult, int firstResult);

    /**
     * @return count of all entries
     */
    long countOfEntries();

    /**
     * Return pagination query with filter by search query
     * Which field is important is described in concrete dao interface JavaDoc
     * @param maxEntries count entries
     * @param firstIndex first index of entity (index != id)
     * @param searchQuery query. Could contain one or two words. Order is not important
     * @return list of entities between (firstIndex) - (firstIndex+maxEntries) those match searchQuery
     */
    List<T> getNEntries(int maxEntries, int firstIndex, String searchQuery);

    /**
     * Return count of entries with searchQuery
     * Which field is important is described in concrete dao interface JavaDoc
     * @param searchQuery query. Could contain one or two words. Order is not important
     * @return list of entities those match searchQuery
     */
    long countOfEntries(String searchQuery);

    /**
     * Load all entryes
     * @return all entries
     */
    List<T> loadAll();

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
}
