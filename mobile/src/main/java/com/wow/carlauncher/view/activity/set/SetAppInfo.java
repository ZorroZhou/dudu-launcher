package com.wow.carlauncher.view.activity.set;

import com.wow.carlauncher.ex.manage.appInfo.AppInfo;

public class SetAppInfo implements SetEnum {
    private AppInfo appInfo;

    public AppInfo getAppInfo() {
        return appInfo;
    }

    public SetAppInfo(AppInfo appInfo) {
        this.appInfo = appInfo;
    }

    @Override
    public String getName() {
        return appInfo.name + "(" + appInfo.clazz + ")";
    }
}
