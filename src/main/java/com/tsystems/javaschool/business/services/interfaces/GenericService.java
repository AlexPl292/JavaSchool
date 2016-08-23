package com.tsystems.javaschool.business.services.interfaces;

import java.util.List;

/**
 * Created by alex on 23.08.16.
 */
public interface GenericService<T> {
    void addNew(T tariff);

    List<T> getNEntries(int maxResult, int firstResult);

    long countOfEntries();
}
