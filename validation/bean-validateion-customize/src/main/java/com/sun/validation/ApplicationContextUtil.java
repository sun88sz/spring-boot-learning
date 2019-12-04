package com.sun.validation;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

@Deprecated
@Component
public class ApplicationContextUtil implements ApplicationContextAware {

    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext arg0) throws BeansException {
        context = arg0;
    }

    @SuppressWarnings("unchecked")
    public static <T> T getBean(Class<?> clazz) {
        return (T) context.getBean(clazz);
    }

    public static Object getBean(String name) {
        return context.getBean(name);
    }

    public static String getWebRealPath(String dir) {
        return ((WebApplicationContext) context).getServletContext().getRealPath(dir);
    }

    public static String getContextName() {
        return ((WebApplicationContext) context).getServletContext().getContextPath();
    }

    public static void publish(ApplicationEvent e) {
        context.publishEvent(e);
    }

}