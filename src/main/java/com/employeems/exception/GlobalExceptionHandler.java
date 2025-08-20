package com.employeems.exception;

import com.employeems.dto.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Global exception handler for the Employee Management System
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    
    /**
     * Handle EmployeeNotFoundException
     */
    @ExceptionHandler(EmployeeNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEmployeeNotFoundException(
            EmployeeNotFoundException ex, WebRequest request) {
        
        logger.error("Employee not found: {}", ex.getMessage());
        
        ErrorResponse errorResponse = new ErrorResponse(
                ex.getMessage(),
                "Employee Not Found",
                HttpStatus.NOT_FOUND.value(),
                request.getDescription(false)
        );
        
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
    
    /**
     * Handle DuplicateEmailException
     */
    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateEmailException(
            DuplicateEmailException ex, WebRequest request) {
        
        logger.error("Duplicate email error: {}", ex.getMessage());
        
        ErrorResponse errorResponse = new ErrorResponse(
                ex.getMessage(),
                "Duplicate Email",
                HttpStatus.CONFLICT.value(),
                request.getDescription(false)
        );
        
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }
    
    /**
     * Handle InvalidEmployeeDataException
     */
    @ExceptionHandler(InvalidEmployeeDataException.class)
    public ResponseEntity<ErrorResponse> handleInvalidEmployeeDataException(
            InvalidEmployeeDataException ex, WebRequest request) {
        
        logger.error("Invalid employee data: {}", ex.getMessage());
        
        ErrorResponse errorResponse = new ErrorResponse(
                ex.getMessage(),
                "Invalid Employee Data",
                HttpStatus.BAD_REQUEST.value(),
                request.getDescription(false)
        );
        
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    
    /**
     * Handle validation errors
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(
            MethodArgumentNotValidException ex, WebRequest request) {
        
        logger.error("Validation error: {}", ex.getMessage());
        
        List<String> details = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());
        
        ErrorResponse errorResponse = new ErrorResponse(
                "Validation failed",
                "Validation Error",
                HttpStatus.BAD_REQUEST.value(),
                request.getDescription(false),
                details
        );
        
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    
    /**
     * Handle database constraint violations
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(
            DataIntegrityViolationException ex, WebRequest request) {
        
        logger.error("Data integrity violation: {}", ex.getMessage());
        
        ErrorResponse errorResponse = new ErrorResponse(
                "Data integrity constraint violation",
                "Data Integrity Error",
                HttpStatus.CONFLICT.value(),
                request.getDescription(false)
        );
        
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }
    
    /**
     * Handle generic runtime exceptions
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(
            RuntimeException ex, WebRequest request) {
        
        logger.error("Runtime exception: {}", ex.getMessage(), ex);
        
        ErrorResponse errorResponse = new ErrorResponse(
                "An unexpected error occurred",
                "Internal Server Error",
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                request.getDescription(false)
        );
        
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    /**
     * Handle generic exceptions
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(
            Exception ex, WebRequest request) {
        
        logger.error("Generic exception: {}", ex.getMessage(), ex);
        
        ErrorResponse errorResponse = new ErrorResponse(
                "An unexpected error occurred",
                "Internal Server Error",
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                request.getDescription(false)
        );
        
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}


