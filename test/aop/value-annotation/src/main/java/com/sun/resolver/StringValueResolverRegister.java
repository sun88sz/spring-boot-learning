package com.sun.resolver;

import java.util.concurrent.atomic.AtomicBoolean;

import com.sun.constant.ConstantServiceUtil;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

/**
 * @author Sun
 */
public class StringValueResolverRegister extends InstantiationAwareBeanPostProcessorAdapter {

    @Autowired
    private DefaultListableBeanFactory beanFactory;


    private AtomicBoolean registerLock = new AtomicBoolean(false);


    /**
     * 在实例化service等bean之前 ， 先实例化 DatabaseStringValueResolver
     *
     * @param beanClass
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
        if (beanFactory.isConfigurationFrozen() && registerLock.compareAndSet(false, true)) {
            // 先初始化 ConstantProperties
            beanFactory.getBean(ConstantServiceUtil.class);
            //  placeholderConfigurerSupport.setIgnoreUnresolvablePlaceholders(true);
        }
        return super.postProcessBeforeInstantiation(beanClass, beanName);
    }

}
