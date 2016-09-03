package com.tsystems.javaschool.db.interfaces;

import com.tsystems.javaschool.db.EMF;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.io.Serializable;
import java.util.List;

/**
 * Created by alex on 18.08.16.
 */
public interface GenericDao <T, PK extends Serializable> {

    EntityManager em = EMF.createEntityManager();

    T create(T newInstance);

    T read(PK id);

    T update(T transientObject);

    void delete(PK id);

    static EntityTransaction getTransaction() {
        return em.getTransaction();
    }

    List<T> selectFromTo(int maxEntries, int firstIndex);

    long countOfEntities();

    List<T> importantSearchFromTo(int maxEntries, int firstIndex, String importantWhere);

    long countOfImportantSearch(String importantWhere);

    List<T> getAll();

    EntityGraph getEntityGraph();
}
