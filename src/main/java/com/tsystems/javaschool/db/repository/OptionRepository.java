package com.tsystems.javaschool.db.repository;

import com.tsystems.javaschool.db.entities.Option;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by alex on 28.09.16.
 *
 * Repository for options
 */
public interface OptionRepository extends JpaRepository<Option, Integer> {
    /**
     * Search for option by name
     * @param name name
     * @return option with name
     */
    Option findByName(String name);
}
