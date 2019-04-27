package com.wow.carlauncher.repertory.web.mobile.service;

import com.wow.carlauncher.repertory.web.mobile.frame.BaseApi;
import com.wow.carlauncher.repertory.web.mobile.frame.WebTask;
import com.wow.carlauncher.repertory.web.mobile.packet.user.LoginRes;

public class CommonApi extends BaseApi {
    /**
     * 绑定设备接口
     */
    private String loginByDevice = "/api/app/common/loginByDevice/{deviceId}";

    public WebTask<LoginRes> loginByDevice(String deviceId) {
        return request(loginByDevice.replace("{deviceId}", deviceId), LoginRes.class);
    }
}
