package com.tsystems.javaschool.business.services.interfaces;

import com.tsystems.javaschool.db.entities.User;

/**
 * Created by alex on 04.10.16.
 */
public interface UserService {
    User findByEmail(String email);
}