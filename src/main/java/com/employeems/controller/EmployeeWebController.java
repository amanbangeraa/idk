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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * Web Controller for Employee Thymeleaf views
 */
@Controller
@RequestMapping("/employees")
public class EmployeeWebController {
    
    private static final Logger logger = LoggerFactory.getLogger(EmployeeWebController.class);
    
    private final EmployeeService employeeService;
    
    @Autowired
    public EmployeeWebController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }
    
    /**
     * GET /employees - List all employees with search and filter
     */
    @GetMapping
    public String listEmployees(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "firstName") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) Department department,
            @RequestParam(required = false) EmployeeStatus status,
            @RequestParam(required = false) String keyword,
            Model model) {
        
        logger.info("Displaying employee list - page: {}, size: {}, filters: dept={}, status={}, keyword={}", 
                   page, size, department, status, keyword);
        
        // Create pageable with sorting
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                   Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        // Get employees with filters
        Page<Employee> employees = employeeService.getEmployeesWithFilters(
                department, status, keyword, pageable);
        
        // Get statistics
        EmployeeStatisticsDTO statistics = employeeService.getEmployeeStatistics();
        
        // Add to model
        model.addAttribute("employees", employees);
        model.addAttribute("statistics", statistics);
        model.addAttribute("departments", Department.values());
        model.addAttribute("statuses", EmployeeStatus.values());
        model.addAttribute("currentDepartment", department);
        model.addAttribute("currentStatus", status);
        model.addAttribute("currentKeyword", keyword);
        model.addAttribute("currentSortBy", sortBy);
        model.addAttribute("currentSortDir", sortDir);
        
        return "employees/list";
    }
    
    /**
     * GET /employees/new - Show add employee form
     */
    @GetMapping("/new")
    public String showAddEmployeeForm(Model model) {
        logger.info("Displaying add employee form");
        
        model.addAttribute("employee", new Employee());
        model.addAttribute("departments", Department.values());
        model.addAttribute("statuses", EmployeeStatus.values());
        model.addAttribute("isEdit", false);
        
        return "employees/form";
    }
    
    /**
     * POST /employees - Process add employee form
     */
    @PostMapping
    public String addEmployee(@Valid @ModelAttribute Employee employee, 
                            BindingResult result, 
                            RedirectAttributes redirectAttributes,
                            Model model) {
        
        logger.info("Processing add employee form for: {}", employee.getEmail());
        
        if (result.hasErrors()) {
            logger.warn("Validation errors in add employee form: {}", result.getAllErrors());
            model.addAttribute("departments", Department.values());
            model.addAttribute("statuses", EmployeeStatus.values());
            model.addAttribute("isEdit", false);
            return "employees/form";
        }
        
        try {
            Employee savedEmployee = employeeService.saveEmployee(employee);
            redirectAttributes.addFlashAttribute("successMessage", 
                    "Employee '" + savedEmployee.getFullName() + "' added successfully!");
            logger.info("Employee added successfully with ID: {}", savedEmployee.getId());
            
            return "redirect:/employees";
            
        } catch (Exception e) {
            logger.error("Error adding employee: {}", e.getMessage(), e);
            redirectAttributes.addFlashAttribute("errorMessage", 
                    "Error adding employee: " + e.getMessage());
            return "redirect:/employees/new";
        }
    }
    
    /**
     * GET /employees/{id} - Show employee details
     */
    @GetMapping("/{id}")
    public String showEmployeeDetails(@PathVariable Long id, Model model) {
        logger.info("Displaying employee details for ID: {}", id);
        
        try {
            Employee employee = employeeService.getEmployeeById(id);
            model.addAttribute("employee", employee);
            return "employees/details";
            
        } catch (Exception e) {
            logger.error("Error fetching employee details: {}", e.getMessage(), e);
            return "redirect:/employees";
        }
    }
    
    /**
     * GET /employees/{id}/edit - Show edit employee form
     */
    @GetMapping("/{id}/edit")
    public String showEditEmployeeForm(@PathVariable Long id, Model model) {
        logger.info("Displaying edit employee form for ID: {}", id);
        
        try {
            Employee employee = employeeService.getEmployeeById(id);
            model.addAttribute("employee", employee);
            model.addAttribute("departments", Department.values());
            model.addAttribute("statuses", EmployeeStatus.values());
            model.addAttribute("isEdit", true);
            return "employees/form";
            
        } catch (Exception e) {
            logger.error("Error fetching employee for edit: {}", e.getMessage(), e);
            return "redirect:/employees";
        }
    }
    
    /**
     * POST /employees/{id}/update - Process employee update
     */
    @PostMapping("/{id}/update")
    public String updateEmployee(@PathVariable Long id,
                               @Valid @ModelAttribute Employee employee,
                               BindingResult result,
                               RedirectAttributes redirectAttributes,
                               Model model) {
        
        logger.info("Processing update employee form for ID: {}", id);
        
        if (result.hasErrors()) {
            logger.warn("Validation errors in update employee form: {}", result.getAllErrors());
            model.addAttribute("departments", Department.values());
            model.addAttribute("statuses", EmployeeStatus.values());
            model.addAttribute("isEdit", true);
            return "employees/form";
        }
        
        try {
            Employee updatedEmployee = employeeService.updateEmployee(id, employee);
            redirectAttributes.addFlashAttribute("successMessage", 
                    "Employee '" + updatedEmployee.getFullName() + "' updated successfully!");
            logger.info("Employee updated successfully with ID: {}", updatedEmployee.getId());
            
            return "redirect:/employees/" + id;
            
        } catch (Exception e) {
            logger.error("Error updating employee: {}", e.getMessage(), e);
            redirectAttributes.addFlashAttribute("errorMessage", 
                    "Error updating employee: " + e.getMessage());
            return "redirect:/employees/" + id + "/edit";
        }
    }
    
    /**
     * POST /employees/{id}/delete - Delete employee
     */
    @PostMapping("/{id}/delete")
    public String deleteEmployee(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        logger.info("Processing delete employee request for ID: {}", id);
        
        try {
            Employee employee = employeeService.getEmployeeById(id);
            String employeeName = employee.getFullName();
            
            employeeService.deleteEmployee(id);
            redirectAttributes.addFlashAttribute("successMessage", 
                    "Employee '" + employeeName + "' deleted successfully!");
            logger.info("Employee deleted successfully with ID: {}", id);
            
            return "redirect:/employees";
            
        } catch (Exception e) {
            logger.error("Error deleting employee: {}", e.getMessage(), e);
            redirectAttributes.addFlashAttribute("errorMessage", 
                    "Error deleting employee: " + e.getMessage());
            return "redirect:/employees/" + id;
        }
    }
    
    /**
     * GET /employees/departments/{dept} - Filter employees by department
     */
    @GetMapping("/departments/{dept}")
    public String filterByDepartment(@PathVariable Department dept,
                                   @RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "10") int size,
                                   Model model) {
        
        logger.info("Filtering employees by department: {}", dept);
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("firstName", "lastName"));
        Page<Employee> employees = employeeService.getEmployeesWithFilters(
                dept, null, null, pageable);
        
        EmployeeStatisticsDTO statistics = employeeService.getEmployeeStatistics();
        
        model.addAttribute("employees", employees);
        model.addAttribute("statistics", statistics);
        model.addAttribute("departments", Department.values());
        model.addAttribute("statuses", EmployeeStatus.values());
        model.addAttribute("currentDepartment", dept);
        model.addAttribute("currentStatus", null);
        model.addAttribute("currentKeyword", null);
        model.addAttribute("currentSortBy", "firstName");
        model.addAttribute("currentSortDir", "asc");
        
        return "employees/list";
    }
    
    /**
     * GET /employees/search - Search employees
     */
    @GetMapping("/search")
    public String searchEmployees(@RequestParam String keyword,
                                @RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "10") int size,
                                Model model) {
        
        logger.info("Searching employees with keyword: {}", keyword);
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("firstName", "lastName"));
        Page<Employee> employees = employeeService.searchEmployeesWithPagination(keyword, pageable);
        
        EmployeeStatisticsDTO statistics = employeeService.getEmployeeStatistics();
        
        model.addAttribute("employees", employees);
        model.addAttribute("statistics", statistics);
        model.addAttribute("departments", Department.values());
        model.addAttribute("statuses", EmployeeStatus.values());
        model.addAttribute("currentDepartment", null);
        model.addAttribute("currentStatus", null);
        model.addAttribute("currentKeyword", keyword);
        model.addAttribute("currentSortBy", "firstName");
        model.addAttribute("currentSortDir", "asc");
        
        return "employees/list";
    }
    
    /**
     * GET /employees/statistics - Show employee statistics
     */
    @GetMapping("/statistics")
    public String showStatistics(Model model) {
        logger.info("Displaying employee statistics");
        
        EmployeeStatisticsDTO statistics = employeeService.getEmployeeStatistics();
        model.addAttribute("statistics", statistics);
        
        return "employees/statistics";
    }
}


