/*
 * Hibernate Validator, declare and validate application constraints
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package com.sun.validation.constraintvalidation.decimal;

import java.math.BigDecimal;

/**
 * Check that the number being validated is greater than or equal to the minimum
 * value specified.
 *
 * @author Marko Bekhta
 */
public class DecimalMinValidatorForBigDecimal extends CustomerConstraintDecimalMinValidator<BigDecimal> {

	@Override protected int compare(BigDecimal number) {
		return DecimalNumberComparatorHelper.compare( number, minValue );
	}
}
