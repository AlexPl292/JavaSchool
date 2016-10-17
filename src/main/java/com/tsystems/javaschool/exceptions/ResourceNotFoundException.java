package com.tsystems.javaschool.exceptions;

/**
 * Created by alex on 30.09.16.
 *
 * Resource is not found
 */
public class ResourceNotFoundException extends JSException {
    /**
     * Name of resource
     */
    private String resourceName;

    /**
     * Id of resource
     */
    private Integer resourceId;

    public ResourceNotFoundException(String resourceName, Integer resourceId) {
        // Create exception with message, name and id
        super("Resource '" +
                resourceName + "' with id " + resourceId + " does not exist");
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
