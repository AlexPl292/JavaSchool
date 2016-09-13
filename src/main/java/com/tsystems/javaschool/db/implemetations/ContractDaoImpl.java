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
 *
 * JPA implementation of ContractDao
 */
public class ContractDaoImpl extends GenericDaoImpl<Contract, Integer> implements ContractDao {

    private ContractDaoImpl() {}

    private static class ContractDaoHolder {
        private static final ContractDaoImpl instance = new ContractDaoImpl();
        private ContractDaoHolder() {}
    }

    public static ContractDaoImpl getInstance() {
        return ContractDaoHolder.instance;
    }


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
