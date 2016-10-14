package com.tsystems.javaschool.exceptions;

/**
 * Created by alex on 07.10.16.
 * <p>
 * "#" (forbidden)
 * -> (required)
 * <p>
 * Codes:
 * <p>
 * Code 1:
 * new
 * /#
 * v#
 * 1
 * <p>
 * Code 2:
 * <p>
 * new
 * /   \
 * v     v
 * 1 ### 2
 * <p>
 * <p>
 * Code 3:
 * new
 * /   #
 * v     #
 * 1 ---> 2
 */
public class WrongOptionConfigurationException extends RuntimeException {
    private Integer errorCode;

    public WrongOptionConfigurationException(Integer errorCode) {
        super("Wrong option configuration. Error code: " + errorCode + ". Check docs");
        this.errorCode = errorCode;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }
}
