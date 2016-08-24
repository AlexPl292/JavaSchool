package com.tsystems.javaschool.business.services.implementations;

import com.tsystems.javaschool.business.services.interfaces.ContractService;
import com.tsystems.javaschool.db.entities.Contract;
import com.tsystems.javaschool.db.implemetations.ContractDaoImpl;
import com.tsystems.javaschool.db.interfaces.ContractDao;

import java.util.List;

/**
 * Created by alex on 24.08.16.
 */
public class ContractServiceImpl implements ContractService{

    ContractDao contractDao = new ContractDaoImpl();

    @Override
    public void addNew(Contract contract) {
        contractDao.create(contract);
    }

    @Override
    public List<Contract> getNEntries(int maxResult, int firstResult) {
        return contractDao.selectFromTo(maxResult, firstResult);
    }

    @Override
    public long countOfEntries() {
        return contractDao.countOfEntities();
    }

    @Override
    public List<Contract> getNEntries(int maxEntries, int firstIndex, String searchQuery) {
        if ("".equals(searchQuery))
            return getNEntries(maxEntries, firstIndex);
        return contractDao.importantSearchFromTo(maxEntries, firstIndex, searchQuery);
    }

    @Override
    public long countOfEntries(String searchQuery) {
        if ("".equals(searchQuery))
            return countOfEntries();
        return contractDao.countOfImportantSearch(searchQuery);
    }

    @Override
    public List<Contract> loadAll() {
        return contractDao.getAll();
    }

    @Override
    public Contract loadByKey(Integer key) {
        return contractDao.read(key);
    }
}
