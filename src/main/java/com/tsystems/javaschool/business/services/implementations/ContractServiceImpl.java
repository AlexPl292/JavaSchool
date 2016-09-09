package com.tsystems.javaschool.business.services.implementations;

import com.tsystems.javaschool.business.services.interfaces.ContractService;
import com.tsystems.javaschool.business.services.interfaces.GenericService;
import com.tsystems.javaschool.business.services.interfaces.OptionService;
import com.tsystems.javaschool.db.entities.Contract;
import com.tsystems.javaschool.db.entities.Option;
import com.tsystems.javaschool.db.implemetations.ContractDaoImpl;
import com.tsystems.javaschool.db.implemetations.OptionDaoImpl;
import com.tsystems.javaschool.db.interfaces.ContractDao;
import com.tsystems.javaschool.db.interfaces.GenericDao;
import com.tsystems.javaschool.db.interfaces.OptionDao;
import com.tsystems.javaschool.util.EMU;

import javax.persistence.EntityGraph;
import javax.persistence.EntityTransaction;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by alex on 24.08.16.
 */
public class ContractServiceImpl implements ContractService{

    ContractDao contractDao = ContractDaoImpl.getInstance();

    private ContractServiceImpl() {}

    private static class ContractServiceHolder {
        private static final ContractServiceImpl instance = new ContractServiceImpl();
    }

    public static ContractServiceImpl getInstance() {
        return ContractServiceHolder.instance;
    }

    @Override
    public void addNew(Contract contract) {
        try {
            EMU.beginTransaction();
            contractDao.create(contract);
            EMU.commit();
        } catch (RuntimeException re) {
            if (EMU.getEntityManager() != null && EMU.getEntityManager().isOpen())
                EMU.rollback();
            throw re;
        } finally {
            EMU.closeEntityManager();
        }
    }

    @Override
    public List<Contract> getNEntries(int maxResult, int firstResult) {
        List<Contract> contracts = contractDao.selectFromTo(maxResult, firstResult);
        EMU.closeEntityManager();
        return contracts;
    }

    @Override
    public long countOfEntries() {
        long res =  contractDao.countOfEntities();
        EMU.closeEntityManager();
        return res;
    }

    @Override
    public List<Contract> getNEntries(int maxEntries, int firstIndex, String searchQuery) {
        if ("".equals(searchQuery))
            return getNEntries(maxEntries, firstIndex);
        List<Contract> contracts = contractDao.importantSearchFromTo(maxEntries, firstIndex, searchQuery);
        EMU.closeEntityManager();
        return contracts;
    }

    @Override
    public long countOfEntries(String searchQuery) {
        if ("".equals(searchQuery))
            return countOfEntries();
        long res = contractDao.countOfImportantSearch(searchQuery);
        EMU.closeEntityManager();
        return res;
    }

    @Override
    public List<Contract> loadAll() {
        List<Contract> contracts = contractDao.getAll();
        EMU.closeEntityManager();
        return contracts;
    }

    @Override
    public Contract loadByKey(Integer key) {
        Contract contract = contractDao.read(key);
        EMU.closeEntityManager();
        return contract;
    }

    @Override
    public EntityGraph getEntityGraph() {
        return contractDao.getEntityGraph();
    }

    @Override
    public void remove(Integer key) {
        try {
            EMU.beginTransaction();
            contractDao.delete(key);
            EMU.commit();
        } catch (RuntimeException re) {
            if (EMU.getEntityManager() != null && EMU.getEntityManager().isOpen())
                EMU.rollback();
            throw re;
        } finally {
            EMU.closeEntityManager();
        }
    }

    @Override
    public Contract addNew(Contract contract, List<Integer> optionsIds) {
        try {
            EMU.beginTransaction();
            contract.setUsedOptions(OptionDaoImpl.getInstance().loadOptionsByIds(optionsIds));
            contractDao.create(contract);
            EMU.commit();
            return contract;
        } catch (RuntimeException re) {
            if (EMU.getEntityManager() != null && EMU.getEntityManager().isOpen())
                EMU.rollback();
            throw re;
        } finally {
            EMU.closeEntityManager();
        }
    }

    @Override
    public Contract loadByKey(Integer key, Map<String, Object> hints) {
        Contract contract = contractDao.read(key, hints);
        EMU.closeEntityManager();
        return contract;
    }

    @Override
    public void setBlock(Integer id, Integer blockLevel) {
        try {
            EMU.beginTransaction();
            Contract contract = contractDao.read(id);
            contract.setIsBlocked(blockLevel);
            EMU.commit();
        } catch (RuntimeException re) {
            if (EMU.getEntityManager() != null && EMU.getEntityManager().isOpen())
                EMU.rollback();
            throw re;
        } finally {
            EMU.closeEntityManager();
        }
    }
}
