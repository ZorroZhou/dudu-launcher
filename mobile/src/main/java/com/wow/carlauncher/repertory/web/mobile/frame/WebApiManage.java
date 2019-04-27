package com.wow.carlauncher.repertory.web.mobile.frame;

import android.content.Context;
import android.util.Log;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by 10124 on 2017/7/27.
 */
public class WebApiManage {
    private final static String TAG = "frame.DatabaseManage";

    private static boolean inited = false;

    private WebApiManage() {
    }

    //所有接口
    private static Map<Class<?>, BaseApi> wservice;

    private static JsonConverter jsonConverter = new JsonResponseParser();

    private static Map<String, String> defaultHeadFeild;

    public static Map<String, String> getDefaultHeadFeild() {
        return defaultHeadFeild;
    }

    public static synchronized void init(Context context, WebApiInfo info) {
        if (!inited) {
            inited = true;
            wservice = new ConcurrentHashMap<>();

            defaultHeadFeild = new ConcurrentHashMap<>();

            for (Class<?> clazz : info.getInterFaceClass()) {
                Object sw = null;
                try {
                    sw = clazz.newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (sw instanceof BaseApi) {
                    BaseApi webService = (BaseApi) sw;
                    webService.setContext(context);
                    webService.setJsonConverter(jsonConverter);
                    webService.setDefaultResultHandle(info.getDefaultResultHandle());
                    webService.setServer(info.getServerUrl());
                    wservice.put(clazz, webService);
                }

            }
        }
    }

    public static <T extends BaseApi> T getApi(Class<T> clazz) {
        Log.e(TAG, "getService: " + wservice);
        if (wservice.containsKey(clazz)) {
            return (T) wservice.get(clazz);
        } else {
            throw new NullPointerException();
        }
    }
}
