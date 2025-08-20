package com.employeems.service;

import com.employeems.dto.EmployeeStatisticsDTO;
import com.employeems.entity.Employee;
import com.employeems.enums.Department;
import com.employeems.enums.EmployeeStatus;
import com.employeems.exception.DuplicateEmailException;
import com.employeems.exception.EmployeeNotFoundException;
import com.employeems.exception.InvalidEmployeeDataException;
import com.employeems.repository.EmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service class for Employee business logic
 */
@Service
@Transactional
public class EmployeeService {
    
    private static final Logger logger = LoggerFactory.getLogger(EmployeeService.class);
    
    private final EmployeeRepository employeeRepository;
    
    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }
    
    /**
     * Save a new employee
     */
    public Employee saveEmployee(Employee employee) {
        logger.info("Saving new employee: {}", employee.getEmail());
        
        // Validate unique email
        if (employeeRepository.existsByEmail(employee.getEmail())) {
            throw new DuplicateEmailException("Employee with email '" + employee.getEmail() + "' already exists");
        }
        
        // Set default values
        if (employee.getStatus() == null) {
            employee.setStatus(EmployeeStatus.ACTIVE);
        }
        if (employee.getIsActive() == null) {
            employee.setIsActive(true);
        }
        
        Employee savedEmployee = employeeRepository.save(employee);
        logger.info("Employee saved successfully with ID: {}", savedEmployee.getId());
        
        return savedEmployee;
    }
    
    /**
     * Update an existing employee
     */
    public Employee updateEmployee(Long id, Employee employeeDetails) {
        logger.info("Updating employee with ID: {}", id);
        
        Employee existingEmployee = getEmployeeById(id);
        
        // Check if email is being changed and if it's unique
        if (!existingEmployee.getEmail().equals(employeeDetails.getEmail()) &&
            employeeRepository.existsByEmailAndIdNot(employeeDetails.getEmail(), id)) {
            throw new DuplicateEmailException("Employee with email '" + employeeDetails.getEmail() + "' already exists");
        }
        
        // Update fields
        existingEmployee.setFirstName(employeeDetails.getFirstName());
        existingEmployee.setLastName(employeeDetails.getLastName());
        existingEmployee.setEmail(employeeDetails.getEmail());
        existingEmployee.setPhoneNumber(employeeDetails.getPhoneNumber());
        existingEmployee.setDepartment(employeeDetails.getDepartment());
        existingEmployee.setPosition(employeeDetails.getPosition());
        existingEmployee.setSalary(employeeDetails.getSalary());
        existingEmployee.setHireDate(employeeDetails.getHireDate());
        existingEmployee.setStatus(employeeDetails.getStatus());
        existingEmployee.setIsActive(employeeDetails.getIsActive());
        
        Employee updatedEmployee = employeeRepository.save(existingEmployee);
        logger.info("Employee updated successfully with ID: {}", updatedEmployee.getId());
        
        return updatedEmployee;
    }
    
    /**
     * Delete an employee (soft delete)
     */
    public void deleteEmployee(Long id) {
        logger.info("Deleting employee with ID: {}", id);
        
        Employee employee = getEmployeeById(id);
        employee.setIsActive(false);
        employee.setStatus(EmployeeStatus.TERMINATED);
        
        employeeRepository.save(employee);
        logger.info("Employee deleted successfully with ID: {}", id);
    }
    
    /**
     * Get employee by ID
     */
    @Transactional(readOnly = true)
    public Employee getEmployeeById(Long id) {
        logger.debug("Fetching employee with ID: {}", id);
        
        return employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));
    }
    
    /**
     * Get all employees with pagination and sorting
     */
    @Transactional(readOnly = true)
    public Page<Employee> getAllEmployees(Pageable pageable) {
        logger.debug("Fetching all employees with pagination: {}", pageable);
        
        if (pageable.getSort().isUnsorted()) {
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), 
                    Sort.by(Sort.Direction.ASC, "firstName", "lastName"));
        }
        
        return employeeRepository.findAll(pageable);
    }
    
    /**
     * Get all active employees
     */
    @Transactional(readOnly = true)
    public List<Employee> getAllActiveEmployees() {
        logger.debug("Fetching all active employees");
        return employeeRepository.findByIsActive(true);
    }
    
    /**
     * Get employees by department
     */
    @Transactional(readOnly = true)
    public List<Employee> getEmployeesByDepartment(Department department) {
        logger.debug("Fetching employees by department: {}", department);
        return employeeRepository.findByDepartment(department);
    }
    
    /**
     * Get employees by status
     */
    @Transactional(readOnly = true)
    public List<Employee> getEmployeesByStatus(EmployeeStatus status) {
        logger.debug("Fetching employees by status: {}", status);
        return employeeRepository.findByStatus(status);
    }
    
    /**
     * Search employees by keyword
     */
    @Transactional(readOnly = true)
    public List<Employee> searchEmployees(String keyword) {
        logger.debug("Searching employees with keyword: {}", keyword);
        
        if (!StringUtils.hasText(keyword)) {
            return getAllActiveEmployees();
        }
        
        return employeeRepository.searchEmployeesByKeyword(keyword.trim());
    }
    
    /**
     * Search employees with pagination
     */
    @Transactional(readOnly = true)
    public Page<Employee> searchEmployeesWithPagination(String keyword, Pageable pageable) {
        logger.debug("Searching employees with keyword: {} and pagination: {}", keyword, pageable);
        
        if (!StringUtils.hasText(keyword)) {
            return getAllEmployees(pageable);
        }
        
        return employeeRepository.searchEmployeesWithPagination(keyword.trim(), pageable);
    }
    
    /**
     * Get employees with filters
     */
    @Transactional(readOnly = true)
    public Page<Employee> getEmployeesWithFilters(Department department, EmployeeStatus status, 
                                                String keyword, Pageable pageable) {
        logger.debug("Fetching employees with filters - department: {}, status: {}, keyword: {}", 
                    department, status, keyword);
        
        return employeeRepository.findEmployeesWithFilters(department, status, keyword, pageable);
    }
    
    /**
     * Get employees by hire date range
     */
    @Transactional(readOnly = true)
    public List<Employee> getEmployeesByHireDateRange(LocalDate startDate, LocalDate endDate) {
        logger.debug("Fetching employees by hire date range: {} to {}", startDate, endDate);
        
        if (startDate == null || endDate == null) {
            throw new InvalidEmployeeDataException("Start date and end date are required");
        }
        
        if (startDate.isAfter(endDate)) {
            throw new InvalidEmployeeDataException("Start date cannot be after end date");
        }
        
        return employeeRepository.findActiveEmployeesByHireDateRange(startDate, endDate);
    }
    
    /**
     * Get employees by salary range
     */
    @Transactional(readOnly = true)
    public List<Employee> getEmployeesBySalaryRange(BigDecimal minSalary, BigDecimal maxSalary) {
        logger.debug("Fetching employees by salary range: {} to {}", minSalary, maxSalary);
        
        if (minSalary == null || maxSalary == null) {
            throw new InvalidEmployeeDataException("Minimum and maximum salary are required");
        }
        
        if (minSalary.compareTo(maxSalary) > 0) {
            throw new InvalidEmployeeDataException("Minimum salary cannot be greater than maximum salary");
        }
        
        return employeeRepository.findActiveEmployeesBySalaryRange(minSalary, maxSalary);
    }
    
    /**
     * Get top paid employees
     */
    @Transactional(readOnly = true)
    public List<Employee> getTopPaidEmployees(int limit) {
        logger.debug("Fetching top {} paid employees", limit);
        
        List<Employee> topEmployees = employeeRepository.findTop10ByOrderBySalaryDesc();
        return topEmployees.stream().limit(limit).collect(Collectors.toList());
    }
    
    /**
     * Get employee statistics
     */
    @Transactional(readOnly = true)
    public EmployeeStatisticsDTO getEmployeeStatistics() {
        logger.debug("Fetching employee statistics");
        
        long totalEmployees = employeeRepository.count();
        long activeEmployees = employeeRepository.countByIsActive(true);
        long inactiveEmployees = employeeRepository.countByIsActive(false);
        
        // Get department statistics
        List<Object[]> deptStats = employeeRepository.getDepartmentStatistics();
        Map<String, Object> departmentStats = deptStats.stream()
                .collect(Collectors.toMap(
                    row -> (String) row[0],
                    row -> Map.of(
                        "count", row[1],
                        "avgSalary", row[2]
                    )
                ));
        
        return new EmployeeStatisticsDTO(totalEmployees, activeEmployees, inactiveEmployees, departmentStats);
    }
    
    /**
     * Get department-wise employee count
     */
    @Transactional(readOnly = true)
    public Map<Department, Long> getEmployeeCountByDepartment() {
        logger.debug("Fetching employee count by department");
        
        return Map.of(
            Department.IT, employeeRepository.countByDepartment(Department.IT),
            Department.HR, employeeRepository.countByDepartment(Department.HR),
            Department.FINANCE, employeeRepository.countByDepartment(Department.FINANCE),
            Department.MARKETING, employeeRepository.countByDepartment(Department.MARKETING),
            Department.OPERATIONS, employeeRepository.countByDepartment(Department.OPERATIONS),
            Department.SALES, employeeRepository.countByDepartment(Department.SALES)
        );
    }
    
    /**
     * Calculate total salary by department
     */
    @Transactional(readOnly = true)
    public Map<Department, BigDecimal> calculateTotalSalaryByDepartment() {
        logger.debug("Calculating total salary by department");
        
        List<Employee> activeEmployees = getAllActiveEmployees();
        
        return activeEmployees.stream()
                .collect(Collectors.groupingBy(
                    Employee::getDepartment,
                    Collectors.reducing(BigDecimal.ZERO, Employee::getSalary, BigDecimal::add)
                ));
    }
    
    /**
     * Validate unique email
     */
    @Transactional(readOnly = true)
    public boolean validateUniqueEmail(String email, Long excludeId) {
        if (excludeId == null) {
            return !employeeRepository.existsByEmail(email);
        }
        return !employeeRepository.existsByEmailAndIdNot(email, excludeId);
    }
    
    /**
     * Get employee by email
     */
    @Transactional(readOnly = true)
    public Optional<Employee> getEmployeeByEmail(String email) {
        logger.debug("Fetching employee by email: {}", email);
        return employeeRepository.findByEmail(email);
    }
}


