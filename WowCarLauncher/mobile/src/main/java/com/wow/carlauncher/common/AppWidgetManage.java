package com.wow.carlauncher.common;

import android.appwidget.AppWidgetHost;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProviderInfo;
import android.content.Context;
import android.view.View;

import com.wow.carlauncher.common.console.ConsoleManage;

import static com.wow.carlauncher.common.CommonData.APP_WIDGET_HOST_ID;

/**
 * Created by 10124 on 2017/11/11.
 */

public class AppWidgetManage {
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
    private Context context;

    public void init(Context context) {
        this.context = context;
        appWidgetHost = new AppWidgetHost(context, APP_WIDGET_HOST_ID);
        appWidgetManager = AppWidgetManager.getInstance(context);
        appWidgetHost.startListening();
    }

    public View getWidgetById(int id) {
        AppWidgetProviderInfo popupWidgetInfo = appWidgetManager.getAppWidgetInfo(id);
        return appWidgetHost.createView(context, id, popupWidgetInfo);
    }
}
