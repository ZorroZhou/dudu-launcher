package com.wow.frame.repertory.remote;

import android.content.Context;
import android.util.Log;

import com.wow.frame.repertory.remote.callback.SCallBack;
import com.wow.frame.repertory.remote.callback.SProgressCallback;

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
public class BaseWebService {
    protected Context context;

    public void setContext(Context context) {
        this.context = context;
    }

    private ResultHandle resultHandle;

    public void setDefaultResultHandle(ResultHandle resultHandle) {
        this.resultHandle = resultHandle;
    }

    private ResultConvertor resultConvertor;

    public void setDefaultResultConvertor(ResultConvertor resultConvertor) {
        this.resultConvertor = resultConvertor;
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

    public BaseWebService() {
    }

    public RequestParams getRequestParams(String url) {
        RequestParams requestParams = new RequestParams(url);
        requestParams.setConnectTimeout(5000);
        return requestParams;
    }


    public <ResultType> WebTask<ResultType> request(final String url, Object params, SCallBack<ResultType> callBack, final Type type) {
        return request(url, params, callBack, WebServiceManage.defaultSingleTask, type);
    }

    public <ResultType> WebTask<ResultType> requestUnSingle(final String url, Object params, SCallBack<ResultType> callBack, final Type type) {
        return request(url, params, callBack, false, type);
    }

    public <ResultType> WebTask<ResultType> updateFile(String url, File file, SCallBack<ResultType> callBack) {
        RequestParams params = new RequestParams(url);
        final WebTask<ResultType> webTask = new WebTask();
        try {
            params.setRequestBody(new FileBody(file, "application/octet-stream"));
            webTask.setCallback(callBack);

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

    public <ResultType> WebTask<ResultType> request(String url, Object params, SCallBack<ResultType> callBack, final boolean singleTask, final Type type) {
        final String completeUrl = server + url;
        Log.e(TAG, "request: " + completeUrl);
        RequestParams requestParams = getRequestParams(completeUrl);
        for (String key : WebServiceManage.getDefaultHeadFeild().keySet()) {
            requestParams.addHeader(key, WebServiceManage.getDefaultHeadFeild().get(key));
        }

        if (params != null) {
            requestParams.setBodyContent(jsonConverter.toJson(params));
        }
        final WebTask<ResultType> webTask = new WebTask();
        if (singleTask) {
            WebServiceManage.removeTask(completeUrl);
            WebServiceManage.addTask(completeUrl, webTask);
        }

        webTask.setCallback(callBack);
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
                ResultType r;
                Log.d(TAG, "接口返回的json" + res);
                r = resultConvertor.convertor(res, type);
                String msg = resultHandle.handle(r);
                if (webTask.getCallback() != null) {
                    webTask.getCallback().callback(true, msg, r);
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
                if (singleTask) {
                    WebServiceManage.removeTask(webTask);
                }
            }
        }));

        return webTask;
    }

    public <ResultType> WebTask<ResultType> downFile(String url, Map<String, Object> params, SCallBack<ResultType> callBack, final boolean singleTask, String savePath) {
        final String completeUrl = server + url;

        if (singleTask) {
            WebTask<?> oldTask = WebServiceManage.getTask(completeUrl);
            if (oldTask != null) {
                oldTask.cancelTask();
            }
        }

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
                if (singleTask) {
                    WebServiceManage.removeTask(completeUrl);
                }
            }
        }));
        if (singleTask) {
            WebServiceManage.addTask(completeUrl, webTask);
        }

        return webTask;
    }

}
