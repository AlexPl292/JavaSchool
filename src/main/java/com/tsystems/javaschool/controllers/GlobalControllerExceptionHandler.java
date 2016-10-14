package com.tsystems.javaschool.controllers;

import com.tsystems.javaschool.exceptions.JSException;
import com.tsystems.javaschool.exceptions.ResourceNotFoundException;
import com.tsystems.javaschool.exceptions.UniqueFieldDuplicateException;
import com.tsystems.javaschool.exceptions.WrongOptionConfigurationException;
import com.tsystems.javaschool.util.ErrorResponse;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Created by alex on 29.09.16.
 */
@ControllerAdvice
public class GlobalControllerExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(UniqueFieldDuplicateException.class)
    public ResponseEntity<ErrorResponse> handleUniqueFieldDuplicateException(UniqueFieldDuplicateException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .location(ex.getPathToDuplicatedEntity())
                .body(new ErrorResponse("Message", "Resource with '" +
                        ex.getDuplicatedField() + "' == '" + ex.getDuplicatedValue() +
                        "' already exists. This field must be unique."));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorResponse handleResourceNotFoundException(ResourceNotFoundException ex) {
        return new ErrorResponse("Message", "Resource '" +
                ex.getResourceName() + "' with id " + ex.getResourceId() + " does not exist");
    }


    @ExceptionHandler(WrongOptionConfigurationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse handleWrongOptionConfigurationException(WrongOptionConfigurationException ex) {
        return new ErrorResponse("Message", ex.getMessage());
    }

    @ExceptionHandler(JSException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse handleJSException(JSException ex) {
        return new ErrorResponse("Message", ex.getMessage());
    }
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return ResponseEntity
                .status(status)
                .body(new ErrorResponse(ex.getBindingResult()));
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return ResponseEntity
                .status(status)
                .body(new ErrorResponse("Message", ex.getMessage()));
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String message = ex.getMessage();
        return ResponseEntity
                .status(status)
                .body(new ErrorResponse("Message", message.substring(0, message.indexOf("(") - 1).replace("\"", "'")));
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String requiredType = ex.getRequiredType().toString();
        return ResponseEntity
                .status(status)
                .body(new ErrorResponse("Message", "Wrong value '" + ex.getValue() +
                        "' for argument '" + ((MethodArgumentTypeMismatchException) ex).getName() +
                        "'. It have to be [" + requiredType.substring(requiredType.lastIndexOf(".") + 1) + "]"));
    }
}
