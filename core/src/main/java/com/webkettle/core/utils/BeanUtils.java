package com.webkettle.core.utils;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class BeanUtils {


    /**
     * 是否功德圆满
     * @param o
     *          等待验证的对象
     * @return
     */
    public static boolean isFull(Object o){
        if (null == o){
            return false;
        }
        if (o instanceof Serializable){
            return "".equals(o.toString().trim());
        }
        Field[] declaredFields = o.getClass().getDeclaredFields();
        for (Field field: declaredFields){
            PropertyDescriptor propertyDescriptor = null;
            try {
                propertyDescriptor = new PropertyDescriptor(field.getName(), o.getClass());
            }catch (IntrospectionException e){
                throw new RuntimeException("解析实体类异常", e);
            }
            Method readMethod = propertyDescriptor.getReadMethod();
            try {
                Object invoke = readMethod.invoke(o);
                if (!isFull(invoke)){
                    return false;
                }
            } catch (IllegalAccessException|InvocationTargetException e) {
                throw new RuntimeException("不标准的getter/setter方法", e);
            }
        }
        return true;
    }

}
