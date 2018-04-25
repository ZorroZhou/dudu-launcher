package com.wow.carlauncher.common.ex;

import android.content.Context;

/**
 * Created by 10124 on 2018/4/21.
 */

public class ContextEx {
    private Context context;

    public Context getContext() {
        return context;
    }

    public ContextEx() {
    }

    protected void setContext(Context context) {
        this.context = context;
    }

    public ContextEx(Context context) {
        this.context = context;
    }
}
