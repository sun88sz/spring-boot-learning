/*
 * Hibernate Validator, declare and validate application constraints
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package com.sun.validation.constraintvalidation.decimal;

import com.sun.validation.CustomerConstraintValidator;
import com.sun.validation.constraints.decimal.DecimalMax;
import org.hibernate.validator.internal.util.logging.Log;
import org.hibernate.validator.internal.util.logging.LoggerFactory;

import javax.validation.ConstraintValidatorContext;
import java.lang.invoke.MethodHandles;
import java.math.BigDecimal;

/**
 * Check that the number being validated is less than or equal to the maximum
 * value specified.
 *
 * @author Hardy Ferentschik
 * @author Xavier Sosnovsky
 * @author Marko Bekhta
 */
abstract class CustomerConstraintDecimalMaxValidator<T> implements CustomerConstraintValidator<DecimalMax, T> {

	private static final Log LOG = LoggerFactory.make( MethodHandles.lookup() );

	protected BigDecimal maxValue;
	private boolean inclusive;

	@Override
	public void initialize(DecimalMax maxValue) {
		try {
			this.maxValue = new BigDecimal( maxValue.value() );
		}
		catch (NumberFormatException nfe) {
			throw LOG.getInvalidBigDecimalFormatException( maxValue.value(), nfe );
		}
		this.inclusive = maxValue.inclusive();
	}

	@Override
	public boolean doIsValid(T value, ConstraintValidatorContext context) {
		// null values are valid
		if ( value == null ) {
			return true;
		}

		int comparisonResult = compare( value );
		return inclusive ? comparisonResult <= 0 : comparisonResult < 0;
	}

	protected abstract int compare(T number);
}
