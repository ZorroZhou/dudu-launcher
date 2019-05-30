package com.wow.carlauncher.repertory.server;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

public class UserCarService {

    private final static String REPORT_LOCATION = "api/app/car/reportLocation";

    public static Call reportLocation(String deviceId, String location, final CommonCallback<Object> commonCallback) {
        Map<String, Object> param = new HashMap<>();
        param.put("deviceId", deviceId);
        param.put("location", location);
        return ServerRequestUtil.post(REPORT_LOCATION, param, Object.class, commonCallback);
    }
}
