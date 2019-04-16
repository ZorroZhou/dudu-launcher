package com.wow.carlauncher.view.activity.launcher.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.wow.carlauncher.R;
import com.wow.carlauncher.view.base.BaseEBusView;
import com.wow.carlauncher.view.consoleWindow.ConsoleWin;

import org.xutils.view.annotation.Event;

import static com.wow.carlauncher.common.CommonData.TAG;

/**
 * Created by 10124 on 2018/4/20.
 */

public class LObdView extends BaseEBusView {

    public LObdView(@NonNull Context context) {
        super(context);
        init();
    }

    public LObdView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        addContent(R.layout.content_l_obd);
    }


    @Event(value = {R.id.tv_open_console})
    private void clickEvent(View v) {
        Log.d(TAG, "clickEvent: " + v);
        switch (v.getId()) {
            case R.id.tv_open_console: {
                ConsoleWin.self().show();
                break;
            }
        }
    }
}
