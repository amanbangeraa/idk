package com.employeems.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

/**
 * Validator implementation for ValidPhoneNumber annotation
 */
public class PhoneNumberValidator implements ConstraintValidator<ValidPhoneNumber, String> {
    
    private static final Pattern PHONE_PATTERN = Pattern.compile(
        "^\\+?1?[-.\\s]?\\(?([0-9]{3})\\)?[-.\\s]?([0-9]{3})[-.\\s]?([0-9]{4})$"
    );

    @Override
    public boolean isValid(String phoneNumber, ConstraintValidatorContext context) {
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            return true; // Allow null/empty values, use @NotNull if required
        }
        
        return PHONE_PATTERN.matcher(phoneNumber.trim()).matches();
    }
}


