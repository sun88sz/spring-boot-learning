package com.sun.config;

import com.google.common.collect.Sets;
import com.sun.validation.constraints.*;
import com.sun.validation.constraints.maxmin.Max;
import com.sun.validation.constraints.maxmin.Min;
import io.swagger.annotations.ApiModelProperty;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.Set;

/**
 * @author Sun
 */
@Slf4j
public class LocalCustomerValidatorFactoryBean extends LocalValidatorFactoryBean {

    /**
     * 需要改变注解值的验证类
     */
    private Set<Class> annotationClasses = Sets.newHashSet(NotNull.class, NotEmpty.class, NotBlank.class, Size.class, Length.class, Max.class, Min.class);

    /**
     * 记录已经通过反射改变过注解值的class
     */
    private Set<Class> cacheBeanClasses = Sets.newHashSet();


    /**
     * 检查是否是自定义的验证注解
     *
     * @param anno
     * @return
     */
    private boolean checkIsValidationAnnotation(Annotation anno) {
        for (Class clazz : annotationClasses) {
            if (clazz.isInstance(anno)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void validate(Object target, Errors errors) {
        Class<?> beanClass = target.getClass();
        // 缓存
        if (!cacheBeanClasses.contains(beanClass)) {
            try {
                Field[] declaredFields = beanClass.getDeclaredFields();
                for (Field field : declaredFields) {
                    ApiModelProperty annotation = field.getAnnotation(ApiModelProperty.class);
                    if (annotation != null) {
                        String name = annotation.name();
                        if (StringUtils.isNotEmpty(name)) {
                            Annotation[] declaredAnnotations = field.getDeclaredAnnotations();
                            for (Annotation anno : declaredAnnotations) {
                                if (checkIsValidationAnnotation(anno)) {
                                    //获取 foo 这个代理实例所持有的 InvocationHandler
                                    InvocationHandler h = Proxy.getInvocationHandler(anno);
                                    // 获取 AnnotationInvocationHandler 的 memberValues 字段
                                    Field hField = h.getClass().getDeclaredField("memberValues");
                                    // 因为这个字段事 private final 修饰，所以要打开权限
                                    hField.setAccessible(true);
                                    // 获取 memberValues
                                    Map memberValues = (Map) hField.get(h);
                                    // 修改 value 属性值
                                    String property = (String) memberValues.get("property");
                                    if (StringUtils.isEmpty(property)) {
                                        memberValues.put("property", name);
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                log.error("反射设置Validation注解值错误", e);
            }
            cacheBeanClasses.add(beanClass);
        }
        super.validate(target, errors);
    }
}
