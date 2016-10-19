package com.tsystems.javaschool.exceptions;

/**
 * Created by alex on 14.10.16.
 *
 * Parent exception for all application specific exceptions
 */
public class JSException extends Exception {
    public JSException() {
    }

    public JSException(String message) {
        super(message);
    }
}
