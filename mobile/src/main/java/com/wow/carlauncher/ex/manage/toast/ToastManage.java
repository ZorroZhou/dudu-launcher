package com.wow.carlauncher.ex.manage.toast;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.Toast;

import com.wow.carlauncher.common.LogEx;
import com.wow.carlauncher.common.TaskExecutor;
import com.wow.carlauncher.ex.ContextEx;

/**
 * Created by 10124 on 2018/4/21.
 */

public class ToastManage extends ContextEx {
    private static class SingletonHolder {
        @SuppressLint("StaticFieldLeak")
        private static ToastManage instance = new ToastManage();
    }

    public static ToastManage self() {
        return SingletonHolder.instance;
    }

    private ToastManage() {
        super();
    }

    public void init(Context context) {
        setContext(context);
        LogEx.d(this, "init");
    }

    private static Toast toast;

    public synchronized void show(final String msg) {
        LogEx.d(this, "show:" + msg);
        TaskExecutor.self().autoPost(() -> {
            if (toast != null) {
                toast.cancel();
            }
            toast = Toast.makeText(getContext(), msg, Toast.LENGTH_LONG);
            toast.show();
        });
    }
}
