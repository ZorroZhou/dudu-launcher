package com.wow.frame.repertory.dbTool.beanTool;


import com.wow.carlauncher.common.util.CommonUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 属性信息,现在只包含 field,名称,读取方法,写入方法
 *
 * @author Soap
 */
public class PropertyInfo {

    private static final String GET = "get";
    private static final String SET = "set";
    private static final String IS = "is";

    private Method readMethod;
    private Method writeMethod;
    private Field field;
    private String name;

    //初始化
    protected PropertyInfo(Class<?> clazz, Field field) {
        this.field = field;
        Method[] methods = BeanInfo.declaredMethodCache.get(clazz);
        Class<?> type = field.getType();
        name = field.getName();
        String fileName = CommonUtil.convertFristToUpperCase(name);
        String readMethodName = null;
        if (type == boolean.class || type == null) {
            readMethodName = PropertyInfo.IS + fileName;
        } else {
            readMethodName = PropertyInfo.GET + fileName;
        }

        String writeMethodName = PropertyInfo.SET + fileName;

        for (Method method : methods) {
            if (readMethodName.equals(method.getName())) {
                readMethod = method;
            }
            if (writeMethodName.equals(method.getName())) {
                writeMethod = method;
            }
            if (readMethod != null && writeMethod != null) {
                break;
            }
        }
    }

    public String getName() {
        return name;
    }

    public Method getReadMethod() {
        return readMethod;
    }

    public Method getWriteMethod() {
        return writeMethod;
    }

    public Field getField() {
        return field;
    }
}
