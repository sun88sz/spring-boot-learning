package com.sun.validation.constraints;

import com.sun.validation.constraintvalidation.size.*;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * The annotated element size must be between the specified boundaries (included).
 * <p/>
 * Supported types are:
 * <ul>
 *     <li>{@code CharSequence} (length of character sequence is evaluated)</li>
 *     <li>{@code Collection} (collection size is evaluated)</li>
 *     <li>{@code Map} (map size is evaluated)</li>
 *     <li>Array (array length is evaluated)</li>
 * </ul>
 * <p/>
 * {@code null} elements are considered valid.
 *
 * @author Emmanuel Bernard
 */
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {SizeValidatorForArray.class,
        SizeValidatorForArraysOfBoolean.class,
        SizeValidatorForArraysOfByte.class,
        SizeValidatorForArraysOfChar.class,
        SizeValidatorForArraysOfDouble.class,
        SizeValidatorForArraysOfFloat.class,
        SizeValidatorForArraysOfInt.class,
        SizeValidatorForArraysOfLong.class,
        SizeValidatorForArraysOfShort.class,
        SizeValidatorForCharSequence.class,
        SizeValidatorForCollection.class,
        SizeValidatorForMap.class})
public @interface Size {
    String property() default "";

    String message() default "{validation.default.Size.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * @return size the element must be higher or equal to
     */
    int min() default 0;

    /**
     * @return size the element must be lower or equal to
     */
    int max() default Integer.MAX_VALUE;

    /**
     * Defines several {@link Size} annotations on the same element.
     *
     * @see Size
     */
    @Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
    @Retention(RUNTIME)
    @Documented
    @interface List {

        Size[] value();
    }
}
