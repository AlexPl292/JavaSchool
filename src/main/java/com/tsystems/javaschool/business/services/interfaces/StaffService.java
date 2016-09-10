package com.tsystems.javaschool.business.services.interfaces;

import com.tsystems.javaschool.db.entities.Staff;

/**
 * Created by alex on 10.09.16.
 */
public interface StaffService extends GenericService<Staff, Integer> {

    Staff login(String email, String password);
}
