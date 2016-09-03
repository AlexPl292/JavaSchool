package com.tsystems.javaschool.db.implemetations;

import com.tsystems.javaschool.db.EMF;
import com.tsystems.javaschool.db.interfaces.GenericDao;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;

/**
 * Created by alex on 18.08.16.
 */
public abstract class GenericDaoImpl<T, PK extends Serializable> implements GenericDao<T, PK>{

    private Class type;

    public GenericDaoImpl() {
        ParameterizedType pt = (ParameterizedType) getClass().getGenericSuperclass();
        type = (Class) pt.getActualTypeArguments()[0];
    }

    @Override
    public T create(T newInstance) {
        em.persist(newInstance);
        return newInstance;
    }

    @Override
    public T read(PK id) {
        return (T) em.find(type, id);
    }

    @Override
    public T update(T transientObject) {
        em.merge(transientObject);
        return transientObject;
    }

    @Override
    public void delete(PK id) {
        em.remove(em.getReference(type, id));
    }

    @Override
    public EntityGraph getEntityGraph() {
        return em.createEntityGraph(type);
    }
}
