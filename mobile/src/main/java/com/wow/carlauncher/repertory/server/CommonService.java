package com.wow.carlauncher.repertory.server;

import com.wow.carlauncher.ex.manage.okHttp.OkHttpManage;
import com.wow.carlauncher.ex.manage.okHttp.ProgressResponseListener;
import com.wow.carlauncher.repertory.server.response.GetUpdateResult;
import com.wow.carlauncher.repertory.server.response.LoginResult;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

public class CommonService {

    private final static String GET_UPDATE = "api/app/common/getUpdate/[TYPE]";
    private final static String GET_UPDATE_TYPE = "[TYPE]";

    public static Call getUpdate(int type, final CommonCallback<GetUpdateResult.AppUpdate> commonCallback) {
        return ServerRequestUtil.get(ServerConstant.SERVER_URL + GET_UPDATE.replace(GET_UPDATE_TYPE, type + ""), GetUpdateResult.class, commonCallback);
    }

    private final static String GET_LOGIN = "api/app/common/login";

    public static Call login(String accessToken, String nickname, String userPic, final CommonCallback<LoginResult.LoginInfo> commonCallback) {
        Map<String, Object> param = new HashMap<>();
        param.put("accessToken", accessToken);
        param.put("nickname", nickname);
        param.put("userPic", userPic);
        return ServerRequestUtil.post(ServerConstant.SERVER_URL + GET_LOGIN, param, LoginResult.class, commonCallback);
    }

    private final static String GET_LOGIN_BY_TOKEN = "api/app/common/loginByToken";

    public static Call loginByToken(String token, final CommonCallback<LoginResult.LoginInfo> commonCallback) {
        Map<String, Object> param = new HashMap<>();
        param.put("token", token);
        return ServerRequestUtil.post(ServerConstant.SERVER_URL + GET_LOGIN_BY_TOKEN, param, LoginResult.class, commonCallback);
    }

    public static Call downFile(String url, final ProgressResponseListener commonCallback) {
        return OkHttpManage.self().get(url, commonCallback);
    }
}
