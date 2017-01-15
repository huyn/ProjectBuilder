package com.huyn.projectbuilder.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by huyaonan on 16/4/6.
 */
public class ReflectUtil {

    public static void setValueByInflect(Object object, String attributeName, Object value) {
        Field field = null;
        try {
            Class clazz = object.getClass();
            for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
                try {
                    field = clazz.getDeclaredField(attributeName);
                    field.setAccessible(true);
                    field.set(object, value);
                    field.setAccessible(false);
                    return;
                } catch (Exception e) {
                    //这里甚么都不要做！并且这里的异常必须这样写，不能抛出去。
                    //如果这里的异常打印或者往外抛，则就不会执行clazz = clazz.getSuperclass(),最后就不会进入到父类中了
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Object invoke(final Object obj, final String methodName, final Class[] classes, final Object[] objects) {
        try {
            Method method = getMethod(obj.getClass(), methodName, classes);
            method.setAccessible(true);// 调用private方法的关键一句话
            return method.invoke(obj, objects);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Method getMethod(Class clazz, String methodName, final Class[] classes) throws Exception {
        Method method = null;

        for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
            try {
                method = clazz.getDeclaredMethod(methodName, classes);
                return method;
            } catch (Exception e) {
                //这里甚么都不要做！并且这里的异常必须这样写，不能抛出去。
                //如果这里的异常打印或者往外抛，则就不会执行clazz = clazz.getSuperclass(),最后就不会进入到父类中了
            }
        }

        return null;
//        try {
//            method = clazz.getDeclaredMethod(methodName, classes);
//        } catch (NoSuchMethodException e) {
//            try {
//                method = clazz.getMethod(methodName, classes);
//            } catch (NoSuchMethodException ex) {
//                if (clazz.getSuperclass() == null) {
//                    return method;
//                } else {
//                    method = getMethod(clazz.getSuperclass(), methodName,
//                            classes);
//                }
//            }
//        }
//        return method;
    }

    public static Object invoke(final Object obj, final String methodName, final Class[] classes) {
        return invoke(obj, methodName, classes, new Object[]{});
    }

    public static Object invoke(final Object obj, final String methodName) {
        return invoke(obj, methodName, new Class[]{}, new Object[]{});
    }

    public static Object invokeConstructor(Class clazz, final Class[] classes, final Object[] objects) {
        try {
            Constructor<?> localConstructor = clazz.getConstructor(classes);
            return localConstructor.newInstance(objects);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
