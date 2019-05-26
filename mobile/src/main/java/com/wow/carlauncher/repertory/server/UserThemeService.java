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
        return ServerRequestUtil.post(ServerConstant.SERVER_URL + GET_PAGE, param, ThemePageResponse.class, commonCallback);
    }
}
