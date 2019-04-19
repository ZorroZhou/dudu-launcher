package com.wow.carlauncher.view.activity.set;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;

import com.wow.carlauncher.common.CommonData;
import com.wow.carlauncher.common.util.SharedPreUtil;
import com.wow.carlauncher.common.util.ThreadObj;
import com.wow.carlauncher.common.view.SetView;
import com.wow.carlauncher.ex.manage.appInfo.AppInfo;
import com.wow.carlauncher.ex.manage.appInfo.AppInfoManage;
import com.wow.carlauncher.view.activity.launcher.event.LauncherDockLabelShowChangeEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import static com.wow.carlauncher.common.CommonData.TAG;

public class SetSwitchOnClickListener implements SetView.OnValueChangeListener {
    private String key;

    public SetSwitchOnClickListener(String key) {
        this.key = key;
    }

    @Override
    public void onValueChange(String newValue, String oldValue) {
        if ("1".equals(newValue)) {
            SharedPreUtil.saveSharedPreBoolean(this.key, true);
            newValue(true);
        } else {
            SharedPreUtil.saveSharedPreBoolean(this.key, false);
            newValue(false);
        }
    }

    public void newValue(boolean value) {

    }
}
