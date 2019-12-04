package com.sun.validation.constraintvalidation;

import com.sun.validation.AbstractValidator;
import com.sun.validation.constraints.NotNull;

import javax.validation.ConstraintValidatorContext;

/**
 *
 */
public class NotNullValidator extends AbstractValidator<NotNull, Object> {

    @Override
    public boolean doIsValid(Object value, ConstraintValidatorContext cc) {
        return value != null;
    }
}
