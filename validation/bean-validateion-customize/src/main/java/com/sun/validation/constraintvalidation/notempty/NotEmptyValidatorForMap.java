/*
 * Hibernate Validator, declare and validate application constraints
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package com.sun.validation.constraintvalidation.notempty;

import com.sun.validation.CustomerConstraintValidator;
import com.sun.validation.constraints.NotEmpty;

import javax.validation.ConstraintValidatorContext;
import java.util.Map;

/**
 * Check that the map is not null and not empty.
 *
 * @author Guillaume Smet
 */
// as per the JLS, Map<?, ?> is a subtype of Map, so we need to explicitly reference
// Map here to support having properties defined as Map (see HV-1551)
@SuppressWarnings("rawtypes")
public class NotEmptyValidatorForMap implements CustomerConstraintValidator<NotEmpty, Map> {

	/**
	 * Checks the map is not {@code null} and not empty.
	 *
	 * @param map the map to validate
	 * @param constraintValidatorContext context in which the constraint is evaluated
	 * @return returns {@code true} if the map is not {@code null} and the map is not empty
	 */
	@Override
	public boolean doIsValid(Map map, ConstraintValidatorContext constraintValidatorContext) {
		if ( map == null ) {
			return false;
		}
		return map.size() > 0;
	}
}
