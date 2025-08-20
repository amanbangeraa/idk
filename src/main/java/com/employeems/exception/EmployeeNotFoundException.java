package com.employeems.exception;

/**
 * Exception thrown when an employee is not found in the system
 */
public class EmployeeNotFoundException extends RuntimeException {
    
    public EmployeeNotFoundException(String message) {
        super(message);
    }
    
    public EmployeeNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public EmployeeNotFoundException(Long employeeId) {
        super("Employee not found with ID: " + employeeId);
    }
}


