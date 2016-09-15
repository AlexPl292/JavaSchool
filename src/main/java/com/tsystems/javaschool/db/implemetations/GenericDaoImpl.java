package com.tsystems.javaschool.db.implemetations;

import com.tsystems.javaschool.db.interfaces.GenericDao;
import com.tsystems.javaschool.util.EMU;

import javax.persistence.EntityGraph;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.Map;

/**
 * Created by alex on 18.08.16.
 * @param <T> Entity of this dao
 * @param <PK> primary key
 */
abstract class GenericDaoImpl<T, PK extends Serializable> implements GenericDao<T, PK>{

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
        EMU.getEntityManager().persist(newInstance);
    }

    @Override
    public T read(PK id) {
        return (T) EMU.getEntityManager().find(type, id);
    }

    @Override
    public T update(T transientObject) {
        return EMU.getEntityManager().merge(transientObject);
    }

    @Override
    public void delete(PK id) {
        EMU.getEntityManager().remove(EMU.getEntityManager().getReference(type, id));
    }

    @Override
    public EntityGraph getEntityGraph() {
        return EMU.getEntityManager().createEntityGraph(type);
    }

    @Override
    public T read(Integer key, Map<String, Object> hints) {
        return (T) EMU.getEntityManager().find(type, key, hints);
    }
}
