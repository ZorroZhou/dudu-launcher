package com.wow.carlauncher.common.ex;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.Toast;

import org.xutils.x;

/**
 * Created by 10124 on 2018/4/21.
 */

public class ToastEx extends ContextEx {
    private static class SingletonHolder {
        @SuppressLint("StaticFieldLeak")
        private static ToastEx instance = new ToastEx();
    }

    public static ToastEx self() {
        return SingletonHolder.instance;
    }

    private ToastEx() {
        super();
    }

    public void init(Context context) {
        setContext(context);
    }

    private static Toast toast;

    public synchronized void show(final String msg) {
        x.task().autoPost(new Runnable() {
            @Override
            public void run() {
                if (toast != null) {
                    toast.cancel();
                }
                toast = Toast.makeText(getContext(), msg, Toast.LENGTH_LONG);
                toast.show();
            }
        });
    }
}
