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
public class StatusResponse {
    private boolean success = true;
    private Map<String, String> errors = new HashMap<>();

    public StatusResponse() {
    }

    public StatusResponse(BindingResult result) {
        addBindingResult(result);
    }

    public void addBindingResult(BindingResult result) {
        if (result.hasErrors()) {
            success = false;
            for (FieldError error : result.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
        }
    }

    public void addError(String name, String message) {
        success = false;
        errors.put(name, message);
    }

    public Map<String, String> getErrors() {
        return errors;
    }

    public void setErrors(Map<String, String> errors) {
        if (errors != null) {
            success = false;
            this.errors = errors;
        }
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean status) {
        this.success = status;
    }

}
