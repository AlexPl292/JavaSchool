package com.tsystems.javaschool.db.implemetations;

import com.tsystems.javaschool.db.entities.Tariff;
import com.tsystems.javaschool.db.interfaces.TariffDao;

import java.util.List;
import java.util.Map;

/**
 * Created by alex on 21.08.16.
 *
 * JPA implementation of TariffDao
 */
public class TariffDaoImpl extends GenericDaoImpl<Tariff, Integer> implements TariffDao{
    @Override
    public List<Tariff> selectFromTo(int maxEntries, int firstIndex) {
        return em.createQuery("SELECT NEW Tariff(c.id, c.name, c.cost, c.description) FROM Tariff c", Tariff.class)
                .setFirstResult(firstIndex)
                .setMaxResults(maxEntries)
                .getResultList();
    }

    @Override
    public long countOfEntities() {
        return (long) em.createQuery("SELECT count(c.id) FROM Tariff c").getSingleResult();
    }

    @Override
    public List<Tariff> importantSearchFromTo(int maxEntries, int firstIndex, String searchQuery) {
        String query = "SELECT NEW Tariff(c.id, c.name, c.cost, c.description) FROM Tariff c WHERE c.name LIKE :first";
        return em.createQuery(query, Tariff.class)
                .setParameter("first", "%"+ searchQuery +"%")
                .setFirstResult(firstIndex)
                .setMaxResults(maxEntries)
                .getResultList();
    }

    @Override
    public long countOfImportantSearch(String searchQuery) {
        String query = "SELECT COUNT(c.id) FROM Tariff c WHERE c.name LIKE :first";
        return (long) em.createQuery(query)
                .setParameter("first", "%"+ searchQuery +"%")
                .getSingleResult();
    }

    @Override
    public List<Tariff> getAll() {
        return em.createQuery("SELECT NEW Tariff(c.id, c.name, c.cost, c.description) FROM Tariff c", Tariff.class)
                .getResultList();
    }

    @Override
    public Tariff read(Integer key, Map<String, Object> hints) {
        return em.find(Tariff.class, key, hints);
    }
}
