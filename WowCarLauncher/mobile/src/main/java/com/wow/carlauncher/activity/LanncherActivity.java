package com.wow.carlauncher.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wow.carlauncher.R;
import com.wow.carlauncher.common.BaseActivity;
import com.wow.carlauncher.common.CommonData;
import com.wow.carlauncher.common.util.CommonUtil;
import com.wow.carlauncher.common.util.SharedPreUtil;
import com.wow.carlauncher.plugin.PluginManage;
import com.wow.carlauncher.popupWindow.PopupWindow;

import org.xutils.view.annotation.ViewInject;

import java.util.Timer;
import java.util.TimerTask;

import static com.wow.carlauncher.common.CommonData.IDATA_PACKAGE_NAME;
import static com.wow.carlauncher.common.CommonData.REQUEST_SELECT_APP_TO_DOCK1;
import static com.wow.carlauncher.common.CommonData.REQUEST_SELECT_APP_TO_DOCK2;
import static com.wow.carlauncher.common.CommonData.REQUEST_SELECT_APP_TO_DOCK3;
import static com.wow.carlauncher.common.CommonData.REQUEST_SELECT_APP_TO_DOCK4;
import static com.wow.carlauncher.common.CommonData.REQUEST_SELECT_APP_TO_DOCK5;
import static com.wow.carlauncher.common.CommonData.SDATA_DOCK1_CLASS;
import static com.wow.carlauncher.common.CommonData.SDATA_DOCK2_CLASS;
import static com.wow.carlauncher.common.CommonData.SDATA_DOCK3_CLASS;
import static com.wow.carlauncher.common.CommonData.SDATA_DOCK4_CLASS;
import static com.wow.carlauncher.common.CommonData.SDATA_DOCK5_CLASS;

/**
 * Created by 10124 on 2017/10/26.
 */
public class LanncherActivity extends BaseActivity implements View.OnClickListener, View.OnLongClickListener {

    @ViewInject(R.id.ll_main_right)
    private LinearLayout ll_main_right;

    @ViewInject(R.id.ll_main_left)
    private LinearLayout ll_main_left;

    @ViewInject(R.id.rl_dock_all)
    private RelativeLayout rl_dock_all;

    @ViewInject(R.id.rl_dock1)
    private RelativeLayout rl_dock1;
    @ViewInject(R.id.iv_dock1_icon)
    private ImageView iv_dock1_icon;
    @ViewInject(R.id.tv_dock1_name)
    private TextView tv_dock1_name;


    @ViewInject(R.id.rl_dock2)
    private RelativeLayout rl_dock2;
    @ViewInject(R.id.iv_dock2_icon)
    private ImageView iv_dock2_icon;
    @ViewInject(R.id.tv_dock2_name)
    private TextView tv_dock2_name;

    @ViewInject(R.id.rl_dock3)
    private RelativeLayout rl_dock3;
    @ViewInject(R.id.iv_dock3_icon)
    private ImageView iv_dock3_icon;
    @ViewInject(R.id.tv_dock3_name)
    private TextView tv_dock3_name;

    @ViewInject(R.id.rl_dock4)
    private RelativeLayout rl_dock4;
    @ViewInject(R.id.iv_dock4_icon)
    private ImageView iv_dock4_icon;
    @ViewInject(R.id.tv_dock4_name)
    private TextView tv_dock4_name;

    @ViewInject(R.id.rl_dock5)
    private RelativeLayout rl_dock5;
    @ViewInject(R.id.iv_dock5_icon)
    private ImageView iv_dock5_icon;
    @ViewInject(R.id.tv_dock5_name)
    private TextView tv_dock5_name;

    @ViewInject(R.id.iv_set)
    private ImageView iv_set;

    private PackageManager pm;

    @Override
    public void init() {
        setContent(R.layout.activity_lanncher);
        pm = getPackageManager();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        checkPermission();
        checkAppState();
        registerReceiver(mHomeKeyEventReceiver, new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
    }

    @Override
    public void initView() {
        hideTitle();
        ll_main_right.addView(PluginManage.music().getLauncherView(), new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        ll_main_left.addView(PluginManage.time().getLauncherView(), new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));


        rl_dock1.setLongClickable(true);
        rl_dock2.setLongClickable(true);
        rl_dock3.setLongClickable(true);
        rl_dock4.setLongClickable(true);
        rl_dock5.setLongClickable(true);

        rl_dock1.setOnClickListener(this);
        rl_dock2.setOnClickListener(this);
        rl_dock3.setOnClickListener(this);
        rl_dock4.setOnClickListener(this);
        rl_dock5.setOnClickListener(this);
        rl_dock_all.setOnClickListener(this);
        iv_set.setOnClickListener(this);

        rl_dock1.setOnLongClickListener(this);
        rl_dock2.setOnLongClickListener(this);
        rl_dock3.setOnLongClickListener(this);
        rl_dock4.setOnLongClickListener(this);
        rl_dock5.setOnLongClickListener(this);

        loadDock();

//        mAppWidgetHost = new AppWidgetHost(getApplication(), APPWIDGET_HOST_ID);
//
//        manager = AppWidgetManager.getInstance(getApplication());
//
//        mAppWidgetHost.startListening();
    }

    @Override
    public boolean onLongClick(View view) {
        switch (view.getId()) {
            case R.id.rl_dock1: {
                startActivityForResult(new Intent(this, AppSelectActivity.class), REQUEST_SELECT_APP_TO_DOCK1);
                break;
            }
            case R.id.rl_dock2: {
                startActivityForResult(new Intent(this, AppSelectActivity.class), REQUEST_SELECT_APP_TO_DOCK2);
                break;
            }
            case R.id.rl_dock3: {
                startActivityForResult(new Intent(this, AppSelectActivity.class), REQUEST_SELECT_APP_TO_DOCK3);
                break;
            }
            case R.id.rl_dock4: {
                startActivityForResult(new Intent(this, AppSelectActivity.class), REQUEST_SELECT_APP_TO_DOCK4);
                break;
            }
            case R.id.rl_dock5: {
                startActivityForResult(new Intent(this, AppSelectActivity.class), REQUEST_SELECT_APP_TO_DOCK5);
                break;
            }
        }
        return false;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_set: {
//                int widgetId = mAppWidgetHost.allocateAppWidgetId();
//                Intent pickIntent = new Intent(AppWidgetManager.ACTION_APPWIDGET_PICK);
//                pickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
//                startActivityForResult(pickIntent, REQUEST_ADD_WIDGET);

                startActivity(new Intent(this, SetActivity.class));
                break;
            }
            case R.id.rl_dock1: {
                String packname = SharedPreUtil.getSharedPreString(SDATA_DOCK1_CLASS);
                if (CommonUtil.isNull(packname)) {
                    startActivityForResult(new Intent(this, AppSelectActivity.class), REQUEST_SELECT_APP_TO_DOCK1);
                } else {
                    openDock(packname);
                }
                break;
            }
            case R.id.rl_dock2: {
                String packname = SharedPreUtil.getSharedPreString(SDATA_DOCK2_CLASS);
                if (CommonUtil.isNull(packname)) {
                    startActivityForResult(new Intent(this, AppSelectActivity.class), REQUEST_SELECT_APP_TO_DOCK2);
                } else {
                    openDock(packname);
                }
                break;
            }
            case R.id.rl_dock3: {
                String packname = SharedPreUtil.getSharedPreString(SDATA_DOCK3_CLASS);
                if (CommonUtil.isNull(packname)) {
                    startActivityForResult(new Intent(this, AppSelectActivity.class), REQUEST_SELECT_APP_TO_DOCK3);
                } else {
                    openDock(packname);
                }
                break;
            }
            case R.id.rl_dock4: {
                String packname = SharedPreUtil.getSharedPreString(SDATA_DOCK4_CLASS);
                if (CommonUtil.isNull(packname)) {
                    startActivityForResult(new Intent(this, AppSelectActivity.class), REQUEST_SELECT_APP_TO_DOCK4);
                } else {
                    openDock(packname);
                }
                break;
            }
            case R.id.rl_dock5: {
                String packname = SharedPreUtil.getSharedPreString(SDATA_DOCK5_CLASS);
                if (CommonUtil.isNull(packname)) {
                    startActivityForResult(new Intent(this, AppSelectActivity.class), REQUEST_SELECT_APP_TO_DOCK5);
                } else {
                    openDock(packname);
                }
                break;
            }
            case R.id.rl_dock_all: {
                startActivity(new Intent(this, AppMenuActivity.class));
                break;
            }
        }
    }


    private void openDock(String clazz) {
        Intent appIntent = pm.getLaunchIntentForPackage(clazz);
        if (appIntent != null) {
            appIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(appIntent);
        } else {
            showTip("APP丢失");
            loadDock();
        }
    }

    private void loadDock() {
        String packname1 = SharedPreUtil.getSharedPreString(SDATA_DOCK1_CLASS);
        if (CommonUtil.isNotNull(packname1)) {
            try {
                PackageInfo packageInfo = pm.getPackageInfo(packname1, 0);
                iv_dock1_icon.setImageDrawable(packageInfo.applicationInfo.loadIcon(pm));
                tv_dock1_name.setText(packageInfo.applicationInfo.loadLabel(pm));
            } catch (Exception e) {
                SharedPreUtil.saveSharedPreString(SDATA_DOCK1_CLASS, null);
            }
        }
        String packname2 = SharedPreUtil.getSharedPreString(SDATA_DOCK2_CLASS);
        if (CommonUtil.isNotNull(packname2)) {
            try {
                PackageInfo packageInfo = pm.getPackageInfo(packname2, 0);
                iv_dock2_icon.setImageDrawable(packageInfo.applicationInfo.loadIcon(pm));
                tv_dock2_name.setText(packageInfo.applicationInfo.loadLabel(pm));
            } catch (Exception e) {
                SharedPreUtil.saveSharedPreString(SDATA_DOCK2_CLASS, null);
            }
        }

        String packname3 = SharedPreUtil.getSharedPreString(SDATA_DOCK3_CLASS);
        if (CommonUtil.isNotNull(packname3)) {
            try {
                PackageInfo packageInfo = pm.getPackageInfo(packname3, 0);
                iv_dock3_icon.setImageDrawable(packageInfo.applicationInfo.loadIcon(pm));
                tv_dock3_name.setText(packageInfo.applicationInfo.loadLabel(pm));
            } catch (Exception e) {
                SharedPreUtil.saveSharedPreString(SDATA_DOCK3_CLASS, null);
            }
        }

        String packname4 = SharedPreUtil.getSharedPreString(SDATA_DOCK4_CLASS);
        if (CommonUtil.isNotNull(packname4)) {
            try {
                PackageInfo packageInfo = pm.getPackageInfo(packname4, 0);
                iv_dock4_icon.setImageDrawable(packageInfo.applicationInfo.loadIcon(pm));
                tv_dock4_name.setText(packageInfo.applicationInfo.loadLabel(pm));
            } catch (Exception e) {
                SharedPreUtil.saveSharedPreString(SDATA_DOCK4_CLASS, null);
            }
        }

        String packname5 = SharedPreUtil.getSharedPreString(SDATA_DOCK5_CLASS);
        if (CommonUtil.isNotNull(packname5)) {
            try {
                PackageInfo packageInfo = pm.getPackageInfo(packname5, 0);
                iv_dock5_icon.setImageDrawable(packageInfo.applicationInfo.loadIcon(pm));
                tv_dock5_name.setText(packageInfo.applicationInfo.loadLabel(pm));
            } catch (Exception e) {
                SharedPreUtil.saveSharedPreString(SDATA_DOCK5_CLASS, null);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SELECT_APP_TO_DOCK1) {
            if (resultCode == RESULT_OK) {
                String packName = data.getStringExtra(IDATA_PACKAGE_NAME);
                SharedPreUtil.saveSharedPreString(SDATA_DOCK1_CLASS, packName);
                loadDock();
            }
        }
        if (requestCode == REQUEST_SELECT_APP_TO_DOCK2) {
            if (resultCode == RESULT_OK) {
                String packName = data.getStringExtra(IDATA_PACKAGE_NAME);
                SharedPreUtil.saveSharedPreString(SDATA_DOCK2_CLASS, packName);
                loadDock();
            }
        }
        if (requestCode == REQUEST_SELECT_APP_TO_DOCK3) {
            if (resultCode == RESULT_OK) {
                String packName = data.getStringExtra(IDATA_PACKAGE_NAME);
                SharedPreUtil.saveSharedPreString(SDATA_DOCK3_CLASS, packName);
                loadDock();
            }
        }
        if (requestCode == REQUEST_SELECT_APP_TO_DOCK4) {
            if (resultCode == RESULT_OK) {
                String packName = data.getStringExtra(IDATA_PACKAGE_NAME);
                SharedPreUtil.saveSharedPreString(SDATA_DOCK4_CLASS, packName);
                loadDock();
            }
        }
        if (requestCode == REQUEST_SELECT_APP_TO_DOCK5) {
            if (resultCode == RESULT_OK) {
                String packName = data.getStringExtra(IDATA_PACKAGE_NAME);
                SharedPreUtil.saveSharedPreString(SDATA_DOCK5_CLASS, packName);
                loadDock();
            }
        }
    }

    private Timer timer;

    private void checkAppState() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                PopupWindow.self().checkShowApp(CommonUtil.getForegroundApp(mContext));
                String selectapp = SharedPreUtil.getSharedPreString(CommonData.SDATA_POPUP_SHOW_APPS);

            }
        }, 0, 1000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mHomeKeyEventReceiver);
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= 23 && !Settings.canDrawOverlays(this)) {
            new AlertDialog.Builder(mContext).setTitle("系统提示")
                    .setMessage("APP需要弹出窗口权限！取消后可在APP设置调整！")
                    .setPositiveButton("前往设置", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                            startActivity(intent);
                        }
                    }).setNegativeButton("不在提示", null).show();
        }
    }

    @Override
    public void onBackPressed() {

    }

    private BroadcastReceiver mHomeKeyEventReceiver = new BroadcastReceiver() {
        String SYSTEM_REASON = "reason";
        String SYSTEM_HOME_KEY = "homekey";
        String SYSTEM_HOME_KEY_LONG = "recentapps";

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
                String reason = intent.getStringExtra(SYSTEM_REASON);
                if (TextUtils.equals(reason, SYSTEM_HOME_KEY)) {
                    //表示按了home键,程序到了后台
                    Intent i = new Intent(Intent.ACTION_MAIN, null);
                    i.addCategory(Intent.CATEGORY_HOME);
                    context.startActivity(i);
                } else if (TextUtils.equals(reason, SYSTEM_HOME_KEY_LONG)) {
                    //表示长按home键,显示最近使用的程序列表
                }
            }
        }
    };

//
//    AppWidgetHost mAppWidgetHost;
//    AppWidgetManager manager;
//
//    private static final int APPWIDGET_HOST_ID = 0x200;
//
//    private static final int REQUEST_ADD_WIDGET = 1;
//    private static final int REQUEST_CREATE_WIDGET = 2;
//
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (resultCode == RESULT_OK) {
//
//            switch (requestCode) {
//                case REQUEST_ADD_WIDGET:
//                    addWidget(data);
//                    break;
//                case REQUEST_CREATE_WIDGET:
//                    createWidget(data);
//                    break;
//                default:
//                    break;
//            }
//        } else if (requestCode == REQUEST_CREATE_WIDGET
//                && resultCode == RESULT_CANCELED && data != null) {
//            int appWidgetId = data.getIntExtra(
//                    AppWidgetManager.EXTRA_APPWIDGET_ID, -1);
//            if (appWidgetId != -1) {
//                mAppWidgetHost.deleteAppWidgetId(appWidgetId);
//            }
//        }
//    }
//
//    View hostView;
//
//    private void createWidget(Intent data) {
//        // 获取选择的widget的id
//        int appWidgetId = data.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, -1);
//        Log.e("~!~!!!!!!!!!!!!!", "createWidget: " + appWidgetId);
//        // 获取所选的Widget的AppWidgetProviderInfo信息
//        AppWidgetProviderInfo appWidget = manager.getAppWidgetInfo(appWidgetId);
//        // 根据AppWidgetProviderInfo信息，创建HostView
//        hostView = mAppWidgetHost.createView(getApplication(), appWidgetId, appWidget);
//        hostView.setScaleY(2);
//        hostView.setScaleX(2);
//        hostView.setBackgroundResource(android.R.color.transparent);
//
//        x.task().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                ergodicView((ViewGroup) hostView);
//            }
//        }, 100);
//
//
//        ll_main_left.removeAllViews();
//        ll_main_left.addView(hostView, new LinearLayout.LayoutParams((int) (ll_main_left.getWidth() / 2), (int) (ll_main_left.getHeight() / 2)));
//        ll_main_left.requestLayout();
//    }
//
//    private void addWidget(Intent data) {
//        int appWidgetId = data.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, -1);
//        AppWidgetProviderInfo appWidget = manager.getAppWidgetInfo(appWidgetId);
//
//        Log.d("AppWidget", "configure:" + appWidget.configure);
//        if (appWidget.configure != null) {
//            // 有配置，弹出配置
//            Intent intent = new Intent(
//                    AppWidgetManager.ACTION_APPWIDGET_CONFIGURE);
//            intent.setComponent(appWidget.configure);
//            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
//
//            startActivityForResult(intent, REQUEST_CREATE_WIDGET);
//
//        } else {
//            // 没有配置，直接添加
//            onActivityResult(REQUEST_CREATE_WIDGET, RESULT_OK, data);
//        }
//
//    }
//
//    private View an2, an3,amp;
//
//    private void ergodicView(ViewGroup vg) {
//        for (int i = 0; i < vg.getChildCount(); i++) {
//            Log.e("~!~!!!!!!!!!!!!!", "" + vg.getChildAt(i));
//            View v = vg.getChildAt(i);
//            if (v.toString().indexOf("app:id/amp") > 0) {
//                amp=v;
//                amp.getBackground().setAlpha(0);
//            }
//
//            if (v instanceof ViewGroup) {
//                ergodicView((ViewGroup) v);
//            } else {
//                if (v.toString().indexOf("app:id/amt") > 0) {
//                    ((ViewGroup) v.getParent()).setVisibility(View.GONE);
//                }
//                if (v.toString().indexOf("app:id/an2") > 0) {
//                    v.setVisibility(View.GONE);
//                    an2 = v;
//                }
//                if (v.toString().indexOf("app:id/an3") > 0) {
//                    v.setVisibility(View.GONE);
//                }
//                if (v.toString().indexOf("app:id/amx") > 0) {
//                    v.setVisibility(View.GONE);
//                    an3 = v;
//                }
//                if (v.toString().indexOf("app:id/amy") > 0) {
//                    v.setVisibility(View.GONE);
//                    //an3 = v;
//                }
//                if (v.toString().indexOf("app:id/amr") > 0) {
//                    ((TextView) v).addTextChangedListener(new TextWatcher() {
//                        @Override
//                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//                        }
//
//                        @Override
//                        public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//                        }
//
//                        @Override
//                        public void afterTextChanged(Editable s) {
//                            x.task().postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
//                                    an3.setVisibility(View.GONE);
//                                    an2.setVisibility(View.GONE);
//                                }
//                            }, 1000);
//                        }
//                    });
//                }
//            }
//        }
//        //app:id/amt  换肤
//        //app:id/amz  搜索
//        //app:id/amq  1*4的封面
//        //app:id/an2 循环方式
//        //app:id/amx 收藏标记
//        //app:id/an3 下方的线
//
//
//        //amt amz amq amx amy
//        //app:id/amv 上一首的id
//        //app:id/amw 下一首
//        //app:id/amu 播放按钮
//        //app:id/amr 歌曲名称的id
//        //app:id/an0 作者的
//        //app:id/amq 封面的id
//    }
}
