package com.tsystems.javaschool;

import java.net.URI;

/**
 * Created by alex on 30.09.16.
 */
public class UniqueFieldDuplicateException extends RuntimeException {

    private String duplicatedField;
    private String duplicatedValue;
    private URI pathToDuplicatedEntity;

    public UniqueFieldDuplicateException() {
    }

    public UniqueFieldDuplicateException(String duplicatedField, String duplicatedValue, String path) {
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
