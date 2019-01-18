package com.wow.frame.repertory.remote;

import com.wow.frame.repertory.remote.callback.SCallBack;

import org.xutils.common.Callback;

/**
 * Created by 10124 on 2017/7/27.
 */
public class WebTask<T> {
    private SCallBack<T> callback;

    public SCallBack<T> getCallback() {
        return callback;
    }

    public WebTask setCallback(SCallBack<T> callback) {
        this.callback = callback;
        return this;
    }

    private Callback.Cancelable xhttpTask;

    public WebTask setXhttpTask(Callback.Cancelable xhttpTask) {
        this.xhttpTask = xhttpTask;
        return this;
    }

    public void cancelTask() {
        if (xhttpTask != null&&!xhttpTask.isCancelled()) {
            xhttpTask.cancel();
            callback = null;
        }
    }
}
