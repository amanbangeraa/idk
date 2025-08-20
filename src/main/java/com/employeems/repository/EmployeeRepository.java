package com.employeems.repository;

import com.employeems.entity.Employee;
import com.employeems.enums.Department;
import com.employeems.enums.EmployeeStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Employee entity with custom query methods
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    
    // Basic find methods
    Optional<Employee> findByEmail(String email);
    List<Employee> findByDepartment(Department department);
    List<Employee> findByStatus(EmployeeStatus status);
    List<Employee> findByIsActive(Boolean isActive);
    
    // Find by hire date range
    List<Employee> findByHireDateBetween(LocalDate startDate, LocalDate endDate);
    List<Employee> findByHireDateAfter(LocalDate date);
    List<Employee> findByHireDateBefore(LocalDate date);
    
    // Find by salary range
    List<Employee> findBySalaryBetween(BigDecimal minSalary, BigDecimal maxSalary);
    List<Employee> findBySalaryGreaterThan(BigDecimal salary);
    List<Employee> findBySalaryLessThan(BigDecimal salary);
    
    // Find by position
    List<Employee> findByPositionContainingIgnoreCase(String position);
    
    // Search by name (first or last name containing keyword)
    List<Employee> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
            String firstName, String lastName);
    
    // Count methods
    long countByDepartment(Department department);
    long countByStatus(EmployeeStatus status);
    long countByIsActive(Boolean isActive);
    
    // Find top paid employees
    List<Employee> findTop10ByOrderBySalaryDesc();
    
    // Custom JPQL queries
    @Query("SELECT e FROM Employee e WHERE e.department = :department AND e.isActive = true")
    List<Employee> findActiveEmployeesByDepartment(@Param("department") Department department);
    
    @Query("SELECT e FROM Employee e WHERE e.salary >= :minSalary AND e.salary <= :maxSalary AND e.isActive = true")
    List<Employee> findActiveEmployeesBySalaryRange(
            @Param("minSalary") BigDecimal minSalary, 
            @Param("maxSalary") BigDecimal maxSalary);
    
    @Query("SELECT e FROM Employee e WHERE " +
           "LOWER(e.firstName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(e.lastName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(e.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(e.position) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Employee> searchEmployeesByKeyword(@Param("keyword") String keyword);
    
    @Query("SELECT e FROM Employee e WHERE e.hireDate >= :startDate AND e.hireDate <= :endDate AND e.isActive = true")
    List<Employee> findActiveEmployeesByHireDateRange(
            @Param("startDate") LocalDate startDate, 
            @Param("endDate") LocalDate endDate);
    
    // Native SQL queries for complex operations
    @Query(value = "SELECT department, COUNT(*) as count, AVG(salary) as avgSalary " +
                   "FROM employees WHERE is_active = true GROUP BY department", 
           nativeQuery = true)
    List<Object[]> getDepartmentStatistics();
    
    @Query(value = "SELECT * FROM employees WHERE " +
                   "LOWER(first_name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
                   "LOWER(last_name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
                   "LOWER(email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
                   "LOWER(position) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
                   "ORDER BY first_name, last_name", 
           nativeQuery = true)
    Page<Employee> searchEmployeesWithPagination(@Param("keyword") String keyword, Pageable pageable);
    
    // Pagination and sorting support
    Page<Employee> findByDepartment(Department department, Pageable pageable);
    Page<Employee> findByStatus(EmployeeStatus status, Pageable pageable);
    Page<Employee> findByIsActive(Boolean isActive, Pageable pageable);
    
    // Find employees with pagination and search
    @Query("SELECT e FROM Employee e WHERE " +
           "(:department IS NULL OR e.department = :department) AND " +
           "(:status IS NULL OR e.status = :status) AND " +
           "(:keyword IS NULL OR " +
           "LOWER(e.firstName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(e.lastName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(e.email) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Employee> findEmployeesWithFilters(
            @Param("department") Department department,
            @Param("status") EmployeeStatus status,
            @Param("keyword") String keyword,
            Pageable pageable);
    
    // Check if email exists (for validation)
    boolean existsByEmail(String email);
    boolean existsByEmailAndIdNot(String email, Long id);
}


