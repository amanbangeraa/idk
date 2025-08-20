package com.employeems.enums;

/**
 * Enum representing different employment statuses
 */
public enum EmployeeStatus {
    ACTIVE("Active"),
    INACTIVE("Inactive"),
    ON_LEAVE("On Leave"),
    TERMINATED("Terminated");

    private final String displayName;

    EmployeeStatus(String displayName) {
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


