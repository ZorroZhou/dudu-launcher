package com.wow.carlauncher.view.activity.set.view;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.wow.carlauncher.R;
import com.wow.carlauncher.ex.manage.appInfo.AppInfo;
import com.wow.carlauncher.ex.manage.appInfo.AppInfoManage;
import com.wow.carlauncher.common.CommonData;
import com.wow.carlauncher.common.view.SetView;
import com.wow.carlauncher.view.popupWindow.event.PEventFSRefresh;
import com.wow.carlauncher.view.popupWindow.PopupWin;
import com.wow.frame.util.SharedPreUtil;

import org.greenrobot.eventbus.EventBus;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import static com.wow.carlauncher.common.CommonData.POPUP_SIZE;
import static com.wow.carlauncher.common.CommonData.TAG;

/**
 * Created by 10124 on 2018/4/22.
 */

public class SPopupView extends FrameLayout {

    public SPopupView(@NonNull Context context) {
        super(context);
        initView();
    }

    public SPopupView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    @ViewInject(R.id.sv_allow_popup_window)
    private SetView sv_allow_popup_window;

    @ViewInject(R.id.sv_popup_window_showapps)
    private SetView sv_popup_window_showapps;

    @ViewInject(R.id.sv_popup_window_showtype)
    private SetView sv_popup_window_showtype;

    @ViewInject(R.id.sv_popup_full_screen)
    private SetView sv_popup_full_screen;

    @ViewInject(R.id.sv_popup_window_size)
    private SetView sv_popup_window_size;

    private void initView() {
        LinearLayout view = (LinearLayout) View.inflate(getContext(), R.layout.content_set_popup, null);
        this.addView(view, FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);

        x.view().inject(this);


        sv_popup_window_size.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog dialog = new AlertDialog.Builder(getContext()).setTitle("请选择APP").setItems(POPUP_SIZE, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SharedPreUtil.saveSharedPreInteger(CommonData.SDATA_POPUP_SIZE, i);
                        PopupWin.self().setRank(i + 1);
                    }
                }).create();
                dialog.show();
            }
        });
        sv_popup_window_size.setSummary(POPUP_SIZE[SharedPreUtil.getSharedPreInteger(CommonData.SDATA_POPUP_SIZE, 1)]);

        sv_popup_full_screen.setOnValueChangeListener(new SetView.OnValueChangeListener() {
            @Override
            public void onValueChange(String newValue, String oldValue) {
                Log.e(TAG, "onValueChange: " + newValue);
                if ("1".equals(newValue)) {
                    SharedPreUtil.saveSharedPreBoolean(CommonData.SDATA_POPUP_FULL_SCREEN, true);
                } else {
                    SharedPreUtil.saveSharedPreBoolean(CommonData.SDATA_POPUP_FULL_SCREEN, false);
                }
                EventBus.getDefault().post(new PEventFSRefresh());
            }
        });
        sv_popup_full_screen.setChecked(SharedPreUtil.getSharedPreBoolean(CommonData.SDATA_POPUP_FULL_SCREEN, true));

        sv_allow_popup_window.setOnValueChangeListener(new SetView.OnValueChangeListener() {
            @Override
            public void onValueChange(String newValue, String oldValue) {
                if ("1".equals(newValue)) {
                    SharedPreUtil.saveSharedPreBoolean(CommonData.SDATA_POPUP_ALLOW_SHOW, true);
                } else {
                    SharedPreUtil.saveSharedPreBoolean(CommonData.SDATA_POPUP_ALLOW_SHOW, false);
                }
            }
        });
        sv_allow_popup_window.setChecked(SharedPreUtil.getSharedPreBoolean(CommonData.SDATA_POPUP_ALLOW_SHOW, true));

        sv_popup_window_showapps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectapp = SharedPreUtil.getSharedPreString(CommonData.SDATA_POPUP_SHOW_APPS);
                final List<AppInfo> appInfos = new ArrayList<>(AppInfoManage.self().getOtherAppInfos());
                String[] items = new String[appInfos.size()];
                final boolean[] checks = new boolean[appInfos.size()];
                for (int i = 0; i < items.length; i++) {
                    items[i] = appInfos.get(i).name + "(" + appInfos.get(i).clazz + ")";
                    checks[i] = selectapp.contains("[" + appInfos.get(i).clazz + "]");
                }

                AlertDialog dialog = new AlertDialog.Builder(getContext()).setTitle("请选择APP").setNegativeButton("取消", null).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String selectapp = "";
                        for (int i = 0; i < appInfos.size(); i++) {
                            if (checks[i]) {
                                selectapp = selectapp + "[" + appInfos.get(i).clazz + "];";
                            }
                        }
                        if (selectapp.endsWith(";")) {
                            selectapp = selectapp.substring(0, selectapp.length() - 1);
                        }
                        SharedPreUtil.saveSharedPreString(CommonData.SDATA_POPUP_SHOW_APPS, selectapp);
                    }
                }).setMultiChoiceItems(items, checks, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        Log.e(TAG, "onClick: " + appInfos.get(which).name);
                        checks[which] = isChecked;
                    }
                }).create();
                dialog.show();
            }
        });


        sv_popup_window_showtype.setOnValueChangeListener(new SetView.OnValueChangeListener() {
            @Override
            public void onValueChange(String newValue, String oldValue) {
                if ("1".equals(newValue)) {
                    SharedPreUtil.saveSharedPreBoolean(CommonData.SDATA_POPUP_SHOW_TYPE, true);
                } else {
                    SharedPreUtil.saveSharedPreBoolean(CommonData.SDATA_POPUP_SHOW_TYPE, false);
                }
            }
        });
        sv_popup_window_showtype.setChecked(SharedPreUtil.getSharedPreBoolean(CommonData.SDATA_POPUP_SHOW_TYPE, true));

    }
}
