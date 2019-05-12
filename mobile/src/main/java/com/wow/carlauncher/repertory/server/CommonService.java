package com.wow.carlauncher.repertory.server;

import com.wow.carlauncher.repertory.server.response.AppUpdate;

public class CommonService {

    private final static String GET_UPDATE = "api/app/common/getUpdate/[TYPE]";
    private final static String GET_UPDATE_TYPE = "[TYPE]";

    public static void getUpdate(int type, final CommonCallback<AppUpdate> commonCallback) {
        ServerRequestUtil.get(ServerConstant.SERVER_URL + GET_UPDATE.replace(GET_UPDATE_TYPE, type + ""), commonCallback);
    }
}
