package com.wow.musicapi;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by haohua on 2018/2/8.
 */
public class JsonArrayParseDemo {
    public static void main(String[] args) {
        // json内容: [{"song_name": "song1"}, {"song_name": "song2"}]
        String json = "[{\"song_name\": \"song1\"}, {\"song_name\": \"song2\"}]";
        // json array到java list的转换
        List<TestSong> songs = jsonToJavaListDemo(json, TestSong.class);
        System.out.println(songs);
    }

    /**
     * 自己徒手撸一个带范型版本的,从json array到java list的解析器
     * 借用fastjson的原始JSONField注解
     * 为了方便还使用了fastjson的原始解析方法
     * 注: 没有考虑很多类型条件边界条件，以及getter, setter，也就是property
     *
     * @param json
     * @param clazz
     * @param <T>
     * @return
     */
    private static <T> List<T> jsonToJavaListDemo(String json, Class<T> clazz) {
        HashMap<String, Field> fieldsWithJsonAnnotation = new HashMap<>();
        Field[] fields = clazz.getDeclaredFields();
        // 循环找到T这个类里面所有@JSONField标记过的所有成员变量，保存到fieldsWithJsonAnnotation里
        for (Field field : fields) {
            JSONField jsonFieldAnnotation = field.getAnnotation(JSONField.class);
            if (jsonFieldAnnotation != null) {
                String jsonName = jsonFieldAnnotation.name();
                fieldsWithJsonAnnotation.put(jsonName, field);
            }
        }
        final ArrayList<T> result = new ArrayList<>();
        JSONArray jsonArray = JSONArray.parseArray(json);
        // 外层循环: 对jsonArray进行遍历
        for (int i = 0; i < jsonArray.size(); i++) {
            // 这里jsonArray的当前元素不一定是JSONObject, 还可能是基本值类型
            // 真实场景需要运行时判断类型，此处为了简单假定一定是JSONObject
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            if (jsonObject != null) {
                try {
                    // 反射调用T类型的构造函数
                    Constructor<T> constructor = clazz.getDeclaredConstructor();
                    T objectItem = constructor.newInstance();
                    // 内层循环: 对每一个field进行遍历
                    for (Map.Entry<String, Field> entry : fieldsWithJsonAnnotation.entrySet()) {
                        final String jsonKey = entry.getKey();
                        final Field field = entry.getValue();
                        Object jValue = jsonObject.get(jsonKey);
                        // 判断json中的类型和类成员声明的类型是否相同，否则无法赋值
                        if (jValue.getClass() == field.getType()) {
                            // 设置private也可访问
                            field.setAccessible(true);
                            try {
                                // 使用反射给当前field赋值
                                field.set(objectItem, jValue);
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    // add进结果列表
                    result.add(objectItem);
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
        // 返回最终解析和转换后的结果列表
        return result;
    }

    static class TestSong {
        @SuppressWarnings("unused")
        @SerializedName("song_name")
        public String songName;

        @SuppressWarnings("unused")
        public transient String trans;
    }
}
