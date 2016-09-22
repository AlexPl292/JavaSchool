package com.tsystems.javaschool.db.implemetations;

import com.tsystems.javaschool.db.entities.Option;
import com.tsystems.javaschool.db.entities.Tariff;
import com.tsystems.javaschool.db.interfaces.OptionDao;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by alex on 25.08.16.
 *
 * JPA implementation of OptionDao
 */
@Repository
public class OptionDaoImpl implements OptionDao{

    @PersistenceContext
    EntityManager em;

    @Override
    public List<Option> getOptionsOfTariffs(List<Integer> tariffs) {
        return em.createQuery("SELECT distinct o FROM Option o JOIN o.possibleTariffsOfOption t WHERE t.id IN :tariffs GROUP BY o.id, o.name HAVING COUNT(t.id) = :size"
                , Option.class)
                .setParameter("tariffs", tariffs)
                .setParameter("size", (long) tariffs.size())
                .getResultList();
    }

    @Override
    public Set<Option> loadOptionsByIds(List<Integer> ids) {
        return ids.stream().map(this::read).collect(Collectors.toSet());
    }

    @Override
    public void create(Option newInstance) {
        em.persist(newInstance);
    }

    @Override
    public Option read(Integer id) {
        return em.find(Option.class, id);
    }

    @Override
    public Option update(Option transientObject) {
        return em.merge(transientObject);
    }

    @Override
    public void delete(Integer id) {
        em.remove(em.getReference(Option.class, id));
    }

    @Override
    public List<Option> read(Map<String, Object> kwargs) {
        String queryStr = "SELECT t FROM Option t";
        String search = (String) kwargs.get("search");
        Integer maxEntries = (Integer) kwargs.get("maxEntries");
        Integer firstIndex = (Integer) kwargs.get("firstIndex");
        Object graph = kwargs.get("graph");
        if (search != null && !"".equals(search)) {
            queryStr += " WHERE t.name LIKE :first";
        }

        TypedQuery<Option> query = em.createQuery(queryStr, Option.class);
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
        String queryStr = "SELECT COUNT(t.id) FROM Option t";
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
        return em.createEntityGraph(Option.class);
    }

    @Override
    public Option read(Integer key, Map<String, Object> hints) {
        return em.find(Option.class, key, hints);
    }
}
