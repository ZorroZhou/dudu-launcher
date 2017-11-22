package com.wow.frame.repertory.dbTool.beanTool;

import java.util.Map;
import java.util.WeakHashMap;

/**
 * bean管理器,单例,维护一个bean缓存池
 *
 * @author Soap
 */
public class BeanManage {
    private static BeanManage manage;

    public static BeanManage self() {
        if (manage == null) {
            //同步代码块（对象未初始化时，使用同步代码块，保证多线程访问时对象在第一次创建后，不再重复被创建）
            synchronized (BeanManage.class) {
                //未初始化，则初始instance变量
                if (manage == null) {
                    manage = new BeanManage();
                }
            }
        }
        return manage;
    }


    private Map<Class<?>, BeanInfo> beanInfoCache;

    private BeanManage() {
        beanInfoCache = new WeakHashMap<Class<?>, BeanInfo>();
    }


    public BeanInfo getBeanInfo(Class<?> clazz) {
        BeanInfo beanInfo = this.beanInfoCache.get(clazz);
        if (beanInfo == null) {
            beanInfo = new BeanInfo(clazz);
            this.beanInfoCache.put(clazz, beanInfo);
        }
        return beanInfo;
    }

    public void removeBeanInfo(Class<?> clazz) {
        if (this.beanInfoCache != null) {
            this.beanInfoCache.remove(clazz);
        }
    }

    public void clearBeanInfoCache() {
        if (this.beanInfoCache != null) {
            this.beanInfoCache.clear();
        }
    }
}
