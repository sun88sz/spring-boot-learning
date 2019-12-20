/*
 * Hibernate Validator, declare and validate application constraints
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package com.sun.validation.constraintvalidation.decimal;

import com.sun.validation.CustomerConstraintValidator;
import com.sun.validation.constraints.decimal.DecimalMin;
import org.hibernate.validator.internal.util.logging.Log;
import org.hibernate.validator.internal.util.logging.LoggerFactory;

import javax.validation.ConstraintValidatorContext;
import java.lang.invoke.MethodHandles;
import java.math.BigDecimal;

/**
 * Check that the number being validated is greater than or equal to the minimum
 * value specified.
 *
 * @author Hardy Ferentschik
 * @author Xavier Sosnovsky
 * @author Marko Bekhta
 */
public abstract class CustomerConstraintDecimalMinValidator<T> implements CustomerConstraintValidator<DecimalMin, T> {

	private static final Log LOG = LoggerFactory.make( MethodHandles.lookup() );

	protected BigDecimal minValue;
	private boolean inclusive;

	@Override
	public void initialize(DecimalMin minValue) {
		try {
			this.minValue = new BigDecimal( minValue.value() );
		}
		catch (NumberFormatException nfe) {
			throw LOG.getInvalidBigDecimalFormatException( minValue.value(), nfe );
		}
		this.inclusive = minValue.inclusive();
	}

	@Override
	public boolean doIsValid(T value, ConstraintValidatorContext context) {
		// null values are valid
		if ( value == null ) {
			return true;
		}

		int comparisonResult = compare( value );
		return inclusive ? comparisonResult >= 0 : comparisonResult > 0;
	}

	protected abstract int compare(T number);
}
