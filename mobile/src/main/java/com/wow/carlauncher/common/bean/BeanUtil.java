package com.wow.carlauncher.common.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liuyixian on 15/11/15.
 */
public class BeanUtil {
    public static Map<String, Object> bean2map(Object bean) {
        try {
            BeanInfo info = BeanManage.self().getBeanInfo(bean.getClass());
            if (info == null) {
                return null;
            }
            PropertyInfo[] pis = info.getPropertyInfos();
            Map<String, Object> returnMap = new HashMap<String, Object>();
            for (PropertyInfo pi : pis) {
                String propertyName = pi.getName();
                returnMap.put(propertyName, pi.getReadMethod().invoke(bean));
            }
            return returnMap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T toBean(Class<T> clazz, Object map) {
        if (map instanceof Map) {
            try {
                Map<String, Object> bb = (Map<String, Object>) map;
                return map2bean(clazz, bb);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static <T> List<T> toList(Class<T> clazz, Object list) {
        List<T> res = new ArrayList<>();
        if (list instanceof List) {
            List l = (List) list;
            for (Object obj : l) {
                try {
                    res.add(map2bean(clazz, (Map<String, Object>) obj));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
        return res;
    }

    public static <T> T map2bean(Class<T> clazz, Map<String, Object> map) {
        // 查询
        try {
            BeanInfo info = BeanManage.self().getBeanInfo(clazz);
            if (info == null) {
                return null;
            }
            PropertyInfo[] pis = info.getPropertyInfos();
            T obj = clazz.newInstance();
            for (PropertyInfo pi : pis) {
                String propertyName = pi.getName();
                if (map.containsKey(propertyName)) {
                    Object value = map.get(propertyName);
                    pi.getWriteMethod().invoke(obj, getValueByType(pi.getField().getType(), value));
                }
            }
            return obj;
        } catch (Exception e) {

        }
        return null;
    }

    public static Object getValueByType(Class<?> clazz, Object value) {
        if (clazz == null || value == null) {
            return null;
        }
        if (clazz.equals(Integer.class) || clazz.equals(int.class)) {
            return Integer.parseInt(value + "");
        } else if (clazz.equals(Double.class) || clazz.equals(double.class)) {
            return Double.parseDouble(value + "");
        } else if (clazz.equals(Float.class) || clazz.equals(float.class)) {
            return Float.parseFloat(value + "");
        } else if (clazz.equals(Long.class) || clazz.equals(long.class)) {
            return Long.parseLong(value + "");
        } else if (clazz.equals(Short.class) || clazz.equals(short.class)) {
            return Short.parseShort(value + "");
        } else if (clazz.equals(String.class)) {
            return String.valueOf(value);
        } else {
            return null;
        }

    }
}
