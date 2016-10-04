package com.tsystems.javaschool.db.repository;

import com.tsystems.javaschool.db.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by alex on 04.10.16.
 */
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByEmail(String email);
}
