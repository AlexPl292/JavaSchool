package com.tsystems.javaschool.util;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by alex on 22.09.16.
 * Class for status returning for post requests.
 * Default success == true. It changes to false on:
 * - addBingingResult with errors
 * - Add error
 * - Set errors (not null)
 */
public class ErrorResponse {
    private Map<String, String> errors = new HashMap<>();

    public ErrorResponse() {
    }

    public ErrorResponse(BindingResult result) {
        addBindingResult(result);
    }

    public ErrorResponse(String name, String message) {
        errors.put(name, message);
    }

    public void addBindingResult(BindingResult result) {
        if (result.hasErrors()) {
            for (FieldError error : result.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
        }
    }

    public void addError(String name, String message) {
        errors.put(name, message);
    }

    public Map<String, String> getErrors() {
        return errors;
    }

    @Override
    public String toString() {
        return "ErrorResponse{" +
                "errors=" + errors +
                '}';
    }
}
