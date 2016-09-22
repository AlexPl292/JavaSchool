package com.tsystems.javaschool.util;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by alex on 22.09.16.
 */
public class StatusResponse {
    private boolean success;
    private Map<String, String> errors = new HashMap<>();

    public StatusResponse() {
    }

    public StatusResponse(BindingResult result) {
        addBindingResult(result);
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

    public void setErrors(Map<String, String> errors) {
        this.errors = errors;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean status) {
        this.success = status;
    }

}
