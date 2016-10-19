package com.tsystems.javaschool.util;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by alex on 22.09.16.
 *
 * Class for errors returning for post requests.
 */
public class ErrorResponse {
    private Map<String, String> errors = new HashMap<>();

    public ErrorResponse() {
    }

    /**
     * Create object from BindingResult
     * @param result bindingResult
     */
    public ErrorResponse(BindingResult result) {
        addBindingResult(result);
    }

    /**
     * Create object with one error
     * @param name name of error
     * @param message message
     */
    public ErrorResponse(String name, String message) {
        errors.put(name, message);
    }

    /**
     * Fill errors from BindingResult
     * @param result bingindResulg
     */
    public void addBindingResult(BindingResult result) {
        if (result.hasErrors()) {
            for (FieldError error : result.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
        }
    }

    /**
     * Add error to list
     * @param name error name
     * @param message error message
     */
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
