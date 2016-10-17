package com.tsystems.javaschool.exceptions;

/**
 * Created by alex on 14.10.16.
 *
 * Database don't contains entity
 */
public class NoEntityInDB extends JSException {
    public NoEntityInDB() {
    }

    public NoEntityInDB(String m) {
        super(m);
    }
}
