package com.sun.resolver;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.util.StringValueResolver;

/**
 * 
 * @author Sun
 */
public class CustomizedStringValueResolver implements StringValueResolver, BeanFactoryAware {

    @Override
    public String resolveStringValue(String strVal) {
        String value = null;
        if (strVal.startsWith("${") && strVal.endsWith("}")) {
            String key = strVal.substring(2, strVal.length() - 1);
            value = "xxxxxxxxxxxxxxxxxx";
        }
        return value == null ? strVal : value;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        ((DefaultListableBeanFactory) beanFactory).addEmbeddedValueResolver(this);
    }
}
