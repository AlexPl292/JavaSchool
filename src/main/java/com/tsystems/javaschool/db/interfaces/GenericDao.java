package com.tsystems.javaschool.db.interfaces;

import javax.persistence.EntityGraph;
import javax.persistence.EntityTransaction;
import java.io.Serializable;
import java.util.List;

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

    /**
     * Return entities with pagination
     * @param maxEntries count of entries
     * @param firstIndex first index of entity (index != id)
     * @return list of entities between (firstIndex) - (firstIndex+maxEntries)
     */
    List<T> selectFromTo(int maxEntries, int firstIndex);

    /**
     * @return count of all entries
     */
    long countOfEntities();

    /**
     * Return pagination query with filter by "important" field
     * Which field is important is described in concrete dao interface JavaDoc
     * @param maxEntries count entries
     * @param firstIndex first index of entity (index != id)
     * @param searchQuery query. Could contain one or two words. Order is not important
     * @return list of entities between (firstIndex) - (firstIndex+maxEntries) those match searchQuery
     */
    List<T> importantSearchFromTo(int maxEntries, int firstIndex, String searchQuery);

    /**
     * Return count of entries with searchQuery
     * Which field is important is described in concrete dao interface JavaDoc
     * @param searchQuery query. Could contain one or two words. Order is not important
     * @return list of entities those match searchQuery
     */
    long countOfImportantSearch(String searchQuery);

    /**
     * @return all entries
     */
    List<T> getAll();

    /**
     * Used for load or not lazy fetched fields
     * @return entity graph
     */
    EntityGraph getEntityGraph();
}
