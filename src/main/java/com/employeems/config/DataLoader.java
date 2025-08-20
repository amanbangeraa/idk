package com.employeems.config;

import com.employeems.entity.Employee;
import com.employeems.enums.Department;
import com.employeems.enums.EmployeeStatus;
import com.employeems.repository.EmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

/**
 * Data loader to populate the database with sample employee data
 */
@Component
public class DataLoader implements CommandLineRunner {
    
    private static final Logger logger = LoggerFactory.getLogger(DataLoader.class);
    
    private final EmployeeRepository employeeRepository;
    
    @Autowired
    public DataLoader(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }
    
    @Override
    public void run(String... args) throws Exception {
        logger.info("Starting data initialization...");
        
        // Check if data already exists
        if (employeeRepository.count() > 0) {
            logger.info("Database already contains data. Skipping initialization.");
            return;
        }
        
        // Create sample employees
        List<Employee> sampleEmployees = createSampleEmployees();
        
        // Save all employees
        employeeRepository.saveAll(sampleEmployees);
        
        logger.info("Successfully loaded {} sample employees into the database", sampleEmployees.size());
    }
    
    /**
     * Create sample employee data
     */
    private List<Employee> createSampleEmployees() {
        return Arrays.asList(
                        // IT Department
            createEmployee("John", "Smith", "john.smith@company.com", "+1-555-010-1234",
                         Department.IT, "Senior Software Engineer", new BigDecimal("95000"),
                         LocalDate.of(2020, 3, 15), EmployeeStatus.ACTIVE),

            createEmployee("Sarah", "Johnson", "sarah.johnson@company.com", "+1-555-010-2345",
                         Department.IT, "Software Engineer", new BigDecimal("75000"),
                         LocalDate.of(2021, 6, 20), EmployeeStatus.ACTIVE),

            createEmployee("Michael", "Brown", "michael.brown@company.com", "+1-555-010-3456",
                         Department.IT, "DevOps Engineer", new BigDecimal("85000"),
                         LocalDate.of(2019, 11, 8), EmployeeStatus.ACTIVE),

            createEmployee("Emily", "Davis", "emily.davis@company.com", "+1-555-010-4567",
                         Department.IT, "QA Engineer", new BigDecimal("70000"),
                         LocalDate.of(2022, 1, 10), EmployeeStatus.ACTIVE),

            createEmployee("David", "Wilson", "david.wilson@company.com", "+1-555-010-5678",
                         Department.IT, "System Administrator", new BigDecimal("80000"),
                         LocalDate.of(2018, 9, 12), EmployeeStatus.ON_LEAVE),
            
                        // HR Department
            createEmployee("Lisa", "Anderson", "lisa.anderson@company.com", "+1-555-020-1234",
                         Department.HR, "HR Manager", new BigDecimal("75000"),
                         LocalDate.of(2019, 4, 5), EmployeeStatus.ACTIVE),

            createEmployee("Robert", "Taylor", "robert.taylor@company.com", "+1-555-020-2345",
                         Department.HR, "HR Specialist", new BigDecimal("60000"),
                         LocalDate.of(2021, 2, 18), EmployeeStatus.ACTIVE),

            createEmployee("Jennifer", "Martinez", "jennifer.martinez@company.com", "+1-555-020-3456",
                         Department.HR, "Recruiter", new BigDecimal("55000"),
                         LocalDate.of(2022, 7, 25), EmployeeStatus.ACTIVE),
            
                        // Finance Department
            createEmployee("William", "Garcia", "william.garcia@company.com", "+1-555-030-1234",
                         Department.FINANCE, "Finance Manager", new BigDecimal("90000"),
                         LocalDate.of(2018, 12, 3), EmployeeStatus.ACTIVE),

            createEmployee("Amanda", "Rodriguez", "amanda.rodriguez@company.com", "+1-555-030-2345",
                         Department.FINANCE, "Financial Analyst", new BigDecimal("65000"),
                         LocalDate.of(2020, 8, 14), EmployeeStatus.ACTIVE),

            createEmployee("Christopher", "Lee", "christopher.lee@company.com", "+1-555-030-3456",
                         Department.FINANCE, "Accountant", new BigDecimal("60000"),
                         LocalDate.of(2021, 3, 22), EmployeeStatus.ACTIVE),
            
                        // Marketing Department
            createEmployee("Jessica", "White", "jessica.white@company.com", "+1-555-040-1234",
                         Department.MARKETING, "Marketing Director", new BigDecimal("95000"),
                         LocalDate.of(2019, 1, 15), EmployeeStatus.ACTIVE),

            createEmployee("Daniel", "Clark", "daniel.clark@company.com", "+1-555-040-2345",
                         Department.MARKETING, "Marketing Specialist", new BigDecimal("65000"),
                         LocalDate.of(2020, 5, 30), EmployeeStatus.ACTIVE),

            createEmployee("Ashley", "Lewis", "ashley.lewis@company.com", "+1-555-040-3456",
                         Department.MARKETING, "Content Creator", new BigDecimal("55000"),
                         LocalDate.of(2022, 3, 8), EmployeeStatus.ACTIVE),
            
                        // Operations Department
            createEmployee("Matthew", "Hall", "matthew.hall@company.com", "+1-555-050-1234",
                         Department.OPERATIONS, "Operations Manager", new BigDecimal("85000"),
                         LocalDate.of(2018, 6, 10), EmployeeStatus.ACTIVE),

            createEmployee("Nicole", "Young", "nicole.young@company.com", "+1-555-050-2345",
                         Department.OPERATIONS, "Operations Coordinator", new BigDecimal("55000"),
                         LocalDate.of(2021, 9, 5), EmployeeStatus.ACTIVE),

            createEmployee("Kevin", "King", "kevin.king@company.com", "+1-555-050-3456",
                         Department.OPERATIONS, "Process Analyst", new BigDecimal("70000"),
                         LocalDate.of(2020, 12, 18), EmployeeStatus.ACTIVE),
            
                        // Sales Department
            createEmployee("Stephanie", "Wright", "stephanie.wright@company.com", "+1-555-060-1234",
                         Department.SALES, "Sales Manager", new BigDecimal("85000"),
                         LocalDate.of(2019, 7, 22), EmployeeStatus.ACTIVE),

            createEmployee("Andrew", "Lopez", "andrew.lopez@company.com", "+1-555-060-2345",
                         Department.SALES, "Sales Representative", new BigDecimal("60000"),
                         LocalDate.of(2021, 4, 12), EmployeeStatus.ACTIVE),

            createEmployee("Rachel", "Hill", "rachel.hill@company.com", "+1-555-060-3456",
                         Department.SALES, "Account Executive", new BigDecimal("65000"),
                         LocalDate.of(2020, 10, 28), EmployeeStatus.ACTIVE),
            
                        // Additional IT employees for testing pagination
            createEmployee("Thomas", "Scott", "thomas.scott@company.com", "+1-555-010-6789",
                         Department.IT, "Frontend Developer", new BigDecimal("75000"),
                         LocalDate.of(2022, 5, 15), EmployeeStatus.ACTIVE),

            createEmployee("Maria", "Green", "maria.green@company.com", "+1-555-010-7890",
                         Department.IT, "Backend Developer", new BigDecimal("80000"),
                         LocalDate.of(2021, 11, 3), EmployeeStatus.ACTIVE),

            createEmployee("James", "Adams", "james.adams@company.com", "+1-555-010-8901",
                         Department.IT, "Mobile Developer", new BigDecimal("78000"),
                         LocalDate.of(2022, 2, 20), EmployeeStatus.ACTIVE),

            createEmployee("Patricia", "Baker", "patricia.baker@company.com", "+1-555-010-9012",
                         Department.IT, "UI/UX Designer", new BigDecimal("72000"),
                         LocalDate.of(2021, 8, 14), EmployeeStatus.ACTIVE),

            createEmployee("Richard", "Carter", "richard.carter@company.com", "+1-555-010-0123",
                         Department.IT, "Data Scientist", new BigDecimal("90000"),
                         LocalDate.of(2020, 4, 7), EmployeeStatus.ACTIVE),

            // Some terminated employees
            createEmployee("Susan", "Turner", "susan.turner@company.com", "+1-555-020-4567",
                         Department.HR, "HR Assistant", new BigDecimal("50000"),
                         LocalDate.of(2021, 6, 1), EmployeeStatus.TERMINATED),

            createEmployee("Mark", "Phillips", "mark.phillips@company.com", "+1-555-060-4567",
                         Department.SALES, "Sales Associate", new BigDecimal("55000"),
                         LocalDate.of(2020, 3, 15), EmployeeStatus.TERMINATED)
        );
    }
    
    /**
     * Helper method to create an employee
     */
    private Employee createEmployee(String firstName, String lastName, String email, String phoneNumber,
                                  Department department, String position, BigDecimal salary, 
                                  LocalDate hireDate, EmployeeStatus status) {
        Employee employee = new Employee();
        employee.setFirstName(firstName);
        employee.setLastName(lastName);
        employee.setEmail(email);
        employee.setPhoneNumber(phoneNumber);
        employee.setDepartment(department);
        employee.setPosition(position);
        employee.setSalary(salary);
        employee.setHireDate(hireDate);
        employee.setStatus(status);
        employee.setIsActive(status != EmployeeStatus.TERMINATED);
        
        return employee;
    }
}


