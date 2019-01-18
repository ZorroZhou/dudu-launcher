package com.wow.frame.repertory.remote;

import java.lang.reflect.Type;

/**
 * Created by 10124 on 2017/7/27.
 * 基础结果处理接口，默认会先走这个接口处理结果
 */
public interface ResultHandle {
    <T> String handle(T t);
}
