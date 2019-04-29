package com.wow.carlauncher.ex.manage.toast;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.wow.carlauncher.ex.ContextEx;

import org.xutils.x;

import static com.wow.carlauncher.common.CommonData.TAG;

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
        Log.e(TAG + getClass().getSimpleName(), "init ");
    }

    private static Toast toast;

    public synchronized void show(final String msg) {
        x.task().autoPost(() -> {
            if (toast != null) {
                toast.cancel();
            }
            toast = Toast.makeText(getContext(), msg, Toast.LENGTH_LONG);
            toast.show();
        });
    }
}
