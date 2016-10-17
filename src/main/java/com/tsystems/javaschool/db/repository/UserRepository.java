package com.tsystems.javaschool.db.repository;

import com.tsystems.javaschool.db.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by alex on 04.10.16.
 *
 * Repository for users
 */
public interface UserRepository extends JpaRepository<User, Integer> {
    /**
     * Search for user by email
     * @param email email
     * @return user with email
     */
    User findByEmail(String email);
}
