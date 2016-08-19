package com.tsystems.javaschool.db.implemetations;

import com.tsystems.javaschool.db.EMF;
import com.tsystems.javaschool.db.interfaces.GenericDao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;

/**
 * Created by alex on 18.08.16.
 */
public abstract class GenericDaoImpl<T, PK extends Serializable> implements GenericDao<T, PK>{

    protected EntityManager em = EMF.createEntityManager();

    private Class type;

    public GenericDaoImpl() {
        ParameterizedType pt = (ParameterizedType) getClass().getGenericSuperclass();
        type = (Class) pt.getActualTypeArguments()[0];
    }

    @Override
    public T create(T newInstance) {
        em.getTransaction().begin();
        em.persist(newInstance);
        em.getTransaction().commit();
        return newInstance;
    }

    @Override
    public T read(PK id) {
        return (T) em.find(type, id);
    }

    @Override
    public T update(T transientObject) {
        em.getTransaction().begin();
        em.merge(transientObject);
        em.getTransaction().commit();
        return transientObject;
    }

    @Override
    public void delete(PK id) {
        em.getTransaction().begin();
        em.remove(em.getReference(type, id));
        em.getTransaction().commit();
    }
}
