package com.wow.carlauncher.repertory.web.mobile;

import java.lang.reflect.Type;

/**
 * Created by 10124 on 2017/7/27.
 * 默认的结果转换
 */
public interface ResultConvertor {
    <T> T convertor(String res, Type typeOfT);
}
