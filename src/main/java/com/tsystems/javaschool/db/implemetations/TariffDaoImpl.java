package com.tsystems.javaschool.db.implemetations;

import com.tsystems.javaschool.db.entities.Tariff;
import com.tsystems.javaschool.db.interfaces.TariffDao;
import com.tsystems.javaschool.util.EMU;

import java.util.List;
import java.util.Map;

/**
 * Created by alex on 21.08.16.
 *
 * JPA implementation of TariffDao
 */
public class TariffDaoImpl extends GenericDaoImpl<Tariff, Integer> implements TariffDao{

    private TariffDaoImpl() {}

    private static class TariffDaoHolder {
        private static final TariffDaoImpl instance = new TariffDaoImpl();
    }

    public static TariffDaoImpl getInstance() {
        return TariffDaoHolder.instance;
    }

    @Override
    public List<Tariff> selectFromTo(int maxEntries, int firstIndex) {
        return EMU.getEntityManager().createQuery("SELECT NEW Tariff(c.id, c.name, c.cost, c.description) FROM Tariff c", Tariff.class)
                .setFirstResult(firstIndex)
                .setMaxResults(maxEntries)
                .getResultList();
    }

    @Override
    public long countOfEntities() {
        return (long) EMU.getEntityManager().createQuery("SELECT count(c.id) FROM Tariff c").getSingleResult();
    }

    @Override
    public List<Tariff> importantSearchFromTo(int maxEntries, int firstIndex, String searchQuery) {
        String query = "SELECT NEW Tariff(c.id, c.name, c.cost, c.description) FROM Tariff c WHERE c.name LIKE :first";
        return EMU.getEntityManager().createQuery(query, Tariff.class)
                .setParameter("first", "%"+ searchQuery +"%")
                .setFirstResult(firstIndex)
                .setMaxResults(maxEntries)
                .getResultList();
    }

    @Override
    public long countOfImportantSearch(String searchQuery) {
        String query = "SELECT COUNT(c.id) FROM Tariff c WHERE c.name LIKE :first";
        return (long) EMU.getEntityManager().createQuery(query)
                .setParameter("first", "%"+ searchQuery +"%")
                .getSingleResult();
    }

    @Override
    public List<Tariff> getAll() {
        return EMU.getEntityManager().createQuery("SELECT NEW Tariff(c.id, c.name, c.cost, c.description) FROM Tariff c", Tariff.class)
                .getResultList();
    }

    @Override
    public Tariff read(Integer key, Map<String, Object> hints) {
        return EMU.getEntityManager().find(Tariff.class, key, hints);
    }
}
