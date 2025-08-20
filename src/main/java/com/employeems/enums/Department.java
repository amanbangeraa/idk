package com.employeems.enums;

/**
 * Enum representing different departments in the organization
 */
public enum Department {
    IT("Information Technology"),
    HR("Human Resources"),
    FINANCE("Finance"),
    MARKETING("Marketing"),
    OPERATIONS("Operations"),
    SALES("Sales");

    private final String displayName;

    Department(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}


