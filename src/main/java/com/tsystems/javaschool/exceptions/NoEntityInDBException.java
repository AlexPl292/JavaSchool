package com.tsystems.javaschool.exceptions;

/**
 * Created by alex on 14.10.16.
 *
 * Database don't contains entity
 */
public class NoEntityInDBException extends JSException {
    public NoEntityInDBException() {
    }

    public NoEntityInDBException(String m) {
        super(m);
    }
}
