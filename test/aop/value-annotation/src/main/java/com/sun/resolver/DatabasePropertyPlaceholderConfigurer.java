package com.sun.resolver;

import java.util.Properties;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.util.StringValueResolver;

/**
 * @author Sun
 */
public class DatabasePropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {
    
    @Override
    protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties props)
            throws BeansException {

        StringValueResolver valueResolver = new DatabaseStringValueResolver();
        doProcessProperties(beanFactoryToProcess, valueResolver);
    }
}