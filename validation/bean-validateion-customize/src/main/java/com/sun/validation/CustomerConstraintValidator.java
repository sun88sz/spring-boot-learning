package com.sun.validation;

import org.hibernate.validator.internal.engine.constraintvalidation.ConstraintValidatorContextImpl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.annotation.Annotation;

/**
 * 这里采用模板的设计模式
 *
 * @param
 */
public interface CustomerConstraintValidator<A extends Annotation, T> extends ConstraintValidator<A, T> {

    /**
     * 初始化具体由实现类实现
     *
     * @param value
     * @param context
     * @return
     */
    @Override
    default boolean isValid(T value, ConstraintValidatorContext context) {
        // 获取验证结果,采用模板方法
        boolean result = doIsValid(value, context);
        // 当验证错误时修改默认信息
        if (!result) {
            // 改变默认提示信息
            if (ConstraintValidatorContextImpl.class.isAssignableFrom(context.getClass())) {
                ConstraintValidatorContextImpl constraintValidatorContext = (ConstraintValidatorContextImpl) context;
                // 获取默认提示信息
                String defaultConstraintMessageTemplate = context.getDefaultConstraintMessageTemplate();

                Object key = constraintValidatorContext.getConstraintDescriptor().getAttributes().get("property");
                // 禁用默认提示信息
                context.disableDefaultConstraintViolation();

                // 设置提示语（在message前面加上key）
                context.buildConstraintViolationWithTemplate(key + defaultConstraintMessageTemplate).addConstraintViolation();
            }
        }

        return result;
    }

    /**
     * 真正验证方法
     *
     * @param value
     * @param context
     * @return
     */
    boolean doIsValid(T value, ConstraintValidatorContext context);
}