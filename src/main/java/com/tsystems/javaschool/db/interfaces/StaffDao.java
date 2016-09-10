package com.tsystems.javaschool.db.interfaces;

import com.tsystems.javaschool.db.entities.Staff;

/**
 * Created by alex on 10.09.16.
 */
public interface StaffDao extends GenericDao<Staff, Integer>{

    Staff readByEmail(String email);
}
