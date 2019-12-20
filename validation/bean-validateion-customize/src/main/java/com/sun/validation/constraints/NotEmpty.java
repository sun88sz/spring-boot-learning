/*
 * Bean Validation API
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package com.sun.validation.constraints;

import com.sun.validation.constraintvalidation.notempty.*;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * The annotated element must not be {@code null} nor empty. Supported types are:
 * <ul>
 * <li>{@code CharSequence} (length of character sequence is evaluated)</li>
 * <li>{@code Collection} (collection size is evaluated)</li>
 * <li>{@code Map} (map size is evaluated)</li>
 * <li>Array (array length is evaluated)</li>
 * </ul>
 *
 * @author Emmanuel Bernard
 * @author Hardy Ferentschik
 * @since 2.0
 */
@Documented
@Constraint(validatedBy = {NotEmptyValidatorForArray.class,
        NotEmptyValidatorForArraysOfBoolean.class,
        NotEmptyValidatorForArraysOfByte.class,
        NotEmptyValidatorForArraysOfChar.class,
        NotEmptyValidatorForArraysOfDouble.class,
        NotEmptyValidatorForArraysOfFloat.class,
        NotEmptyValidatorForArraysOfInt.class,
        NotEmptyValidatorForArraysOfLong.class,
        NotEmptyValidatorForArraysOfShort.class,
        NotEmptyValidatorForCharSequence.class,
        NotEmptyValidatorForCollection.class,
        NotEmptyValidatorForMap.class})
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Repeatable(NotEmpty.List.class)
public @interface NotEmpty {

    String property();

    String message() default "{validation.default.NotEmpty.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * Defines several {@code @NotEmpty} constraints on the same element.
     *
     * @see NotEmpty
     */
    @Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
    @Retention(RUNTIME)
    @Documented
    public @interface List {
        NotEmpty[] value();
    }
}
