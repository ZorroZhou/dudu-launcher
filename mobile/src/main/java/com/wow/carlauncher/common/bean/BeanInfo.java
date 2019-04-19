package com.wow.carlauncher.common.bean;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * bean信息,里面暂时只是包含了bean的所有属性信息,有一个方法的弱缓存
 *
 * @author Soap
 */
public class BeanInfo {
    //方法缓存
    protected static Map<Class<?>, Method[]> declaredMethodCache = new WeakHashMap<Class<?>, Method[]>();
    //属性信息缓存
    protected static Map<Class<?>, PropertyInfo[]> declaredFieldCache = new WeakHashMap<Class<?>, PropertyInfo[]>();

    private Class<?> clazz;

    protected BeanInfo(Class<?> clazz) {
        this.clazz = clazz;
        declaredMethodCache.put(clazz, getMethods(clazz));
        declaredFieldCache.put(clazz, getPropertyInfos(clazz));
    }

    //获取这个bean的属性信息
    public PropertyInfo[] getPropertyInfos() {
        PropertyInfo[] propertyInfo = null;
        propertyInfo = declaredFieldCache.get(clazz);
        if (propertyInfo == null) {
            propertyInfo = getPropertyInfos(clazz);
        }
        return propertyInfo;
    }

    //静态方法,获取一个bean封装的属性信息,通过反射
    public static <T> PropertyInfo[] getPropertyInfos(Class<T> clazz) {
        List<PropertyInfo> ps = new ArrayList<PropertyInfo>();
        Field[] fields = getFields(clazz);
        for (Field field : fields) {
            if (field.getName().indexOf("$") > -1 || Modifier.isStatic(field.getModifiers())) {
                continue;
            }
            PropertyInfo info = new PropertyInfo(clazz, field);
            ps.add(info);
        }
        return ps.toArray(new PropertyInfo[ps.size()]);
    }

    //静态方法,获取一个bean封装的Field信息,通过反射
    public static <T> Field[] getFields(Class<T> clazz) {
        List<Field> fleids = new ArrayList<Field>();
        for (Class<?> c = clazz; c != Object.class; c = c.getSuperclass()) {
            fleids.addAll(Arrays.asList(c.getDeclaredFields()));
        }
        return fleids.toArray(new Field[fleids.size()]);
    }

    //静态方法,获取一个bean封装的Method信息,通过反射
    public static <T> Method[] getMethods(Class<T> clazz) {
        List<Method> methods = new ArrayList<Method>();
        for (Class<?> c = clazz; c != Object.class; c = c.getSuperclass()) {
            methods.addAll(Arrays.asList(c.getDeclaredMethods()));
        }
        return methods.toArray(new Method[methods.size()]);
    }
}
