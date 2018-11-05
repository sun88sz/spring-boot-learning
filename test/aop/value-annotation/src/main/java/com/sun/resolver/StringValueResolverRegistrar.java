package com.sun.resolver;

import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

/**
 * 
 * @author Sun
 */
public class StringValueResolverRegistrar extends InstantiationAwareBeanPostProcessorAdapter {

    @Autowired
    private DefaultListableBeanFactory beanFactory;

    private AtomicBoolean registerLock = new AtomicBoolean(false);

    @Override
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
        if (beanFactory.isConfigurationFrozen() && registerLock.compareAndSet(false, true)) {
            beanFactory.getBean(CustomizedStringValueResolver.class);
        }
        return super.postProcessBeforeInstantiation(beanClass, beanName);
    }
}
