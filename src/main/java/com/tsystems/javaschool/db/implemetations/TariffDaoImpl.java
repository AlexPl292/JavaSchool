package com.tsystems.javaschool.db.implemetations;

import com.tsystems.javaschool.db.entities.Tariff;
import com.tsystems.javaschool.db.interfaces.TariffDao;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.util.List;
import java.util.Map;

/**
 * Created by alex on 21.08.16.
 *
 * JPA implementation of TariffDao
 */
@Repository
public class TariffDaoImpl implements TariffDao{

    /*    private TariffDaoImpl() {}

        private static class TariffDaoHolder {
            private static final TariffDaoImpl instance = new TariffDaoImpl();
            private TariffDaoHolder() {}
        }

        public static TariffDaoImpl getInstance() {
            return TariffDaoHolder.instance;
        }
        */
    @PersistenceContext
    EntityManager em;

    @Override
    public void create(Tariff newInstance) {
        em.persist(newInstance);
    }

    @Override
    public Tariff read(Integer id) {
        return em.find(Tariff.class, id);
    }

    @Override
    public Tariff update(Tariff transientObject) {
        return em.merge(transientObject);
    }

    @Override
    public void delete(Integer id) {
        em.remove(em.getReference(Tariff.class, id));
    }

    @Override
    public List<Tariff> read(Map<String, Object> kwargs) {
        String queryStr = "SELECT t FROM Tariff t";
        String search = (String) kwargs.get("search");
        Integer maxEntries = (Integer) kwargs.get("maxEntries");
        Integer firstIndex = (Integer) kwargs.get("firstIndex");
        Object graph = kwargs.get("graph");
        if (search != null && !"".equals(search)) {
            queryStr += " WHERE t.name LIKE :first";
        }

        TypedQuery<Tariff> query = em.createQuery(queryStr, Tariff.class);
        if (search != null && !"".equals(search))
            query.setParameter("first", "%"+search+"%");
        if (maxEntries != null)
            query.setMaxResults(maxEntries);
        if (firstIndex != null)
            query.setFirstResult(firstIndex);
        if (graph != null)
            query.setHint("javax.persistence.loadgraph", graph);
        return query.getResultList();
    }

    @Override
    public long count(Map<String, Object> kwargs) {
        String queryStr = "SELECT COUNT(t.id) FROM Tariff t";
        String search = (String) kwargs.get("search");
        if (search != null && !"".equals(search)) {
            queryStr += " WHERE t.name LIKE :first";
        }

        Query query = em.createQuery(queryStr);
        if (search != null && !"".equals(search))
            query.setParameter("first", "%"+search+"%");
        return (long) query.getSingleResult();
    }

    @Override
    public EntityGraph getEntityGraph() {
        return em.createEntityGraph(Tariff.class);
    }

    @Override
    public Tariff read(Integer key, Map<String, Object> hints) {
        return em.find(Tariff.class, key, hints);
    }
}
