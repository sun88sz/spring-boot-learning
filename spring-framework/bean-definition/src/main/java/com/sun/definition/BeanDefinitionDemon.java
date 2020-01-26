package com.sun.definition;

import com.sun.bean.User;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.GenericBeanDefinition;

public class BeanDefinitionDemon {
    public static void main(String[] args) {

        // 1.
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(User.class);
        builder.addPropertyValue("age", 10).addPropertyValue("name", "xxxxx");

        BeanDefinition beanDefinition = builder.getBeanDefinition();

        // 2.
        GenericBeanDefinition definition = new GenericBeanDefinition();
        definition.setBeanClass(User.class);
        MutablePropertyValues properties = new MutablePropertyValues();
        properties.add("age", 10).add("name", "xxxxx");

        definition.setPropertyValues(properties);
    }
}
