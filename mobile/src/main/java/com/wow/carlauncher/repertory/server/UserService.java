package com.wow.carlauncher.repertory.server;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

public class UserService {

    private final static String SEND_MAIL_CODE = "api/app/user/sendMailCode";

    public static Call sendMailCode(String email, final CommonCallback<Object> commonCallback) {
        Map<String, Object> param = new HashMap<>();
        param.put("email", email);
        return ServerRequestUtil.post(ServerConstant.SERVER_URL + SEND_MAIL_CODE, param, Object.class, commonCallback);
    }

    private final static String BIND_MAIL = "api/app/user/bindEmail";

    public static Call bindMail(String code, String pass, final CommonCallback<String> commonCallback) {
        Map<String, Object> param = new HashMap<>();
        param.put("code", code);
        param.put("pass", pass);
        return ServerRequestUtil.post(ServerConstant.SERVER_URL + BIND_MAIL, param, String.class, commonCallback);
    }
}
