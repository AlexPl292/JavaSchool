package com.tsystems.javaschool.db.implemetations;

import com.tsystems.javaschool.db.entities.Option;
import com.tsystems.javaschool.db.interfaces.GenericDao;
import com.tsystems.javaschool.db.interfaces.OptionDao;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

/**
 * Created by alex on 25.08.16.
 */
public class OptionDaoImpl extends GenericDaoImpl<Option, Integer> implements OptionDao{

    @Override
    public List<Option> selectFromTo(int maxEntries, int firstIndex) {
        return null;
    }

    @Override
    public long countOfEntities() {
        return 0;
    }

    @Override
    public List<Option> importantSearchFromTo(int maxEntries, int firstIndex, String importantWhere) {
        return null;
    }

    @Override
    public long countOfImportantSearch(String importantWhere) {
        return 0;
    }

    @Override
    public List<Option> getAll() {
        return null;
    }
}
