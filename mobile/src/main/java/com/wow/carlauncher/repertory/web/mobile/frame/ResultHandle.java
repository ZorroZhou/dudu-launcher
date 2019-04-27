package com.wow.carlauncher.repertory.web.mobile.frame;

import com.wow.carlauncher.repertory.web.mobile.packet.Response;

/**
 * Created by 10124 on 2017/7/27.
 * 基础结果处理接口，默认会先走这个接口处理结果
 */
public interface ResultHandle {
    <T> String handle(Response<T> t);
}
