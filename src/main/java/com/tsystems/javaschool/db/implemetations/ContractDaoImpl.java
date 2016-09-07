package com.tsystems.javaschool.db.implemetations;

import com.tsystems.javaschool.db.entities.Contract;
import com.tsystems.javaschool.db.interfaces.ContractDao;
import com.tsystems.javaschool.util.EMU;

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

    @Override
    public Contract read(Integer key, Map<String, Object> hints) {
        return EMU.getEntityManager().find(Contract.class, key, hints);
    }

}
