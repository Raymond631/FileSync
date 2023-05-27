package com.example.filesync.common;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @author Raymond Li
 * @create 2023-05-28 2:15
 * @description
 */
public class ApplicationContextUtil implements ApplicationContextAware{
    private static ApplicationContext ac;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ac = applicationContext;
    }
    public static <T> T getBean(Class<T> clazz) {
        return ac.getBean(clazz);
    }
}
