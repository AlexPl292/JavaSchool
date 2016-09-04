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

import javax.persistence.EntityGraph;
import javax.persistence.EntityTransaction;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by alex on 24.08.16.
 */
public class ContractServiceImpl implements ContractService{

    ContractDao contractDao = new ContractDaoImpl();

    @Override
    public Contract addNew(Contract contract) {
        return contractDao.create(contract);
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

    @Override
    public EntityGraph getEntityGraph() {
        return contractDao.getEntityGraph();
    }

    @Override
    public Contract addOptions(Contract contract, List<Integer> optionIds) {
        OptionDao optionDao = new OptionDaoImpl();
        Set<Option> options = new HashSet<>();
        EntityTransaction transaction = GenericDao.getTransaction();
        transaction.begin();
        for (Integer id : optionIds) {
            options.add(optionDao.read(id));
        }
        contract.setUsedOptions(options);
        transaction.commit();
        return contract;
    }
}
