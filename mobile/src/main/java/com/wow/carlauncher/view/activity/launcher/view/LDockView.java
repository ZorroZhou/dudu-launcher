package com.wow.carlauncher.view.activity.launcher.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wow.carlauncher.R;
import com.wow.carlauncher.common.CommonData;
import com.wow.carlauncher.ex.manage.appInfo.AppInfoManage;
import com.wow.carlauncher.ex.manage.toast.ToastManage;
import com.wow.carlauncher.view.activity.AppSelectActivity;
import com.wow.carlauncher.view.activity.launcher.event.LDockLabelShowChangeEvent;
import com.wow.carlauncher.view.base.BaseEXView;
import com.wow.carlauncher.common.util.CommonUtil;
import com.wow.carlauncher.common.util.SharedPreUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import static com.wow.carlauncher.common.CommonData.REQUEST_SELECT_APP_TO_DOCK1;
import static com.wow.carlauncher.common.CommonData.REQUEST_SELECT_APP_TO_DOCK2;
import static com.wow.carlauncher.common.CommonData.REQUEST_SELECT_APP_TO_DOCK3;
import static com.wow.carlauncher.common.CommonData.REQUEST_SELECT_APP_TO_DOCK4;
import static com.wow.carlauncher.common.CommonData.SDATA_DOCK1_CLASS;
import static com.wow.carlauncher.common.CommonData.SDATA_DOCK2_CLASS;
import static com.wow.carlauncher.common.CommonData.SDATA_DOCK3_CLASS;
import static com.wow.carlauncher.common.CommonData.SDATA_DOCK4_CLASS;
import static com.wow.carlauncher.common.CommonData.TAG;

/**
 * Created by 10124 on 2018/4/22.
 */

public class LDockView extends BaseEXView {
    public LDockView(@NonNull Context context) {
        super(context);
    }

    public LDockView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }


    @ViewInject(R.id.iv_dock1)
    private ImageView iv_dock1;
    @ViewInject(R.id.tv_dock1)
    private TextView tv_dock1;

    @ViewInject(R.id.iv_dock2)
    private ImageView iv_dock2;
    @ViewInject(R.id.tv_dock2)
    private TextView tv_dock2;

    @ViewInject(R.id.iv_dock3)
    private ImageView iv_dock3;
    @ViewInject(R.id.tv_dock3)
    private TextView tv_dock3;

    @ViewInject(R.id.iv_dock4)
    private ImageView iv_dock4;
    @ViewInject(R.id.tv_dock4)
    private TextView tv_dock4;

    private PackageManager pm;

    @Override
    protected int getContent() {
        return R.layout.content_l_dock;
    }

    @Override
    protected void initView() {
        pm = getContext().getPackageManager();
        //这里要先添加事件，要不然ID重复，会导致读取的子view错误
        loadDock();
    }

    private void openDock(String clazz) {
        if (!AppInfoManage.self().openApp(clazz)) {
            loadDock();
        }
    }

    public void loadDock() {
        String packname1 = SharedPreUtil.getSharedPreString(SDATA_DOCK1_CLASS);
        if (CommonUtil.isNotNull(packname1)) {
            if (AppInfoManage.self().checkApp(packname1)) {
                iv_dock1.setImageDrawable(AppInfoManage.self().getIcon(packname1));
                tv_dock1.setText(AppInfoManage.self().getName(packname1));
            } else {
                ToastManage.self().show("dock1加载失败");
                SharedPreUtil.saveSharedPreString(SDATA_DOCK1_CLASS, null);
            }
        }
        String packname2 = SharedPreUtil.getSharedPreString(SDATA_DOCK2_CLASS);
        if (CommonUtil.isNotNull(packname2)) {
            if (AppInfoManage.self().checkApp(packname2)) {
                iv_dock2.setImageDrawable(AppInfoManage.self().getIcon(packname2));
                tv_dock2.setText(AppInfoManage.self().getName(packname2));
            } else {
                ToastManage.self().show("dock2加载失败");
                SharedPreUtil.saveSharedPreString(SDATA_DOCK2_CLASS, null);
            }
        }

        String packname3 = SharedPreUtil.getSharedPreString(SDATA_DOCK3_CLASS);
        if (CommonUtil.isNotNull(packname3)) {
            if (AppInfoManage.self().checkApp(packname3)) {
                iv_dock3.setImageDrawable(AppInfoManage.self().getIcon(packname3));
                tv_dock3.setText(AppInfoManage.self().getName(packname3));
            } else {
                ToastManage.self().show("dock3加载失败");
                SharedPreUtil.saveSharedPreString(SDATA_DOCK3_CLASS, null);
            }
        }

        String packname4 = SharedPreUtil.getSharedPreString(SDATA_DOCK4_CLASS);
        if (CommonUtil.isNotNull(packname4)) {
            if (AppInfoManage.self().checkApp(packname4)) {
                iv_dock4.setImageDrawable(AppInfoManage.self().getIcon(packname4));
                tv_dock4.setText(AppInfoManage.self().getName(packname4));
            } else {
                ToastManage.self().show("dock4加载失败");
                SharedPreUtil.saveSharedPreString(SDATA_DOCK4_CLASS, null);
            }
        }

        dockLabelShow(SharedPreUtil.getSharedPreBoolean(CommonData.SDATA_LAUNCHER_DOCK_LABEL_SHOW, true));
    }

    @Event(value = {R.id.ll_dock1, R.id.ll_dock2, R.id.ll_dock3, R.id.ll_dock4})
    private void clickEvent(View v) {
        Log.d(TAG, "clickEvent: " + v);
        switch (v.getId()) {
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
        tv_dock1.setVisibility(showFlag);
        tv_dock2.setVisibility(showFlag);
        tv_dock3.setVisibility(showFlag);
        tv_dock4.setVisibility(showFlag);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(LDockLabelShowChangeEvent event) {
        dockLabelShow(event.show);
    }
}
