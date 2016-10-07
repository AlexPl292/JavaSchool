package com.tsystems.javaschool.db.repository;

import com.tsystems.javaschool.db.entities.Option;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by alex on 28.09.16.
 */
public interface OptionRepository extends JpaRepository<Option, Integer> {
    List<Option> findByName(String name);
}
