package com.wow.carlauncher.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.res.Resources;

import com.wow.carlauncher.R;
import com.wow.carlauncher.common.LogEx;
import com.wow.carlauncher.common.TaskExecutor;
import com.wow.carlauncher.common.util.CommonUtil;
import com.wow.carlauncher.ex.manage.appInfo.AppInfoManage;
import com.wow.carlauncher.ex.manage.skin.SkinManage;
import com.wow.carlauncher.repertory.db.entiy.SkinInfo;
import com.wow.carlauncher.repertory.db.manage.DatabaseManage;
import com.wow.carlauncher.view.event.EventSkinInstall;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by 10124 on 2017/11/13.
 */

public class AppInstallReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null && (intent.getAction().equals(Intent.ACTION_PACKAGE_ADDED) ||
                intent.getAction().equals(Intent.ACTION_PACKAGE_REMOVED) ||
                intent.getAction().equals(Intent.ACTION_PACKAGE_REPLACED))) {
            String dataString = intent.getDataString();
            LogEx.d(this, "AppInstallReceiver:" + dataString);
            if (CommonUtil.isNotNull(dataString) && dataString.startsWith("package:")) {
                dataString = dataString.substring(dataString.indexOf(":") + 1);
                if (dataString.startsWith("com.wow.carlauncher.theme")) {
                    LogEx.d(this, "这是一个主题!!!:" + dataString);
                    final String mark = dataString;
                    final String action = intent.getAction();
                    TaskExecutor.self().run(new Runnable() {
                        @Override
                        public void run() {
                            if (action.equals(Intent.ACTION_PACKAGE_ADDED) || action.equals(Intent.ACTION_PACKAGE_REPLACED)) {
                                try {
                                    String nameRes = context.getResources().getResourceEntryName(R.string.theme_name);
                                    PackageInfo packageInfo = context.getPackageManager().getPackageInfo(mark, 0);

                                    Resources resources = context.createPackageContext(mark, 0).getResources();
                                    int id = resources.getIdentifier(nameRes, "string", mark);
                                    String name = resources.getString(id);

                                    SkinInfo skinInfo = DatabaseManage.getBean(SkinInfo.class, " mark='" + mark + "'");

                                    if (skinInfo == null) {
                                        DatabaseManage.insert(new SkinInfo()
                                                .setMark(mark)
                                                .setVersion(packageInfo.versionCode)
                                                .setName(name));
                                    } else {
                                        DatabaseManage.update(new SkinInfo()
                                                .setMark(mark)
                                                .setVersion(packageInfo.versionCode)
                                                .setName(name), " mark='" + mark + "'");
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else if (action.equals(Intent.ACTION_PACKAGE_REMOVED)) {
                                DatabaseManage.delete(SkinInfo.class, " mark='" + mark + "'");
                                SkinManage.self().loadSkin();
                            }

                            EventBus.getDefault().post(new EventSkinInstall());
                        }
                    });
                } else {
                    AppInfoManage.self().refreshAppInfo();
                }
            }
        }
    }
}
