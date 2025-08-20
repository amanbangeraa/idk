package com.employeems.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for consistent error response format
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
    
    private String message;
    private String error;
    private int status;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;
    
    private String path;
    private List<String> details;
    
    public ErrorResponse() {
        this.timestamp = LocalDateTime.now();
    }
    
    public ErrorResponse(String message, String error, int status) {
        this();
        this.message = message;
        this.error = error;
        this.status = status;
    }
    
    public ErrorResponse(String message, String error, int status, String path) {
        this(message, error, status);
        this.path = path;
    }
    
    public ErrorResponse(String message, String error, int status, String path, List<String> details) {
        this(message, error, status, path);
        this.details = details;
    }
    
    // Getters and Setters
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public String getError() {
        return error;
    }
    
    public void setError(String error) {
        this.error = error;
    }
    
    public int getStatus() {
        return status;
    }
    
    public void setStatus(int status) {
        this.status = status;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
    
    public String getPath() {
        return path;
    }
    
    public void setPath(String path) {
        this.path = path;
    }
    
    public List<String> getDetails() {
        return details;
    }
    
    public void setDetails(List<String> details) {
        this.details = details;
    }
}


