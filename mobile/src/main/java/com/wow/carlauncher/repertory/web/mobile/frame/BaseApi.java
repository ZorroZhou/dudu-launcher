package com.wow.carlauncher.repertory.web.mobile.frame;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.internal.$Gson$Types;
import com.google.gson.reflect.TypeToken;
import com.wow.carlauncher.common.util.CommonUtil;
import com.wow.carlauncher.common.util.GsonUtil;
import com.wow.carlauncher.repertory.web.mobile.frame.callback.SCallBack;
import com.wow.carlauncher.repertory.web.mobile.frame.callback.SProgressCallback;
import com.wow.carlauncher.repertory.web.mobile.packet.Response;

import org.xutils.common.Callback;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.http.body.FileBody;
import org.xutils.x;

import java.io.File;
import java.lang.reflect.Type;
import java.util.Map;

import static android.content.ContentValues.TAG;

/**
 * Created by Che on 2016/11/21 0021.
 */
public class BaseApi {
    protected Context context;

    public void setContext(Context context) {
        this.context = context;
    }

    private ResultHandle resultHandle;

    public void setDefaultResultHandle(ResultHandle resultHandle) {
        this.resultHandle = resultHandle;
    }

    private String server;

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    private JsonConverter jsonConverter;

    public void setJsonConverter(JsonConverter jsonConverter) {
        this.jsonConverter = jsonConverter;
    }

    public BaseApi() {
    }

    public RequestParams getRequestParams(String url) {
        RequestParams requestParams = new RequestParams(url);
        requestParams.setConnectTimeout(5000);
        return requestParams;
    }

    public <ResultType> WebTask<ResultType> updateFile(String url, File file) {
        RequestParams params = new RequestParams(url);
        final WebTask<ResultType> webTask = new WebTask<>();
        try {
            params.setRequestBody(new FileBody(file, "application/octet-stream"));

            webTask.setXhttpTask(x.http().request(HttpMethod.PUT, params, new Callback.ProgressCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    Log.e(TAG, "onSuccess: " + result);
                    if (webTask.getCallback() != null) {
                        webTask.getCallback().callback(true, "", null);
                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    if (ex instanceof ResultCheck.ResultException) {
                        ResultCheck.ResultException resultException = (ResultCheck.ResultException) ex;
                        if (webTask.getCallback() != null) {
                            webTask.getCallback().callback(false, resultException.getMsg(), null);
                        }
                    } else {
                        ex.printStackTrace();
                        if (webTask.getCallback() != null) {
                            webTask.getCallback().callback(false, "文件上传失败,请检查网络..", null);
                        }
                    }
                }

                @Override
                public void onCancelled(CancelledException cex) {

                }

                @Override
                public void onFinished() {
                }

                @Override
                public void onWaiting() {

                }

                @Override
                public void onStarted() {

                }

                @Override
                public void onLoading(long total, long current, boolean isDownloading) {
                    Log.e(TAG, "onLoading: " + total + ":" + current);
                    if (webTask.getCallback() != null && webTask.getCallback() instanceof SProgressCallback) {
                        SProgressCallback callback = (SProgressCallback) webTask.getCallback();
                        if (total != 0) {
                            callback.onProgress((float) current / total);
                        }
                    }
                }
            }));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return webTask;
    }

    public <ResultType> WebTask<ResultType> request(String url, Class<ResultType> clazz) {
        return request(url, null, clazz);
    }

    public <ResultType> WebTask<ResultType> request(String url, Object params, Class<ResultType> clazz) {
        final String completeUrl = server + url;
        Log.e(TAG, "request: " + completeUrl);
        RequestParams requestParams = getRequestParams(completeUrl);
        for (String key : WebApiManage.getDefaultHeadFeild().keySet()) {
            requestParams.addHeader(key, WebApiManage.getDefaultHeadFeild().get(key));
        }

        if (params != null) {
            requestParams.setBodyContent(jsonConverter.toJson(params));
        }
        final WebTask<ResultType> webTask = new WebTask<>();
        webTask.setXhttpTask(x.http().post(requestParams, new Callback.ProgressCallback<String>() {
            @Override
            public void onWaiting() {

            }

            @Override
            public void onStarted() {

            }

            @Override
            public void onLoading(long total, long current, boolean isDownloading) {
                if (webTask.getCallback() != null && webTask.getCallback() instanceof SProgressCallback) {
                    SProgressCallback callback = (SProgressCallback) webTask.getCallback();
                    if (total != 0) {
                        callback.onProgress((float) current / total);
                    }
                }
            }

            @Override
            public void onSuccess(String res) {
                Log.d(TAG, "接口返回的json" + res);
                Response<ResultType> r = GsonUtil.getGson().fromJson(res, $Gson$Types.newParameterizedTypeWithOwner(null, Response.class, clazz));
                String msg = resultHandle.handle(r);
                if (CommonUtil.isNull(msg)) {
                    if (webTask.getCallback() != null) {
                        webTask.getCallback().callback(true, null, r.getData());

                    }
                } else {
                    if (webTask.getCallback() != null) {
                        webTask.getCallback().callback(false, msg, r.getData());
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (ex instanceof ResultCheck.ResultException) {
                    ResultCheck.ResultException resultException = (ResultCheck.ResultException) ex;
                    if (webTask.getCallback() != null) {
                        webTask.getCallback().callback(false, resultException.getMsg(), null);
                    }
                } else {
                    ex.printStackTrace();
                    if (webTask.getCallback() != null) {
                        webTask.getCallback().callback(false, "服务器无法连接,请检查网络..", null);
                    }
                }
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
            }
        }));

        return webTask;
    }

    public <ResultType> WebTask<ResultType> downFile(String url, Map<String, Object> params, SCallBack<ResultType> callBack, String savePath) {
        final String completeUrl = server + url;

        RequestParams requestParams = getRequestParams(completeUrl);
        requestParams.setSaveFilePath(savePath);
        if (params != null) {
            for (String key : params.keySet()) {
                Object value = params.get(key);
                if (value instanceof String) {
                    requestParams.addParameter(key, (String) value);
                }
            }
        }
        final WebTask<ResultType> webTask = new WebTask();
        webTask.setCallback(callBack);
        webTask.setXhttpTask(x.http().get(requestParams, new Callback.ProgressCallback<File>() {
            @Override
            public void onWaiting() {

            }

            @Override
            public void onStarted() {

            }

            @Override
            public void onLoading(long total, long current, boolean isDownloading) {
                if (webTask.getCallback() != null && webTask.getCallback() instanceof SProgressCallback) {
                    SProgressCallback callback = (SProgressCallback) webTask.getCallback();
                    if (total != 0) {
                        callback.onProgress((float) current / total);
                    }
                }
            }

            @Override
            public void onSuccess(File res) {
                ResultType r;
                if (webTask.getCallback() != null) {
                    webTask.getCallback().callback(true, "", null);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (ex instanceof ResultCheck.ResultException) {
                    ResultCheck.ResultException resultException = (ResultCheck.ResultException) ex;
                    if (webTask.getCallback() != null) {
                        webTask.getCallback().callback(false, resultException.getMsg(), null);
                    }
                } else {
                    ex.printStackTrace();
                    if (webTask.getCallback() != null) {
                        webTask.getCallback().callback(false, "服务器无法连接,请检查网络..", null);
                    }
                }
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
            }
        }));
        return webTask;
    }

}
