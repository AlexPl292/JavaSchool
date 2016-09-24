package com.tsystems.javaschool.db.implemetations;

import com.tsystems.javaschool.db.interfaces.GenericDao;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.Map;

/**
 * Created by alex on 18.08.16.
 * @param <T> Entity of this dao
 * @param <PK> primary key
 */
abstract class GenericDaoImpl<T, PK extends Serializable> implements GenericDao<T, PK>{

    @PersistenceContext
    protected EntityManager em;

    private Class type;

    /**
     * Constructor with clever code, that get class type of generic
     */
    GenericDaoImpl() {
        ParameterizedType pt = (ParameterizedType) getClass().getGenericSuperclass();
        type = (Class) pt.getActualTypeArguments()[0];
    }

    @Override
    public void create(T newInstance) {
        em.persist(newInstance);
    }

    @Override
    public T read(PK id) {
        return (T) em.find(type, id);
    }

    @Override
    public T update(T transientObject) {
        return em.merge(transientObject);
    }

    @Override
    public void delete(PK id) {
        em.remove(em.getReference(type, id));
    }

    @Override
    public EntityGraph getEntityGraph() {
        return em.createEntityGraph(type);
    }

    @Override
    public T read(Integer key, Map<String, Object> hints) {
        return (T) em.find(type, key, hints);
    }
}
