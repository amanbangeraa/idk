package com.employeems.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

/**
 * Custom validation annotation for phone number format
 */
@Documented
@Constraint(validatedBy = PhoneNumberValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPhoneNumber {
    String message() default "Invalid phone number format. Use format: +1-555-123-4567 or 555-123-4567";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}


