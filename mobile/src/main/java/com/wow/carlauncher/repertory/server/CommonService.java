package com.wow.carlauncher.repertory.server;

import com.wow.carlauncher.ex.manage.okHttp.OkHttpManage;
import com.wow.carlauncher.ex.manage.okHttp.ProgressResponseListener;
import com.wow.carlauncher.repertory.server.response.AppUpdateRes;
import com.wow.carlauncher.repertory.server.response.LoginResult;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

public class CommonService {
    public static final int APP_TYPE_LAUNCHER = 0;
    public static final int APP_TYPE_MUSIC = 1;
    public static final int APP_TYPE_FM = 2;

    public static final int UPDATE_TYPE_DEBUG = 1;
    public static final int UPDATE_TYPE_RELEASE = 2;

    private final static String GET_UPDATE = "api/app/common/getUpdate/[TYPE]";
    private final static String GET_UPDATE_TYPE = "[TYPE]";

    public static Call getUpdate(int type, final CommonCallback<AppUpdateRes> commonCallback) {
        return ServerRequestUtil.get(GET_UPDATE.replace(GET_UPDATE_TYPE, type + ""), AppUpdateRes.class, commonCallback);
    }

    private final static String GET_APP_UPDATE = "api/app/common/getUpdate/[TYPE]/[APP_TYPE]";
    private final static String GET_APP_UPDATE_TYPE = "[TYPE]";
    private final static String GET_APP_UPDATE_APP_TYPE = "[APP_TYPE]";

    public static Call getUpdate(int type, int apptype, final CommonCallback<AppUpdateRes> commonCallback) {
        return ServerRequestUtil.get(GET_APP_UPDATE.replace(GET_APP_UPDATE_TYPE, type + "").replace(GET_APP_UPDATE_APP_TYPE, apptype + ""), AppUpdateRes.class, commonCallback);
    }

    private final static String GET_LOGIN = "api/app/common/login";

    public static Call login(String accessToken, String nickname, String userPic, final CommonCallback<LoginResult> commonCallback) {
        Map<String, Object> param = new HashMap<>();
        param.put("accessToken", accessToken);
        param.put("nickname", nickname);
        param.put("userPic", userPic);
        return ServerRequestUtil.post(GET_LOGIN, param, LoginResult.class, commonCallback);
    }

    private final static String GET_LOGIN_BY_TOKEN = "api/app/common/loginByToken";

    public static Call loginByToken(String token, final CommonCallback<LoginResult> commonCallback) {
        Map<String, Object> param = new HashMap<>();
        param.put("token", token);
        return ServerRequestUtil.post(GET_LOGIN_BY_TOKEN, param, LoginResult.class, commonCallback);
    }

    private final static String ACTIVATE = "api/app/common/activate/[DEVICE_ID]";
    private final static String ACTIVATE_DEVICE_ID = "[DEVICE_ID]";

    public static Call activate(String device) {
        return ServerRequestUtil.get(ACTIVATE.replace(ACTIVATE_DEVICE_ID, device + ""), Object.class, null);
    }

    public static Call downFile(String url, final ProgressResponseListener commonCallback) {
        return OkHttpManage.self().get(url, commonCallback);
    }

    private final static String REPORT_ERROR = "api/app/common/reportError";

    public static Call reportError(String deviceId, String deviceMsg, String message, final CommonCallback<Object> commonCallback) {
        Map<String, Object> param = new HashMap<>();
        param.put("deviceId", deviceId);
        param.put("deviceMsg", deviceMsg);
        param.put("message", message);
        return ServerRequestUtil.post(REPORT_ERROR, param, Object.class, commonCallback);
    }
}
