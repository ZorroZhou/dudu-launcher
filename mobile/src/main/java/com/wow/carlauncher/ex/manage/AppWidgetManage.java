package com.wow.carlauncher.ex.manage;

import android.annotation.SuppressLint;
import android.appwidget.AppWidgetHost;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProviderInfo;
import android.content.Context;
import android.view.View;

import com.wow.carlauncher.common.LogEx;
import com.wow.carlauncher.ex.ContextEx;

import static com.wow.carlauncher.common.CommonData.APP_WIDGET_HOST_ID;

public class AppWidgetManage extends ContextEx {
    @SuppressLint("StaticFieldLeak")
    private static AppWidgetManage self;

    public static AppWidgetManage self() {
        if (self == null) {
            self = new AppWidgetManage();
        }
        return self;
    }

    private AppWidgetManage() {
    }

    private AppWidgetHost appWidgetHost;
    private AppWidgetManager appWidgetManager;

    public void init(Context context) {
        long t1 = System.currentTimeMillis();
        setContext(context);
        appWidgetHost = new AppWidgetHost(context, APP_WIDGET_HOST_ID);
        appWidgetManager = AppWidgetManager.getInstance(context);
        appWidgetHost.startListening();
        LogEx.d(this, "init time:" + (System.currentTimeMillis() - t1));
    }

    public View getWidgetById(int id) {
        LogEx.d(this, "getWidgetById:" + id);
        AppWidgetProviderInfo popupWidgetInfo = appWidgetManager.getAppWidgetInfo(id);
        return appWidgetHost.createView(getContext(), id, popupWidgetInfo);
    }
}
