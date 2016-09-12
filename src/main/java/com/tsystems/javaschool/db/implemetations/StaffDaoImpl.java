package com.tsystems.javaschool.db.implemetations;

import com.tsystems.javaschool.db.entities.Staff;
import com.tsystems.javaschool.db.interfaces.StaffDao;
import com.tsystems.javaschool.util.EMU;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Map;

/**
 * Created by alex on 10.09.16.
 */
public class StaffDaoImpl extends GenericDaoImpl<Staff, Integer> implements StaffDao {

    private StaffDaoImpl() {}

    private static class StaffDaoHolder {
        private static final StaffDaoImpl instance = new StaffDaoImpl();
        private StaffDaoHolder() {}
    }

    public static StaffDaoImpl getInstance() {
        return StaffDaoHolder.instance;
    }

    @Override
    public Staff readByEmail(String email) {
        return  EMU.getEntityManager().createQuery("SELECT c FROM Staff c WHERE c.email = :first", Staff.class)
                .setParameter("first", email)
                .getSingleResult();
    }

    @Override
    public List<Staff> read(Map<String, Object> kwargs) {
        String queryStr = "SELECT c FROM Staff c";
        String search = (String) kwargs.get("search");
        Integer maxEntries = (Integer) kwargs.get("maxEntries");
        Integer firstIndex = (Integer) kwargs.get("firstIndex");
        Object graph = kwargs.get("graph");
        String[] where = null;
        if (search != null && !"".equals(search)) {
            where = search.split("\\s+");
            if (where.length == 1) {
                queryStr += " WHERE c.name LIKE :first OR c.surname LIKE :first";
            } else {
                queryStr += " WHERE c.name LIKE :first AND c.surname LIKE :second OR c.name LIKE :second AND c.surname LIKE :first";
            }
        }

        TypedQuery<Staff> query = EMU.getEntityManager().createQuery(queryStr, Staff.class);
        if (where != null) {
            if (where.length == 1) {
                query.setParameter("first", where[0]);
            } else {
                query.setParameter("first", where[0]);
                query.setParameter("second", where[1]);
            }
        }
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
        String queryStr = "SELECT COUNT(c.id) FROM Staff c";
        String search = (String) kwargs.get("search");
        String[] where = null;
        if (search != null && !"".equals(search)) {
            where = search.split("\\s+");
            if (where.length == 1) {
                queryStr += " WHERE c.name LIKE :first OR c.surname LIKE :first";
            } else {
                queryStr += " WHERE c.name LIKE :first AND c.surname LIKE :second OR c.name LIKE :second AND c.surname LIKE :first";
            }
        }

        Query query = EMU.getEntityManager().createQuery(queryStr);
        if (where != null) {
            if (where.length == 1) {
                query.setParameter("first", where[0]);
            } else {
                query.setParameter("first", where[0]);
                query.setParameter("second", where[1]);
            }
        }
        return (long) query.getSingleResult();
    }
}
