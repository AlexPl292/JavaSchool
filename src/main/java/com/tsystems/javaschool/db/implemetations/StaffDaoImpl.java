package com.tsystems.javaschool.db.implemetations;

import com.tsystems.javaschool.db.entities.Staff;
import com.tsystems.javaschool.db.interfaces.StaffDao;
import com.tsystems.javaschool.util.EMU;

import java.util.List;

/**
 * Created by alex on 10.09.16.
 */
public class StaffDaoImpl extends GenericDaoImpl<Staff, Integer> implements StaffDao {

    private StaffDaoImpl() {}

    private static class StaffDaoHolder {
        private static final StaffDaoImpl instance = new StaffDaoImpl();
    }

    public static StaffDaoImpl getInstance() {
        return StaffDaoHolder.instance;
    }

    @Override
    public List<Staff> selectFromTo(int maxEntries, int firstIndex) {
        return null;
    }

    @Override
    public long countOfEntities() {
        return 0;
    }

    @Override
    public List<Staff> importantSearchFromTo(int maxEntries, int firstIndex, String searchQuery) {
        return null;
    }

    @Override
    public long countOfImportantSearch(String searchQuery) {
        return 0;
    }

    @Override
    public List<Staff> getAll() {
        return null;
    }

    @Override
    public Staff readByEmail(String email) {
        return  EMU.getEntityManager().createQuery("SELECT c FROM Staff c WHERE c.email = :first", Staff.class)
                .setParameter("first", email)
                .getSingleResult();
    }
}
