package com.employeems.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.math.BigDecimal;

/**
 * Validator implementation for ValidSalaryRange annotation
 */
public class SalaryRangeValidator implements ConstraintValidator<ValidSalaryRange, BigDecimal> {
    
    private double min;
    private double max;

    @Override
    public void initialize(ValidSalaryRange constraintAnnotation) {
        this.min = constraintAnnotation.min();
        this.max = constraintAnnotation.max();
    }

    @Override
    public boolean isValid(BigDecimal salary, ConstraintValidatorContext context) {
        if (salary == null) {
            return true; // Allow null values, use @NotNull if required
        }
        
        double salaryValue = salary.doubleValue();
        return salaryValue >= min && salaryValue <= max;
    }
}


