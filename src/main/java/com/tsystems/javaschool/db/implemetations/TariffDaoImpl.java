package com.tsystems.javaschool.db.implemetations;

import com.tsystems.javaschool.db.entities.Tariff;
import com.tsystems.javaschool.db.interfaces.TariffDao;
import com.tsystems.javaschool.util.EMU;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
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

/*    @Override
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
    }*/

/*    @Override
    public Tariff read(Integer key, Map<String, Object> hints) {
        return EMU.getEntityManager().find(Tariff.class, key, hints);
    }*/

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

        TypedQuery<Tariff> query = EMU.getEntityManager().createQuery(queryStr, Tariff.class);
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

        Query query = EMU.getEntityManager().createQuery(queryStr);
        if (search != null && !"".equals(search))
            query.setParameter("first", "%"+search+"%");
        return (long) query.getSingleResult();
    }
}
