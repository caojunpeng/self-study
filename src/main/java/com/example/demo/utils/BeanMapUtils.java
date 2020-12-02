package com.example.demo.utils;

import org.springframework.cglib.beans.BeanMap;

import java.util.LinkedHashMap;

public class BeanMapUtils {
    /**
     * Map转对象
     * @param stringObjectMap
     * @param bean
     * @param <T>
     * @return
     */
    public static <T> T mapToBean(LinkedHashMap<String, Object> stringObjectMap, T bean) {
        BeanMap beanMap = BeanMap.create(bean);
        beanMap.putAll(stringObjectMap);
        return bean;
    }

    /**
     * 对象转map
     * @param bean
     * @param <T>
     * @return
     */
    public static <T> LinkedHashMap<String, Object> beanToMap(T bean) {
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        if (bean != null) {
            BeanMap beanMap = BeanMap.create(bean);
            for (Object key : beanMap.keySet()) {
                map.put(key+"", beanMap.get(key));
            }
        }
        return map;
    }
}
