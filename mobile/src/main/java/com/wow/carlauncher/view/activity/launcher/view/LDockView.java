package com.wow.carlauncher.view.activity.launcher.view;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wow.carlauncher.R;
import com.wow.carlauncher.common.CommonData;
import com.wow.carlauncher.common.util.CommonUtil;
import com.wow.carlauncher.common.util.SharedPreUtil;
import com.wow.carlauncher.ex.manage.ThemeManage;
import com.wow.carlauncher.ex.manage.appInfo.AppInfo;
import com.wow.carlauncher.ex.manage.appInfo.AppInfoManage;
import com.wow.carlauncher.ex.manage.appInfo.event.MAppInfoRefreshShowEvent;
import com.wow.carlauncher.ex.manage.toast.ToastManage;
import com.wow.carlauncher.view.activity.launcher.LayoutEnum;
import com.wow.carlauncher.view.activity.launcher.event.LDockLabelShowChangeEvent;
import com.wow.carlauncher.view.adapter.SelectAppAdapter;
import com.wow.carlauncher.view.base.BaseEXView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

import static com.wow.carlauncher.common.CommonData.SDATA_DOCK1_CLASS;
import static com.wow.carlauncher.common.CommonData.SDATA_DOCK2_CLASS;
import static com.wow.carlauncher.common.CommonData.SDATA_DOCK3_CLASS;
import static com.wow.carlauncher.common.CommonData.SDATA_DOCK4_CLASS;
import static com.wow.carlauncher.common.CommonData.SDATA_DOCK5_CLASS;
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

    @ViewInject(R.id.iv_dock5)
    private ImageView iv_dock5;
    @ViewInject(R.id.tv_dock5)
    private TextView tv_dock5;

    @ViewInject(R.id.ll_dock1)
    private LinearLayout ll_dock1;
    @ViewInject(R.id.ll_dock2)
    private LinearLayout ll_dock2;
    @ViewInject(R.id.ll_dock3)
    private LinearLayout ll_dock3;
    @ViewInject(R.id.ll_dock4)
    private LinearLayout ll_dock4;
    @ViewInject(R.id.ll_dock5)
    private LinearLayout ll_dock5;

    @ViewInject(R.id.ll_base)
    private LinearLayout ll_base;

    private LayoutEnum layoutEnum = LayoutEnum.LAYOUT1;

    public void setLayoutEnum(LayoutEnum layoutEnum) {
        if (layoutEnum == null) {
            return;
        }
        if (!layoutEnum.equals(this.layoutEnum)) {
            this.layoutEnum = layoutEnum;
            loadLayout();
        }
    }

    private void loadLayout() {
        Log.e(TAG, "loadLayout: " + layoutEnum);
        if (layoutEnum == null) {
            layoutEnum = LayoutEnum.LAYOUT1;
        }
        if (layoutEnum.equals(LayoutEnum.LAYOUT1)) {
            ll_base.setOrientation(LinearLayout.VERTICAL);
            ll_base.getViewTreeObserver().addOnPreDrawListener(() -> {
                if (ll_base.getHeight() > 0) {
                    int mm = (int) (ll_base.getHeight() * 0.03);
                    LinearLayout.LayoutParams itemLp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0);
                    itemLp.weight = 1;
                    itemLp.setMargins(0, mm, 0, mm);
                    ll_dock1.setLayoutParams(itemLp);
                    ll_dock2.setLayoutParams(itemLp);
                    ll_dock3.setLayoutParams(itemLp);
                    ll_dock4.setLayoutParams(itemLp);
                    ll_dock5.setLayoutParams(itemLp);
                }
                return true;
            });
            Log.e(TAG, "setLayoutEnum: " + getHeight());

        } else if (layoutEnum.equals(LayoutEnum.LAYOUT2)) {
            ll_base.setOrientation(LinearLayout.HORIZONTAL);
            ll_base.getViewTreeObserver().addOnPreDrawListener(() -> {
                if (ll_base.getHeight() > 0) {
                    int mm = (int) (ll_base.getHeight() * 0.1);
                    LinearLayout.LayoutParams itemLp = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT);
                    itemLp.weight = 1;
                    itemLp.setMargins(0, mm, 0, mm);
                    ll_dock1.setLayoutParams(itemLp);
                    ll_dock2.setLayoutParams(itemLp);
                    ll_dock3.setLayoutParams(itemLp);
                    ll_dock4.setLayoutParams(itemLp);
                    ll_dock5.setLayoutParams(itemLp);
                }
                return true;
            });

        }
    }

    @Override
    protected int getContent() {
        return R.layout.content_l_dock;
    }

    @Override
    public void changedTheme(ThemeManage manage) {
        manage.setTextViewsColor(this, new int[]{
                R.id.tv_dock1,
                R.id.tv_dock2,
                R.id.tv_dock3,
                R.id.tv_dock4,
                R.id.tv_dock5
        }, R.color.l_text1);

        loadDock(false);
        Log.e(TAG + getClass().getSimpleName(), "changedTheme: ");
    }

    @Override
    protected void initView() {
        loadLayout();
    }

    private void openDock(String clazz) {
        if (!AppInfoManage.self().openApp(clazz)) {
            ToastManage.self().show("APP打开失败,可能需要重新选择");
        }
    }

    private void loadDock(boolean removeIfError) {
        String packname1 = SharedPreUtil.getString(SDATA_DOCK1_CLASS);
        Log.e(TAG, "loadDock: " + packname1);
        if (CommonUtil.isNotNull(packname1) && AppInfoManage.self().checkApp(packname1)) {
            iv_dock1.setImageDrawable(AppInfoManage.self().getIcon(packname1));
            tv_dock1.setText(AppInfoManage.self().getName(packname1));
        } else if (removeIfError) {
            iv_dock1.setImageResource(R.mipmap.ic_add_app);
            tv_dock1.setText("添加");
            SharedPreUtil.saveString(SDATA_DOCK1_CLASS, "");
        }
        String packname2 = SharedPreUtil.getString(SDATA_DOCK2_CLASS);
        if (CommonUtil.isNotNull(packname2) && AppInfoManage.self().checkApp(packname2)) {
            iv_dock2.setImageDrawable(AppInfoManage.self().getIcon(packname2));
            tv_dock2.setText(AppInfoManage.self().getName(packname2));
        } else if (removeIfError) {
            iv_dock2.setImageResource(R.mipmap.ic_add_app);
            tv_dock2.setText("添加");
            SharedPreUtil.saveString(SDATA_DOCK2_CLASS, "");
        }

        String packname3 = SharedPreUtil.getString(SDATA_DOCK3_CLASS);
        if (CommonUtil.isNotNull(packname3) && AppInfoManage.self().checkApp(packname3)) {
            iv_dock3.setImageDrawable(AppInfoManage.self().getIcon(packname3));
            tv_dock3.setText(AppInfoManage.self().getName(packname3));
        } else if (removeIfError) {
            iv_dock3.setImageResource(R.mipmap.ic_add_app);
            tv_dock3.setText("添加");
            SharedPreUtil.saveString(SDATA_DOCK3_CLASS, "");
        }

        String packname4 = SharedPreUtil.getString(SDATA_DOCK4_CLASS);
        if (CommonUtil.isNotNull(packname4) && AppInfoManage.self().checkApp(packname4)) {
            iv_dock4.setImageDrawable(AppInfoManage.self().getIcon(packname4));
            tv_dock4.setText(AppInfoManage.self().getName(packname4));
        } else if (removeIfError) {
            iv_dock4.setImageResource(R.mipmap.ic_add_app);
            tv_dock4.setText("添加");
            SharedPreUtil.saveString(SDATA_DOCK4_CLASS, "");
        }
        String packname5 = SharedPreUtil.getString(SDATA_DOCK5_CLASS);
        if (CommonUtil.isNotNull(packname5) && AppInfoManage.self().checkApp(packname5)) {
            iv_dock5.setImageDrawable(AppInfoManage.self().getIcon(packname5));
            tv_dock5.setText(AppInfoManage.self().getName(packname5));
        } else if (removeIfError) {
            iv_dock5.setImageResource(R.mipmap.ic_add_app);
            tv_dock5.setText("添加");
            SharedPreUtil.saveString(SDATA_DOCK5_CLASS, "");
        }
        dockLabelShow(SharedPreUtil.getBoolean(CommonData.SDATA_LAUNCHER_DOCK_LABEL_SHOW, true));
        Log.e(TAG + getClass().getSimpleName(), "loadDock: ");
    }

    @Event(value = {R.id.ll_dock1, R.id.ll_dock2, R.id.ll_dock3, R.id.ll_dock4, R.id.ll_dock5})
    private void clickEvent(View v) {
        Log.d(TAG, "clickEvent: " + v);
        switch (v.getId()) {
            case R.id.ll_dock1: {
                String packname = SharedPreUtil.getString(SDATA_DOCK1_CLASS);
                if (CommonUtil.isNull(packname)) {
                    Log.e(TAG, "clickEvent: " + getActivity());
                    showSelectDialog(SDATA_DOCK1_CLASS);
                } else {
                    openDock(packname);
                }
                break;
            }
            case R.id.ll_dock2: {
                String packname = SharedPreUtil.getString(SDATA_DOCK2_CLASS);
                if (CommonUtil.isNull(packname)) {
                    showSelectDialog(SDATA_DOCK2_CLASS);
                } else {
                    openDock(packname);
                }
                break;
            }
            case R.id.ll_dock3: {
                String packname = SharedPreUtil.getString(SDATA_DOCK3_CLASS);
                if (CommonUtil.isNull(packname)) {
                    showSelectDialog(SDATA_DOCK3_CLASS);
                } else {
                    openDock(packname);
                }
                break;
            }
            case R.id.ll_dock4: {
                String packname = SharedPreUtil.getString(SDATA_DOCK4_CLASS);
                if (CommonUtil.isNull(packname)) {
                    showSelectDialog(SDATA_DOCK4_CLASS);
                } else {
                    openDock(packname);
                }
                break;
            }
            case R.id.ll_dock5: {
                String packname = SharedPreUtil.getString(SDATA_DOCK5_CLASS);
                if (CommonUtil.isNull(packname)) {
                    showSelectDialog(SDATA_DOCK5_CLASS);
                } else {
                    openDock(packname);
                }
                break;
            }
        }
    }

    @Event(value = {R.id.ll_dock1, R.id.ll_dock2, R.id.ll_dock3, R.id.ll_dock4, R.id.ll_dock5}, type = View.OnLongClickListener.class)
    private boolean longClickEvent(View view) {
        switch (view.getId()) {
            case R.id.ll_dock1: {
                showSelectDialog(SDATA_DOCK1_CLASS);
                break;
            }
            case R.id.ll_dock2: {
                showSelectDialog(SDATA_DOCK2_CLASS);
                break;
            }
            case R.id.ll_dock3: {
                showSelectDialog(SDATA_DOCK3_CLASS);
                break;
            }
            case R.id.ll_dock4: {
                showSelectDialog(SDATA_DOCK4_CLASS);
                break;
            }
            case R.id.ll_dock5: {
                showSelectDialog(SDATA_DOCK5_CLASS);
                break;
            }
        }
        return false;
    }

    private Activity getActivity() {
        return (Activity) getContext();
    }

    private void showSelectDialog(String key) {
        final List<AppInfo> appInfos = new ArrayList<>(AppInfoManage.self().getAllAppInfos());
        SelectAppAdapter adapter = new SelectAppAdapter(getContext());
        adapter.addItems(appInfos);
        new AlertDialog.Builder(getContext()).setTitle("请选择APP")
                .setAdapter(adapter, (dialog, which) -> {
                    AppInfo appInfo = appInfos.get(which);
                    SharedPreUtil.saveString(key, appInfo.appMark + CommonData.CONSTANT_APP_PACKAGE_SEPARATE + appInfo.clazz);
                    AppInfoManage.self().refreshShowApp();
                }).show();

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
        tv_dock5.setVisibility(showFlag);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(LDockLabelShowChangeEvent event) {
        Log.e(TAG, "onEvent:LDockLabelShowChangeEvent ");
        dockLabelShow(event.show);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(final MAppInfoRefreshShowEvent event) {
        Log.e(TAG, "onEvent:MAppInfoRefreshShowEvent ");
        loadDock(true);
    }


}
