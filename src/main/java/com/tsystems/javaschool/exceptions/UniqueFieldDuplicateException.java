package com.tsystems.javaschool.exceptions;

import java.net.URI;

/**
 * Created by alex on 30.09.16.
 *
 * Entity with specified field already exists
 */
public class UniqueFieldDuplicateException extends JSException {

    private String duplicatedField;
    private String duplicatedValue;

    // Rest path to entity. Could be set in location header
    private URI pathToDuplicatedEntity;

    public UniqueFieldDuplicateException() {
    }

    public UniqueFieldDuplicateException(String duplicatedField, String duplicatedValue, String path) {
        // Create exceptions with message
        super("Resource with '" +
                duplicatedField + "' == '" + duplicatedValue +
                "' already exists. This field must be unique. Path: " + path);
        this.duplicatedField = duplicatedField;
        this.duplicatedValue = duplicatedValue;
        this.pathToDuplicatedEntity = URI.create(path);
    }

    public String getDuplicatedField() {
        return duplicatedField;
    }

    public void setDuplicatedField(String duplicatedField) {
        this.duplicatedField = duplicatedField;
    }

    public String getDuplicatedValue() {
        return duplicatedValue;
    }

    public void setDuplicatedValue(String duplicatedValue) {
        this.duplicatedValue = duplicatedValue;
    }

    public URI getPathToDuplicatedEntity() {
        return pathToDuplicatedEntity;
    }

    public void setPathToDuplicatedEntity(URI pathToDuplicatedEntity) {
        this.pathToDuplicatedEntity = pathToDuplicatedEntity;
    }
}
