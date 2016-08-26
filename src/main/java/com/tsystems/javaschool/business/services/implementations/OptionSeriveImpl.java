package com.tsystems.javaschool.business.services.implementations;

import com.tsystems.javaschool.business.services.interfaces.OptionService;
import com.tsystems.javaschool.db.entities.Option;
import com.tsystems.javaschool.db.implemetations.OptionDaoImpl;
import com.tsystems.javaschool.db.interfaces.OptionDao;

import java.util.List;

/**
 * Created by alex on 27.08.16.
 */
public class OptionSeriveImpl implements OptionService{

    private OptionDao optionDao = new OptionDaoImpl();
    @Override
    public Option addNew(Option entity) {
        return optionDao.create(entity);
    }

    @Override
    public List<Option> getNEntries(int maxResult, int firstResult) {
        return optionDao.selectFromTo(maxResult, firstResult);
    }

    @Override
    public long countOfEntries() {
        return optionDao.countOfEntities();
    }

    @Override
    public List<Option> getNEntries(int maxEntries, int firstIndex, String searchQuery) {
        if (searchQuery == null || "".equals(searchQuery))
            return getNEntries(maxEntries, firstIndex);
        return optionDao.importantSearchFromTo(maxEntries, firstIndex, searchQuery);
    }

    @Override
    public long countOfEntries(String searchQuery) {
        if (searchQuery == null || "".equals(searchQuery))
            return countOfEntries();
        return optionDao.countOfImportantSearch(searchQuery);
    }

    @Override
    public List<Option> loadAll() {
        return optionDao.getAll();
    }

    @Override
    public Option loadByKey(Integer key) {
        return optionDao.read(key);
    }
}
