package com.tsystems.javaschool.exceptions;

/**
 * Created by alex on 07.10.16.
 *
 * "#" (forbidden)
 * -> (required)
 *
 * Codes:
 *
 * Code 1:
 *    new
 *   /#
 *  v#
 *  1
 *
 * Code 2:
 *
 *    new
 *   /   \
 *  v     v
 *  1 ### 2
 *
 *
 * Code 3:
 *    new
 *   /   #
 *  v     #
 *  1 ---> 2
 */
public class WrongOptionConfigurationException extends RuntimeException{
    private Integer errorCode;

    public WrongOptionConfigurationException(Integer errorCode) {
        super("Error code: "+ errorCode+ ". Check docs");
        this.errorCode = errorCode;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }
}
