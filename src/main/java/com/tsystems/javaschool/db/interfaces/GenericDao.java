package com.tsystems.javaschool.db.interfaces;

import java.io.Serializable;

/**
 * Created by alex on 18.08.16.
 */
public interface GenericDao <T, PK extends Serializable> {

    T create(T newInstance);

    T read(PK id);

    T update(T transientObject);

    void delete(PK id);
}
