package com.wow.carlauncher.repertory.server;

import com.wow.carlauncher.ex.manage.okHttp.OkHttpManage;
import com.wow.carlauncher.ex.manage.okHttp.ProgressResponseListener;
import com.wow.carlauncher.repertory.server.response.GetUpdateResult;

import okhttp3.Call;

public class CommonService {

    private final static String GET_UPDATE = "api/app/common/getUpdate/[TYPE]";
    private final static String GET_UPDATE_TYPE = "[TYPE]";

    public static Call getUpdate(int type, final CommonCallback<GetUpdateResult.AppUpdate> commonCallback) {
        return ServerRequestUtil.get(ServerConstant.SERVER_URL + GET_UPDATE.replace(GET_UPDATE_TYPE, type + ""), GetUpdateResult.class, commonCallback);
    }

    public static Call downFile(String url, final ProgressResponseListener commonCallback) {
        return OkHttpManage.self().get(url, commonCallback);
    }
}
