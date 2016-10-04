package com.tsystems.javaschool.business.services.implementations;

import com.tsystems.javaschool.business.services.interfaces.UserService;
import com.tsystems.javaschool.db.entities.User;
import com.tsystems.javaschool.db.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by alex on 04.10.16.
 */
@Service
@Transactional
public class UserServiceImpl implements UserService{

    public final UserRepository repository;

    @Autowired
    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public User findByEmail(String email) {
        return repository.findByEmail(email);
    }
}
