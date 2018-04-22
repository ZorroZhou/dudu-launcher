package com.wow.carlauncher.activity.launcher.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wow.carlauncher.R;
import com.wow.carlauncher.activity.AppMenuActivity;
import com.wow.carlauncher.activity.AppSelectActivity;
import com.wow.carlauncher.activity.set.SetActivity;
import com.wow.carlauncher.common.CommonData;
import com.wow.carlauncher.common.ex.ToastEx;
import com.wow.carlauncher.event.LauncherDockLabelShowChangeEvent;
import com.wow.frame.util.CommonUtil;
import com.wow.frame.util.SharedPreUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

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
import static com.wow.carlauncher.common.CommonData.SDATA_TIME_PLUGIN_OPEN_APP;

/**
 * Created by 10124 on 2018/4/22.
 */

public class LDockView extends LinearLayout implements View.OnClickListener {
    public LDockView(@NonNull Context context) {
        super(context);
        initView();
    }

    public LDockView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    @ViewInject(R.id.ll_dock1)
    private LinearLayout ll_dock1;
    @ViewInject(R.id.iv_dock1)
    private ImageView iv_dock1;
    @ViewInject(R.id.tv_dock1)
    private TextView tv_dock1;

    @ViewInject(R.id.ll_dock2)
    private LinearLayout ll_dock2;
    @ViewInject(R.id.iv_dock2)
    private ImageView iv_dock2;
    @ViewInject(R.id.tv_dock2)
    private TextView tv_dock2;

    @ViewInject(R.id.ll_dock3)
    private LinearLayout ll_dock3;
    @ViewInject(R.id.iv_dock3)
    private ImageView iv_dock3;
    @ViewInject(R.id.tv_dock3)
    private TextView tv_dock3;

    @ViewInject(R.id.ll_dock4)
    private LinearLayout ll_dock4;
    @ViewInject(R.id.iv_dock4)
    private ImageView iv_dock4;
    @ViewInject(R.id.tv_dock4)
    private TextView tv_dock4;

    @ViewInject(R.id.ll_dock5)
    private LinearLayout ll_dock5;
    @ViewInject(R.id.iv_dock5)
    private ImageView iv_dock5;
    @ViewInject(R.id.tv_dock5)
    private TextView tv_dock5;

    @ViewInject(R.id.tv_app_appss)
    private TextView tv_app_appss;

    private PackageManager pm;

    private void initView() {
        pm = getContext().getPackageManager();

        LinearLayout amapView = (LinearLayout) View.inflate(getContext(), R.layout.content_l_dock, null);
        this.addView(amapView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        x.view().inject(this);

        //这里要先添加事件，要不然ID重复，会导致读取的子view错误

        ll_dock1.setOnClickListener(this);
        ll_dock2.setOnClickListener(this);
        ll_dock3.setOnClickListener(this);
        ll_dock4.setOnClickListener(this);
        ll_dock5.setOnClickListener(this);


        findViewById(R.id.ll_all_apps).setOnClickListener(this);

        loadDock();
    }

    private void openDock(String clazz) {
        Intent appIntent = pm.getLaunchIntentForPackage(clazz);
        if (appIntent != null) {
            appIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getActivity().startActivity(appIntent);
        } else {
            ToastEx.self().show("APP丢失");
            loadDock();
        }
    }

    public void loadDock() {
        String packname1 = SharedPreUtil.getSharedPreString(SDATA_DOCK1_CLASS);
        if (CommonUtil.isNotNull(packname1)) {
            try {
                PackageInfo packageInfo = pm.getPackageInfo(packname1, 0);
                iv_dock1.setImageDrawable(packageInfo.applicationInfo.loadIcon(pm));
                tv_dock1.setText(packageInfo.applicationInfo.loadLabel(pm));
            } catch (Exception e) {
                ToastEx.self().show("dock1加载失败:" + e.getMessage());
                SharedPreUtil.saveSharedPreString(SDATA_DOCK1_CLASS, null);
            }
        }
        String packname2 = SharedPreUtil.getSharedPreString(SDATA_DOCK2_CLASS);
        if (CommonUtil.isNotNull(packname2)) {
            try {
                PackageInfo packageInfo = pm.getPackageInfo(packname2, 0);
                iv_dock2.setImageDrawable(packageInfo.applicationInfo.loadIcon(pm));
                tv_dock2.setText(packageInfo.applicationInfo.loadLabel(pm));
            } catch (Exception e) {
                ToastEx.self().show("dock2加载失败:" + e.getMessage());
                SharedPreUtil.saveSharedPreString(SDATA_DOCK2_CLASS, null);
            }
        }

        String packname3 = SharedPreUtil.getSharedPreString(SDATA_DOCK3_CLASS);
        if (CommonUtil.isNotNull(packname3)) {
            try {
                PackageInfo packageInfo = pm.getPackageInfo(packname3, 0);
                iv_dock3.setImageDrawable(packageInfo.applicationInfo.loadIcon(pm));
                tv_dock3.setText(packageInfo.applicationInfo.loadLabel(pm));
            } catch (Exception e) {
                ToastEx.self().show("dock3加载失败:" + e.getMessage());
                SharedPreUtil.saveSharedPreString(SDATA_DOCK3_CLASS, null);
            }
        }

        String packname4 = SharedPreUtil.getSharedPreString(SDATA_DOCK4_CLASS);
        if (CommonUtil.isNotNull(packname4)) {
            try {
                PackageInfo packageInfo = pm.getPackageInfo(packname4, 0);
                iv_dock4.setImageDrawable(packageInfo.applicationInfo.loadIcon(pm));
                tv_dock4.setText(packageInfo.applicationInfo.loadLabel(pm));
            } catch (Exception e) {
                ToastEx.self().show("dock4加载失败:" + e.getMessage());
                SharedPreUtil.saveSharedPreString(SDATA_DOCK4_CLASS, null);
            }
        }

        String packname5 = SharedPreUtil.getSharedPreString(SDATA_DOCK5_CLASS);
        if (CommonUtil.isNotNull(packname5)) {
            try {
                PackageInfo packageInfo = pm.getPackageInfo(packname5, 0);
                iv_dock5.setImageDrawable(packageInfo.applicationInfo.loadIcon(pm));
                tv_dock5.setText(packageInfo.applicationInfo.loadLabel(pm));
            } catch (Exception e) {
                ToastEx.self().show("dock5加载失败:" + e.getMessage());
                SharedPreUtil.saveSharedPreString(SDATA_DOCK5_CLASS, null);
            }
        }
        dockLabelShow(SharedPreUtil.getSharedPreBoolean(CommonData.SDATA_LAUNCHER_DOCK_LABEL_SHOW, true));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_time: {
                String packname = SharedPreUtil.getSharedPreString(SDATA_TIME_PLUGIN_OPEN_APP);
                if (!CommonUtil.isNull(packname)) {
                    Intent appIntent = pm.getLaunchIntentForPackage(packname);
                    if (appIntent != null) {
                        appIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        getActivity().startActivity(appIntent);
                    } else {
                        ToastEx.self().show("APP丢失");
                    }
                }
                break;
            }
            case R.id.iv_set: {
                getActivity().startActivity(new Intent(getContext(), SetActivity.class));
                break;
            }
            case R.id.ll_dock1: {
                String packname = SharedPreUtil.getSharedPreString(SDATA_DOCK1_CLASS);
                if (CommonUtil.isNull(packname)) {
                    getActivity().startActivityForResult(new Intent(getContext(), AppSelectActivity.class), REQUEST_SELECT_APP_TO_DOCK1);
                } else {
                    openDock(packname);
                }
                break;
            }
            case R.id.ll_dock2: {
                String packname = SharedPreUtil.getSharedPreString(SDATA_DOCK2_CLASS);
                if (CommonUtil.isNull(packname)) {
                    getActivity().startActivityForResult(new Intent(getContext(), AppSelectActivity.class), REQUEST_SELECT_APP_TO_DOCK2);
                } else {
                    openDock(packname);
                }
                break;
            }
            case R.id.ll_dock3: {
                String packname = SharedPreUtil.getSharedPreString(SDATA_DOCK3_CLASS);
                if (CommonUtil.isNull(packname)) {
                    getActivity().startActivityForResult(new Intent(getContext(), AppSelectActivity.class), REQUEST_SELECT_APP_TO_DOCK3);
                } else {
                    openDock(packname);
                }
                break;
            }
            case R.id.ll_dock4: {
                String packname = SharedPreUtil.getSharedPreString(SDATA_DOCK4_CLASS);
                if (CommonUtil.isNull(packname)) {
                    getActivity().startActivityForResult(new Intent(getContext(), AppSelectActivity.class), REQUEST_SELECT_APP_TO_DOCK4);
                } else {
                    openDock(packname);
                }
                break;
            }
            case R.id.ll_dock5: {
                String packname = SharedPreUtil.getSharedPreString(SDATA_DOCK5_CLASS);
                if (CommonUtil.isNull(packname)) {
                    getActivity().startActivityForResult(new Intent(getContext(), AppSelectActivity.class), REQUEST_SELECT_APP_TO_DOCK5);
                } else {
                    openDock(packname);
                }
                break;
            }
            case R.id.ll_all_apps: {
                getActivity().startActivity(new Intent(getContext(), AppMenuActivity.class));
                break;
            }
        }
    }

    private Activity getActivity() {
        return (Activity) getContext();
    }

    private void dockLabelShow(boolean show) {
        int showFlag;
        if (show) {
            showFlag = View.VISIBLE;
        } else {
            showFlag = View.GONE;
        }
        tv_app_appss.setVisibility(showFlag);
        tv_dock1.setVisibility(showFlag);
        tv_dock2.setVisibility(showFlag);
        tv_dock3.setVisibility(showFlag);
        tv_dock4.setVisibility(showFlag);
        tv_dock5.setVisibility(showFlag);
    }

    @Subscribe
    public void onEventMainThread(LauncherDockLabelShowChangeEvent event) {
        dockLabelShow(event.show);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        EventBus.getDefault().unregister(this);
    }
}
