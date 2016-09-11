package com.tsystems.javaschool.db.implemetations;

import com.tsystems.javaschool.db.entities.Contract;
import com.tsystems.javaschool.db.interfaces.ContractDao;
import com.tsystems.javaschool.util.EMU;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Map;

/**
 * Created by alex on 24.08.16.
 */
public class ContractDaoImpl extends GenericDaoImpl<Contract, Integer> implements ContractDao {

    private ContractDaoImpl() {}

    private static class ContractDaoHolder {
        private static final ContractDaoImpl instance = new ContractDaoImpl();
    }

    public static ContractDaoImpl getInstance() {
        return ContractDaoHolder.instance;
    }

/*
    @Override
    public List<Contract> selectFromTo(int maxEntries, int firstIndex) {
        return EMU.getEntityManager().createQuery("SELECT NEW Contract(c.id, c.number, c.customer, c.tariff, c.isBlocked) FROM Contract c", Contract.class)
                .setFirstResult(firstIndex)
                .setMaxResults(maxEntries)
                .getResultList();
    }

    @Override
    public long countOfEntities() {
        return (long) EMU.getEntityManager().createQuery("SELECT count(c.id) FROM Contract c").getSingleResult();
    }

    @Override
    public List<Contract> importantSearchFromTo(int maxEntries, int firstIndex, String searchQuery) {
        String query = "SELECT NEW Contract(c.id, c.number, c.customer, c.tariff, c.isBlocked) FROM Contract c WHERE c.number LIKE :first";
        return EMU.getEntityManager().createQuery(query, Contract.class)
                .setParameter("first", "%"+ searchQuery +"%")
                .setFirstResult(firstIndex)
                .setMaxResults(maxEntries)
                .getResultList();
    }

    @Override
    public long countOfImportantSearch(String searchQuery) {
        String query = "SELECT COUNT(c.id) FROM Contract c WHERE c.number LIKE :first";
        return (long) EMU.getEntityManager().createQuery(query)
                .setParameter("first", "%"+ searchQuery +"%")
                .getSingleResult();
    }

    @Override
    public List<Contract> getAll() {
        return EMU.getEntityManager().createQuery("SELECT NEW Contract(c.id, c.number, c.customer, c.tariff, c.isBlocked) FROM Contract c", Contract.class)
                .getResultList();
    }
*/


    @Override
    public List<Contract> read(Map<String, Object> kwargs) {
        String queryStr = "SELECT t FROM Contract t";
        String search = (String) kwargs.get("search");
        Integer maxEntries = (Integer) kwargs.get("maxEntries");
        Integer firstIndex = (Integer) kwargs.get("firstIndex");
        Object graph = kwargs.get("graph");
        if (search != null && !"".equals(search)) {
            queryStr += " WHERE t.number LIKE :first";
        }

        TypedQuery<Contract> query = EMU.getEntityManager().createQuery(queryStr, Contract.class);
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
        String queryStr = "SELECT COUNT(t.id) FROM Contract t";
        String search = (String) kwargs.get("search");
        if (search != null && !"".equals(search)) {
            queryStr += " WHERE t.number LIKE :first";
        }

        Query query = EMU.getEntityManager().createQuery(queryStr);
        if (search != null && !"".equals(search))
            query.setParameter("first", "%"+search+"%");
        return (long) query.getSingleResult();
    }

}
