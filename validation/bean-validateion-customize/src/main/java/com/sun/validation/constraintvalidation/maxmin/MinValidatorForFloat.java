/*
 * Hibernate Validator, declare and validate application constraints
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package com.sun.validation.constraintvalidation.maxmin;

import org.hibernate.validator.internal.constraintvalidators.bv.number.InfinityNumberComparatorHelper;

/**
 * Check that the number being validated is greater than or equal to the minimum
 * value specified.
 *
 * @author Marko Bekhta
 */
public class MinValidatorForFloat extends AbstractMinValidator<Float> {

	@Override
	protected int compare(Float number) {
		return NumberComparatorHelper.compare( number, minValue, InfinityNumberComparatorHelper.LESS_THAN );
	}
}
