package com.employeems.entity;

import com.employeems.enums.Department;
import com.employeems.enums.EmployeeStatus;
import com.employeems.validation.ValidPhoneNumber;
import com.employeems.validation.ValidSalaryRange;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Employee entity representing an employee in the system
 */
@Entity
@Table(name = "employees", indexes = {
    @Index(name = "idx_employee_email", columnList = "email", unique = true),
    @Index(name = "idx_employee_department", columnList = "department"),
    @Index(name = "idx_employee_status", columnList = "status"),
    @Index(name = "idx_employee_hire_date", columnList = "hire_date")
})
@EntityListeners(AuditingEntityListener.class)
public class Employee {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    @Column(name = "first_name", nullable = false)
    private String firstName;
    
    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    @Column(name = "last_name", nullable = false)
    private String lastName;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email address")
    @Column(name = "email", nullable = false, unique = true)
    private String email;
    
    @ValidPhoneNumber
    @Column(name = "phone_number")
    private String phoneNumber;
    
    @NotNull(message = "Department is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "department", nullable = false)
    private Department department;
    
    @NotBlank(message = "Position is required")
    @Size(max = 100, message = "Position cannot exceed 100 characters")
    @Column(name = "position", nullable = false)
    private String position;
    
    @NotNull(message = "Salary is required")
    @DecimalMin(value = "20000.00", message = "Salary must be at least $20,000")
    @ValidSalaryRange
    @Column(name = "salary", nullable = false, precision = 10, scale = 2)
    private BigDecimal salary;
    
    @NotNull(message = "Hire date is required")
    @PastOrPresent(message = "Hire date cannot be in the future")
    @Column(name = "hire_date", nullable = false)
    private LocalDate hireDate;
    
    @NotNull(message = "Status is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private EmployeeStatus status = EmployeeStatus.ACTIVE;
    
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
    
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    // Default constructor
    public Employee() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    // Parameterized constructor
    public Employee(String firstName, String lastName, String email, Department department, 
                   String position, BigDecimal salary, LocalDate hireDate) {
        this();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.department = department;
        this.position = position;
        this.salary = salary;
        this.hireDate = hireDate;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    public Department getDepartment() {
        return department;
    }
    
    public void setDepartment(Department department) {
        this.department = department;
    }
    
    public String getPosition() {
        return position;
    }
    
    public void setPosition(String position) {
        this.position = position;
    }
    
    public BigDecimal getSalary() {
        return salary;
    }
    
    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }
    
    public LocalDate getHireDate() {
        return hireDate;
    }
    
    public void setHireDate(LocalDate hireDate) {
        this.hireDate = hireDate;
    }
    
    public EmployeeStatus getStatus() {
        return status;
    }
    
    public void setStatus(EmployeeStatus status) {
        this.status = status;
    }
    
    public Boolean getIsActive() {
        return isActive;
    }
    
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    // Business methods
    public String getFullName() {
        return firstName + " " + lastName;
    }
    
    public long getTenureInYears() {
        if (hireDate == null) return 0;
        return java.time.temporal.ChronoUnit.YEARS.between(hireDate, LocalDate.now());
    }
    
    public long getTenureInMonths() {
        if (hireDate == null) return 0;
        return java.time.temporal.ChronoUnit.MONTHS.between(hireDate, LocalDate.now());
    }
    
    // toString method
    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", department=" + department +
                ", position='" + position + '\'' +
                ", salary=" + salary +
                ", hireDate=" + hireDate +
                ", status=" + status +
                ", isActive=" + isActive +
                '}';
    }
    
    // equals and hashCode methods
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return Objects.equals(id, employee.id) &&
                Objects.equals(email, employee.email);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, email);
    }
}


