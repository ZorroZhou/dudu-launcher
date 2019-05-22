package com.wow.carlauncher.view.activity.launcher.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wow.carlauncher.R;
import com.wow.carlauncher.common.CommonData;
import com.wow.carlauncher.common.LogEx;
import com.wow.carlauncher.common.util.CommonUtil;
import com.wow.carlauncher.common.util.SharedPreUtil;
import com.wow.carlauncher.common.util.ViewUtils;
import com.wow.carlauncher.ex.manage.appInfo.AppInfo;
import com.wow.carlauncher.ex.manage.appInfo.AppInfoManage;
import com.wow.carlauncher.ex.manage.appInfo.event.MAppInfoRefreshShowEvent;
import com.wow.carlauncher.ex.manage.toast.ToastManage;
import com.wow.carlauncher.view.activity.launcher.BaseThemeView;
import com.wow.carlauncher.view.activity.launcher.ItemInterval;
import com.wow.carlauncher.view.activity.launcher.LayoutEnum;
import com.wow.carlauncher.view.activity.launcher.event.LDockLabelShowChangeEvent;
import com.wow.carlauncher.view.adapter.SelectAppAdapter;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnLongClick;

import static com.wow.carlauncher.common.CommonData.SDATA_DOCK1_CLASS;
import static com.wow.carlauncher.common.CommonData.SDATA_DOCK2_CLASS;
import static com.wow.carlauncher.common.CommonData.SDATA_DOCK3_CLASS;
import static com.wow.carlauncher.common.CommonData.SDATA_DOCK4_CLASS;
import static com.wow.carlauncher.common.CommonData.SDATA_DOCK5_CLASS;
import static com.wow.carlauncher.common.CommonData.SDATA_LAUNCHER_ITEM_INTERVAL;
import static com.wow.carlauncher.common.CommonData.TAG;

/**
 * Created by 10124 on 2018/4/22.
 */

public class LDockView extends BaseThemeView {
    public LDockView(@NonNull Context context) {
        super(context);
    }

    public LDockView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @BindView(R.id.iv_dock1)
    ImageView iv_dock1;
    @BindView(R.id.tv_dock1)
    TextView tv_dock1;

    @BindView(R.id.iv_dock2)
    ImageView iv_dock2;
    @BindView(R.id.tv_dock2)
    TextView tv_dock2;

    @BindView(R.id.iv_dock3)
    ImageView iv_dock3;
    @BindView(R.id.tv_dock3)
    TextView tv_dock3;

    @BindView(R.id.iv_dock4)
    ImageView iv_dock4;
    @BindView(R.id.tv_dock4)
    TextView tv_dock4;

    @BindView(R.id.iv_dock5)
    ImageView iv_dock5;
    @BindView(R.id.tv_dock5)
    TextView tv_dock5;

    @BindView(R.id.ll_dock1)
    LinearLayout ll_dock1;
    @BindView(R.id.ll_dock2)
    LinearLayout ll_dock2;
    @BindView(R.id.ll_dock3)
    LinearLayout ll_dock3;
    @BindView(R.id.ll_dock4)
    LinearLayout ll_dock4;
    @BindView(R.id.ll_dock5)
    LinearLayout ll_dock5;

    @BindView(R.id.ll_base)
    LinearLayout ll_base;

    private LayoutEnum layoutEnum;

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
        if (layoutEnum.equals(LayoutEnum.LAYOUT1)) {
            ll_base.setOrientation(LinearLayout.VERTICAL);
            int paddingTop = ViewUtils.dip2px(getContext(), ItemInterval.getSizeById(SharedPreUtil.getInteger(SDATA_LAUNCHER_ITEM_INTERVAL, ItemInterval.XIAO.getId())) - 15);
            ll_base.setPadding(0, paddingTop, 0, 0);
            LinearLayout.LayoutParams itemLp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0);
            itemLp.weight = 1;
            itemLp.setMargins(0,
                    ViewUtils.dip2px(getContext(), 15),
                    0,
                    ViewUtils.dip2px(getContext(), 10));
            ll_dock1.setLayoutParams(itemLp);
            ll_dock2.setLayoutParams(itemLp);
            ll_dock3.setLayoutParams(itemLp);
            ll_dock4.setLayoutParams(itemLp);
            ll_dock5.setLayoutParams(itemLp);

            ll_dock5.setVisibility(GONE);
        } else if (layoutEnum.equals(LayoutEnum.LAYOUT2)) {
            ll_base.setOrientation(LinearLayout.HORIZONTAL);
            ll_base.setPadding(0, 0, 0, 0);

            LinearLayout.LayoutParams itemLp = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT);
            itemLp.weight = 1;
            itemLp.setMargins(0,
                    0,
                    0,
                    ViewUtils.dip2px(getContext(), 10));
            ll_dock1.setLayoutParams(itemLp);
            ll_dock2.setLayoutParams(itemLp);
            ll_dock3.setLayoutParams(itemLp);
            ll_dock4.setLayoutParams(itemLp);
            ll_dock5.setLayoutParams(itemLp);

            ll_dock5.setVisibility(VISIBLE);
        }
    }

    @Override
    protected int getContent() {
        return R.layout.content_l_dock;
    }

    @Override
    protected void initView() {
        super.initView();
        loadDock(false);
    }

    private void openDock(String clazz) {
        if (!AppInfoManage.self().openApp(clazz)) {
            ToastManage.self().show("APP打开失败,可能需要重新选择");
        }
    }

    private void loadDock(boolean remove) {
        String packname1 = SharedPreUtil.getString(SDATA_DOCK1_CLASS);
        if (CommonUtil.isNotNull(packname1) && AppInfoManage.self().checkApp(packname1)) {
            AppInfoManage.self().setIconWithSkin(iv_dock1, packname1);
            tv_dock1.setText(AppInfoManage.self().getName(packname1));
        } else {
            iv_dock1.setImageResource(R.drawable.theme_add_app);
            tv_dock1.setText("添加");
            if (remove) {
                SharedPreUtil.saveString(SDATA_DOCK1_CLASS, "");
            }
        }
        String packname2 = SharedPreUtil.getString(SDATA_DOCK2_CLASS);
        if (CommonUtil.isNotNull(packname2) && AppInfoManage.self().checkApp(packname2)) {
            AppInfoManage.self().setIconWithSkin(iv_dock2, packname2);
            tv_dock2.setText(AppInfoManage.self().getName(packname2));
        } else {
            iv_dock2.setImageResource(R.drawable.theme_add_app);
            tv_dock2.setText("添加");
            if (remove) {
                SharedPreUtil.saveString(SDATA_DOCK2_CLASS, "");
            }
        }

        String packname3 = SharedPreUtil.getString(SDATA_DOCK3_CLASS);
        if (CommonUtil.isNotNull(packname3) && AppInfoManage.self().checkApp(packname3)) {
            AppInfoManage.self().setIconWithSkin(iv_dock3, packname3);
            tv_dock3.setText(AppInfoManage.self().getName(packname3));
        } else {
            iv_dock3.setImageResource(R.drawable.theme_add_app);
            tv_dock3.setText("添加");
            if (remove) {
                SharedPreUtil.saveString(SDATA_DOCK3_CLASS, "");
            }
        }

        String packname4 = SharedPreUtil.getString(SDATA_DOCK4_CLASS);
        if (CommonUtil.isNotNull(packname4) && AppInfoManage.self().checkApp(packname4)) {
            AppInfoManage.self().setIconWithSkin(iv_dock4, packname4);
            tv_dock4.setText(AppInfoManage.self().getName(packname4));
        } else {
            iv_dock4.setImageResource(R.drawable.theme_add_app);
            tv_dock4.setText("添加");
            if (remove) {
                SharedPreUtil.saveString(SDATA_DOCK4_CLASS, "");
            }
        }
        String packname5 = SharedPreUtil.getString(SDATA_DOCK5_CLASS);
        if (CommonUtil.isNotNull(packname5) && AppInfoManage.self().checkApp(packname5)) {
            AppInfoManage.self().setIconWithSkin(iv_dock5, packname5);
            tv_dock5.setText(AppInfoManage.self().getName(packname5));
        } else {
            iv_dock5.setImageResource(R.drawable.theme_add_app);
            tv_dock5.setText("添加");
            if (remove) {
                SharedPreUtil.saveString(SDATA_DOCK5_CLASS, "");
            }
        }
        dockLabelShow(SharedPreUtil.getBoolean(CommonData.SDATA_LAUNCHER_DOCK_LABEL_SHOW, true));
    }

    @OnClick(value = {R.id.ll_dock1, R.id.ll_dock2, R.id.ll_dock3, R.id.ll_dock4, R.id.ll_dock5})
    public void clickEvent(View v) {
        Log.d(TAG, "clickEvent: " + v);
        switch (v.getId()) {
            case R.id.ll_dock1: {
                String packname = SharedPreUtil.getString(SDATA_DOCK1_CLASS);
                if (CommonUtil.isNull(packname)) {
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

    @OnLongClick(value = {R.id.ll_dock1, R.id.ll_dock2, R.id.ll_dock3, R.id.ll_dock4, R.id.ll_dock5})
    public boolean longClickEvent(View view) {
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
        LogEx.d(this, "onEvent:LDockLabelShowChangeEvent ");
        dockLabelShow(event.show);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(final MAppInfoRefreshShowEvent event) {
        LogEx.d(this, "onEvent:MAppInfoRefreshShowEvent ");
        loadDock(true);
    }
}
