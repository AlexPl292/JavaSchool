package com.tsystems.javaschool.db.repository;

import com.tsystems.javaschool.db.entities.Option;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by alex on 28.09.16.
 */
public interface OptionRepository extends JpaRepository<Option, Integer>{
}
