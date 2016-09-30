package com.tsystems.javaschool;

/**
 * Created by alex on 30.09.16.
 */
public class ResourceNotFoundException extends RuntimeException{
    private String resourceName;
    private Integer resourceId;

    public ResourceNotFoundException(String resourceName, Integer resourceId) {
        this.resourceName = resourceName;
        this.resourceId = resourceId;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public Integer getResourceId() {
        return resourceId;
    }

    public void setResourceId(Integer resourceId) {
        this.resourceId = resourceId;
    }
}
