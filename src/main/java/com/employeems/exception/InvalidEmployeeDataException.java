package com.employeems.exception;

/**
 * Exception thrown when employee data validation fails
 */
public class InvalidEmployeeDataException extends RuntimeException {
    
    public InvalidEmployeeDataException(String message) {
        super(message);
    }
    
    public InvalidEmployeeDataException(String message, Throwable cause) {
        super(message, cause);
    }
}


