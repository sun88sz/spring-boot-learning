/*
 * Hibernate Validator, declare and validate application constraints
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package com.sun.validation.constraintvalidation.maxmin;

import com.sun.validation.CustomerConstraintValidator;
import com.sun.validation.constraints.maxmin.Min;

import javax.validation.ConstraintValidatorContext;

/**
 * Check that the number being validated is greater than or equal to the minimum
 * value specified.
 *
 * @author Alaa Nassef
 * @author Hardy Ferentschik
 * @author Xavier Sosnovsky
 * @author Marko Bekhta
 */
public abstract class AbstractMinValidator<T> implements CustomerConstraintValidator<Min, T> {

	protected double minValue;

	@Override
	public void initialize(Min maxValue) {
		this.minValue = maxValue.value();
	}

	@Override
	public boolean doIsValid(T value, ConstraintValidatorContext constraintValidatorContext) {
		// null values are valid
		if ( value == null ) {
			return true;
		}

		return compare( value ) >= 0;
	}

	protected abstract int compare(T number);
}
