package com.employeems.controller;

import com.employeems.dto.EmployeeStatisticsDTO;
import com.employeems.entity.Employee;
import com.employeems.enums.Department;
import com.employeems.enums.EmployeeStatus;
import com.employeems.service.EmployeeService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * REST Controller for Employee management
 */
@RestController
@RequestMapping("/api/employees")
@CrossOrigin(origins = "*")
public class EmployeeController {
    
    private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);
    
    private final EmployeeService employeeService;
    
    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }
    
    /**
     * GET /api/employees - Get all employees with pagination and sorting
     */
    @GetMapping
    public ResponseEntity<Page<Employee>> getAllEmployees(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "firstName") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        
        logger.info("Fetching all employees - page: {}, size: {}, sortBy: {}, sortDir: {}", 
                   page, size, sortBy, sortDir);
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                   Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Employee> employees = employeeService.getAllEmployees(pageable);
        
        return ResponseEntity.ok(employees);
    }
    
    /**
     * GET /api/employees/{id} - Get employee by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {
        logger.info("Fetching employee with ID: {}", id);
        
        Employee employee = employeeService.getEmployeeById(id);
        return ResponseEntity.ok(employee);
    }
    
    /**
     * POST /api/employees - Create new employee
     */
    @PostMapping
    public ResponseEntity<Employee> createEmployee(@Valid @RequestBody Employee employee) {
        logger.info("Creating new employee: {}", employee.getEmail());
        
        Employee createdEmployee = employeeService.saveEmployee(employee);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdEmployee);
    }
    
    /**
     * PUT /api/employees/{id} - Update employee
     */
    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable Long id, 
                                                 @Valid @RequestBody Employee employeeDetails) {
        logger.info("Updating employee with ID: {}", id);
        
        Employee updatedEmployee = employeeService.updateEmployee(id, employeeDetails);
        return ResponseEntity.ok(updatedEmployee);
    }
    
    /**
     * DELETE /api/employees/{id} - Delete employee
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        logger.info("Deleting employee with ID: {}", id);
        
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }
    
    /**
     * GET /api/employees/search - Search employees by keyword
     */
    @GetMapping("/search")
    public ResponseEntity<List<Employee>> searchEmployees(@RequestParam String keyword) {
        logger.info("Searching employees with keyword: {}", keyword);
        
        List<Employee> employees = employeeService.searchEmployees(keyword);
        return ResponseEntity.ok(employees);
    }
    
    /**
     * GET /api/employees/search/paginated - Search employees with pagination
     */
    @GetMapping("/search/paginated")
    public ResponseEntity<Page<Employee>> searchEmployeesWithPagination(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        logger.info("Searching employees with keyword: {} and pagination - page: {}, size: {}", 
                   keyword, page, size);
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Employee> employees = employeeService.searchEmployeesWithPagination(keyword, pageable);
        
        return ResponseEntity.ok(employees);
    }
    
    /**
     * GET /api/employees/department/{dept} - Get employees by department
     */
    @GetMapping("/department/{dept}")
    public ResponseEntity<List<Employee>> getEmployeesByDepartment(@PathVariable Department dept) {
        logger.info("Fetching employees by department: {}", dept);
        
        List<Employee> employees = employeeService.getEmployeesByDepartment(dept);
        return ResponseEntity.ok(employees);
    }
    
    /**
     * GET /api/employees/status/{status} - Get employees by status
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Employee>> getEmployeesByStatus(@PathVariable EmployeeStatus status) {
        logger.info("Fetching employees by status: {}", status);
        
        List<Employee> employees = employeeService.getEmployeesByStatus(status);
        return ResponseEntity.ok(employees);
    }
    
    /**
     * GET /api/employees/filter - Get employees with filters
     */
    @GetMapping("/filter")
    public ResponseEntity<Page<Employee>> getEmployeesWithFilters(
            @RequestParam(required = false) Department department,
            @RequestParam(required = false) EmployeeStatus status,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "firstName") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        
        logger.info("Fetching employees with filters - department: {}, status: {}, keyword: {}", 
                   department, status, keyword);
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                   Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Employee> employees = employeeService.getEmployeesWithFilters(
                department, status, keyword, pageable);
        
        return ResponseEntity.ok(employees);
    }
    
    /**
     * GET /api/employees/hire-date-range - Get employees by hire date range
     */
    @GetMapping("/hire-date-range")
    public ResponseEntity<List<Employee>> getEmployeesByHireDateRange(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {
        
        logger.info("Fetching employees by hire date range: {} to {}", startDate, endDate);
        
        List<Employee> employees = employeeService.getEmployeesByHireDateRange(startDate, endDate);
        return ResponseEntity.ok(employees);
    }
    
    /**
     * GET /api/employees/salary-range - Get employees by salary range
     */
    @GetMapping("/salary-range")
    public ResponseEntity<List<Employee>> getEmployeesBySalaryRange(
            @RequestParam BigDecimal minSalary,
            @RequestParam BigDecimal maxSalary) {
        
        logger.info("Fetching employees by salary range: {} to {}", minSalary, maxSalary);
        
        List<Employee> employees = employeeService.getEmployeesBySalaryRange(minSalary, maxSalary);
        return ResponseEntity.ok(employees);
    }
    
    /**
     * GET /api/employees/top-paid - Get top paid employees
     */
    @GetMapping("/top-paid")
    public ResponseEntity<List<Employee>> getTopPaidEmployees(
            @RequestParam(defaultValue = "10") int limit) {
        
        logger.info("Fetching top {} paid employees", limit);
        
        List<Employee> employees = employeeService.getTopPaidEmployees(limit);
        return ResponseEntity.ok(employees);
    }
    
    /**
     * GET /api/employees/statistics - Get employee statistics
     */
    @GetMapping("/statistics")
    public ResponseEntity<EmployeeStatisticsDTO> getEmployeeStatistics() {
        logger.info("Fetching employee statistics");
        
        EmployeeStatisticsDTO statistics = employeeService.getEmployeeStatistics();
        return ResponseEntity.ok(statistics);
    }
    
    /**
     * GET /api/employees/department-count - Get employee count by department
     */
    @GetMapping("/department-count")
    public ResponseEntity<Map<Department, Long>> getEmployeeCountByDepartment() {
        logger.info("Fetching employee count by department");
        
        Map<Department, Long> departmentCount = employeeService.getEmployeeCountByDepartment();
        return ResponseEntity.ok(departmentCount);
    }
    
    /**
     * GET /api/employees/salary-by-department - Get total salary by department
     */
    @GetMapping("/salary-by-department")
    public ResponseEntity<Map<Department, BigDecimal>> getTotalSalaryByDepartment() {
        logger.info("Fetching total salary by department");
        
        Map<Department, BigDecimal> salaryByDepartment = employeeService.calculateTotalSalaryByDepartment();
        return ResponseEntity.ok(salaryByDepartment);
    }
    
    /**
     * GET /api/employees/active - Get all active employees
     */
    @GetMapping("/active")
    public ResponseEntity<List<Employee>> getActiveEmployees() {
        logger.info("Fetching all active employees");
        
        List<Employee> employees = employeeService.getAllActiveEmployees();
        return ResponseEntity.ok(employees);
    }
    
    /**
     * GET /api/employees/validate-email - Validate unique email
     */
    @GetMapping("/validate-email")
    public ResponseEntity<Map<String, Boolean>> validateEmail(
            @RequestParam String email,
            @RequestParam(required = false) Long excludeId) {
        
        logger.info("Validating email: {} with exclude ID: {}", email, excludeId);
        
        boolean isValid = employeeService.validateUniqueEmail(email, excludeId);
        return ResponseEntity.ok(Map.of("valid", isValid));
    }
}


