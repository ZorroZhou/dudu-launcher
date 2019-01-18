package com.wow.carlauncher.repertory.webservice;

import com.wow.carAssistant.packet.response.common.Response;
import com.wow.carlauncher.repertory.webservice.service.CommonService;
import com.wow.frame.SFrame;
import com.wow.frame.repertory.remote.ResultCheck;
import com.wow.frame.repertory.remote.ResultConvertor;
import com.wow.frame.repertory.remote.ResultHandle;
import com.wow.frame.repertory.remote.WebServiceInfo;

import java.lang.reflect.Type;

/**
 * Created by 10124 on 2017/7/27.
 */
public class AppWsInfo extends WebServiceInfo {
    @Override
    public Class<?>[] getInterFaceClass() {
        return new Class<?>[]{
                CommonService.class
        };
    }

    private static final String SERVEL_URL = "http://soap613.oicp.net:8086/";//

    @Override
    public String getServerUrl() {
        return SERVEL_URL;
    }

    @Override
    public ResultConvertor getDefaultResultConvertor() {
        return new ResultConvertor() {

            @Override
            public <T> T convertor(String res, Type typeOfT) {
                return SFrame.getGson().fromJson(res, typeOfT);
            }
        };
    }

    @Override
    public ResultHandle getDefaultResultHandle() {
        return new ResultHandle() {
            @Override
            public <T> String handle(T t) {
                if (t == null) {
                    return "服务器无法连接,请检查网络..";
                }
                if (t instanceof Response) {
                    ResultCheck.checkResult(((Response) t).getCode() != null && ((Response) t).getCode() == 0, ((Response) t).getMsg());
                }
                return "操作成功！";
            }
        };
    }
}
