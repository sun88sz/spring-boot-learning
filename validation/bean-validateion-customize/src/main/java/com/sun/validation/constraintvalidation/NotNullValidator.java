package com.sun.validation.constraintvalidation;

import com.sun.validation.CustomerConstraintValidator;
import com.sun.validation.constraints.NotNull;

import javax.validation.ConstraintValidatorContext;

/**
 *
 */
public class NotNullValidator implements CustomerConstraintValidator<NotNull, Object> {

    @Override
    public boolean doIsValid(Object value, ConstraintValidatorContext cc) {
        return value != null;
    }
}
