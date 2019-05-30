package com.wow.carlauncher.repertory.server;

import com.wow.carlauncher.repertory.server.response.ThemePageResponse;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

public class UserThemeService {

    private final static String GET_PAGE = "api/app/userTheme/getPage";

    public static Call getPage(int page, int rows, final CommonCallback<ThemePageResponse> commonCallback) {
        Map<String, Object> param = new HashMap<>();
        param.put("page", page);
        param.put("rows", rows);
        return ServerRequestUtil.post(GET_PAGE, param, ThemePageResponse.class, commonCallback);
    }

    private final static String GET_URL = "api/app/userTheme/getUrl/[ID]";
    private final static String GET_URL_ID = "[ID]";

    public static Call getUrl(long id, final CommonCallback<String> commonCallback) {
        return ServerRequestUtil.get(GET_URL.replace(GET_URL_ID, id + ""), String.class, commonCallback);
    }
}
