package com.employeems;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Main Spring Boot Application class for Employee Management System
 */
@SpringBootApplication
@EnableJpaAuditing
@EnableJpaRepositories
public class EmployeeManagementSystemApplication {
    
    public static void main(String[] args) {
        System.out.println("================================================");
        System.out.println("Starting Employee Management System...");
        System.out.println("================================================");
        
        SpringApplication.run(EmployeeManagementSystemApplication.class, args);
        
        System.out.println("================================================");
        System.out.println("Employee Management System started successfully!");
        System.out.println("Application URL: http://localhost:8080");
        System.out.println("H2 Console URL: http://localhost:8080/h2-console");
        System.out.println("API Base URL: http://localhost:8080/api");
        System.out.println("================================================");
    }
}


