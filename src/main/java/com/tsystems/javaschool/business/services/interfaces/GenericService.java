package com.tsystems.javaschool.business.services.interfaces;

import com.tsystems.javaschool.db.interfaces.GenericDao;

import javax.persistence.EntityGraph;
import java.io.Serializable;
import java.util.List;

/**
 * Created by alex on 23.08.16.
 */
public interface GenericService<T, PK extends Serializable> {
    T addNew(T entity);

    List<T> getNEntries(int maxResult, int firstResult);

    long countOfEntries();

    List<T> getNEntries(int maxEntries, int firstIndex, String searchQuery);

    long countOfEntries(String searchQuery);

    List<T> loadAll();

    T loadByKey(PK key);

    EntityGraph getEntityGraph();
}
