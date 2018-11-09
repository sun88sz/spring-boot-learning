package com.sun.resolver;


import com.sun.constant.ConstantServiceUtil;
import com.sun.istack.internal.NotNull;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.util.StringValueResolver;

/**
 * 数据库-常量解析器
 * <p>
 * yml, properties配置为第一优先级
 * 找不到 则在数据库常量缓存中去找
 *
 * @author Sun
 */
public class DatabaseStringValueResolver implements StringValueResolver, BeanFactoryAware {
    /**
     * Default placeholder prefix:
     */
    private static final String DEFAULT_PLACEHOLDER_PREFIX = "${";

    /**
     * Default placeholder suffix:
     */
    private static final String DEFAULT_PLACEHOLDER_SUFFIX = "}";

    /**
     * Default value separator:
     */
    private static final String DEFAULT_VALUE_SEPARATOR = ":";


    @Override
    public String resolveStringValue(String strVal) {
        String value = null;
        if (strVal.startsWith(DEFAULT_PLACEHOLDER_PREFIX) && strVal.endsWith(DEFAULT_PLACEHOLDER_SUFFIX)) {
            String key = strVal.substring(2, strVal.length() - 1);

            // 如果有默认值的情况
            int separatorIndex = key.indexOf(DEFAULT_VALUE_SEPARATOR);
            if (separatorIndex != -1) {
                String actualPlaceholder = key.substring(0, separatorIndex);
                String defaultValue = key.substring(separatorIndex + DEFAULT_VALUE_SEPARATOR.length());

                // 获取数据库中的key值
                value = ConstantServiceUtil.getValue(actualPlaceholder);
                if (value == null) {
                    value = defaultValue;
                }
            } else {
                value = ConstantServiceUtil.getValue(key);
            }
        }
        return value == null ? strVal : value;
    }


    /**
     * 添加解析器
     *
     * @param beanFactory
     * @throws BeansException
     */
    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        DefaultListableBeanFactory listableBeanFactory = (DefaultListableBeanFactory) beanFactory;
        listableBeanFactory.addEmbeddedValueResolver(this);
    }
}
