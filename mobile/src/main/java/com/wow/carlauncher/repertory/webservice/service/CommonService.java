package com.wow.carlauncher.repertory.webservice.service;

import com.wow.carAssistant.packet.request.common.AppErrorRequest;
import com.wow.carAssistant.packet.request.common.GetNewAppRequest;
import com.wow.carAssistant.packet.response.common.GetAppUpdateRes;
import com.wow.carAssistant.packet.response.common.Response;
import com.wow.frame.SFrame;
import com.wow.frame.repertory.remote.BaseWebService;
import com.wow.frame.repertory.remote.ResultCheck;
import com.wow.frame.repertory.remote.WebTask;
import com.wow.frame.repertory.remote.callback.SProgressCallback;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;


/**
 * Created by liuyixian on 2017/9/20.
 */

public class CommonService extends BaseWebService {
    /**
     * App检查更新
     */
    private final static String getNewApp = "api/app/common/getNewApp1";

    public WebTask<GetAppUpdateRes> checkUpdate(boolean debug) {
        GetNewAppRequest request = new GetNewAppRequest();
        if (debug) {
            request.setType(1);
        } else {
            request.setType(2);
        }
        return request(getNewApp, request, null, GetAppUpdateRes.class);
    }

    private final static String appError = "api/app/common/appError";

    public WebTask<Response> appError(String device, String msg) {
        return request(appError, new AppErrorRequest()
                .setDeviceInfo(device)
                .setErrorInfo(msg), null, Response.class);
    }

    /**
     * 下载文件
     */
    private final static String getNewAppFile = "api/app/common/getNewAppFile1";

    public WebTask<File> getAppUpdateFile(String savePath, boolean debug) {
        GetNewAppRequest request = new GetNewAppRequest();
        if (debug) {
            request.setType(1);
        } else {
            request.setType(2);
        }

        final WebTask<File> task = new WebTask();

        File temp = new File(savePath);
        if (!temp.exists()) {
            temp.mkdirs();
        }
        RequestParams requestParams = getRequestParams(getServer() + getNewAppFile);
        requestParams.setSaveFilePath(savePath);
        requestParams.setBodyContent(SFrame.getGson().toJson(request));
        task.setXhttpTask(x.http().get(requestParams, new Callback.ProgressCallback<File>() {
            @Override
            public void onWaiting() {

            }

            @Override
            public void onStarted() {

            }

            @Override
            public void onLoading(long total, long current, boolean isDownloading) {
                if (task.getCallback() != null && task.getCallback() instanceof SProgressCallback) {
                    SProgressCallback callback = (SProgressCallback) task.getCallback();
                    if (total != 0) {
                        callback.onProgress((float) current / total);
                    }
                }
            }

            @Override
            public void onSuccess(File res) {
                if (task.getCallback() != null) {
                    task.getCallback().callback(true, "", null);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (ex instanceof ResultCheck.ResultException) {
                    ResultCheck.ResultException resultException = (ResultCheck.ResultException) ex;
                    if (task.getCallback() != null) {
                        task.getCallback().callback(false, resultException.getMsg(), null);
                    }
                } else {
                    ex.printStackTrace();
                    if (task.getCallback() != null) {
                        task.getCallback().callback(false, "服务器无法连接,请检查网络..", null);
                    }
                }
            }

            @Override
            public void onCancelled(CancelledException e) {
            }

            @Override
            public void onFinished() {
            }

        }));
        return task;
    }
}
