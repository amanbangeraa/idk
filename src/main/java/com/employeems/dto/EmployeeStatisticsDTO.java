package com.employeems.dto;

import java.util.Map;

/**
 * DTO for employee statistics
 */
public class EmployeeStatisticsDTO {
    
    private long totalEmployees;
    private long activeEmployees;
    private long inactiveEmployees;
    private Map<String, Object> departmentStats;
    
    public EmployeeStatisticsDTO() {
    }
    
    public EmployeeStatisticsDTO(long totalEmployees, long activeEmployees, long inactiveEmployees, 
                                Map<String, Object> departmentStats) {
        this.totalEmployees = totalEmployees;
        this.activeEmployees = activeEmployees;
        this.inactiveEmployees = inactiveEmployees;
        this.departmentStats = departmentStats;
    }
    
    // Getters and Setters
    public long getTotalEmployees() {
        return totalEmployees;
    }
    
    public void setTotalEmployees(long totalEmployees) {
        this.totalEmployees = totalEmployees;
    }
    
    public long getActiveEmployees() {
        return activeEmployees;
    }
    
    public void setActiveEmployees(long activeEmployees) {
        this.activeEmployees = activeEmployees;
    }
    
    public long getInactiveEmployees() {
        return inactiveEmployees;
    }
    
    public void setInactiveEmployees(long inactiveEmployees) {
        this.inactiveEmployees = inactiveEmployees;
    }
    
    public Map<String, Object> getDepartmentStats() {
        return departmentStats;
    }
    
    public void setDepartmentStats(Map<String, Object> departmentStats) {
        this.departmentStats = departmentStats;
    }
}


