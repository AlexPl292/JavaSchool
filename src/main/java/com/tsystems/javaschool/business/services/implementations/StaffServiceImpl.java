package com.tsystems.javaschool.business.services.implementations;

import com.tsystems.javaschool.business.services.interfaces.StaffService;
import com.tsystems.javaschool.db.entities.Staff;
import com.tsystems.javaschool.db.implemetations.StaffDaoImpl;
import com.tsystems.javaschool.db.interfaces.StaffDao;
import com.tsystems.javaschool.util.EMU;
import org.apache.commons.codec.digest.DigestUtils;

import javax.persistence.EntityGraph;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by alex on 10.09.16.
 */
public class StaffServiceImpl  implements StaffService {

    private StaffDao staffDao = StaffDaoImpl.getInstance();

    private StaffServiceImpl() {}

    private static class StaffServiceHolder {
        private final static StaffServiceImpl instance = new StaffServiceImpl();
    }

    public static StaffServiceImpl getInstance() {
        return StaffServiceHolder.instance;
    }

    @Override
    public Staff login(String email, String password) {
        Staff staff = staffDao.readByEmail(email);
        if (staff == null)
            return null;

        String passwordHash = DigestUtils.sha256Hex(password);
        passwordHash = DigestUtils.sha256Hex(passwordHash + staff.getSalt());
        if (!passwordHash.equals(staff.getPassword()))
            return null;
        EMU.closeEntityManager();
        return staff;
    }

    @Override
    public void addNew(Staff entity) {

    }

/*
    @Override
    public List<Staff> getNEntries(int maxResult, int firstResult) {
        return null;
    }

    @Override
    public long countOfEntries() {
        return 0;
    }

    @Override
    public List<Staff> getNEntries(int maxEntries, int firstIndex, String searchQuery) {
        return null;
    }

    @Override
    public long countOfEntries(String searchQuery) {
        return 0;
    }

    @Override
    public List<Staff> loadAll() {
        return null;
    }
*/

    @Override
    public Staff loadByKey(Integer key) {
        return null;
    }

    @Override
    public EntityGraph getEntityGraph() {
        return null;
    }

    @Override
    public void remove(Integer key) {

    }

    @Override
    public List<Staff> load(Map<String, Object> kwargs) {
        List<Staff> staff = staffDao.read(kwargs);
        EMU.closeEntityManager();
        return staff;
    }

    @Override
    public long count(Map<String, Object> kwargs) {
        long count = staffDao.count(kwargs);
        EMU.closeEntityManager();
        return count;
    }

    @Override
    public List<Staff> loadAll() {
        return load(new HashMap<>());
    }
}
