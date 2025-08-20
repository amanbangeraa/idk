package com.employeems.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

/**
 * Custom validation annotation for salary range validation
 */
@Documented
@Constraint(validatedBy = SalaryRangeValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidSalaryRange {
    String message() default "Salary must be between $20,000 and $500,000";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    
    double min() default 20000.0;
    double max() default 500000.0;
}


