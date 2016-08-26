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
        return em.createQuery("SELECT c FROM Option c", Option.class)
                .setFirstResult(firstIndex)
                .setMaxResults(maxEntries)
                .getResultList();
    }

    @Override
    public long countOfEntities() {
        return (long) em.createQuery("SELECT COUNT(c.id) FROM Option c").getSingleResult();
    }

    @Override
    public List<Option> importantSearchFromTo(int maxEntries, int firstIndex, String importantWhere) {
        return em.createQuery("SELECT c FROM Option c WHERE c.name LIKE :first", Option.class)
                .setParameter("first", "%"+importantWhere+"%")
                .setFirstResult(firstIndex)
                .setMaxResults(maxEntries)
                .getResultList();
    }

    @Override
    public long countOfImportantSearch(String importantWhere) {
        return (long) em.createQuery("SELECT COUNT(c.id) FROM Option c WHERE c.name LIKE :first")
                .setParameter("first", "%"+importantWhere+"%")
                .getSingleResult();
    }

    @Override
    public List<Option> getAll() {
        return em.createQuery("SELECT c FROM Option c", Option.class)
                .getResultList();
    }
}
