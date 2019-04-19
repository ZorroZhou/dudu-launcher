package com.wow.carlauncher.view.activity.set.view;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.wow.carlauncher.R;
import com.wow.carlauncher.common.CommonData;
import com.wow.carlauncher.common.view.SetView;
import com.wow.carlauncher.ex.manage.appInfo.AppInfo;
import com.wow.carlauncher.ex.manage.appInfo.AppInfoManage;
import com.wow.carlauncher.view.activity.set.SetAppMultipleSelectOnClickListener;
import com.wow.carlauncher.view.activity.set.SetSwitchOnClickListener;
import com.wow.carlauncher.view.base.BaseView;
import com.wow.carlauncher.view.popupWindow.PopupWin;
import com.wow.carlauncher.view.popupWindow.event.PEventFSRefresh;
import com.wow.carlauncher.common.util.SharedPreUtil;

import org.greenrobot.eventbus.EventBus;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

import static com.wow.carlauncher.common.CommonData.POPUP_SIZE;
import static com.wow.carlauncher.common.CommonData.TAG;

/**
 * Created by 10124 on 2018/4/22.
 */

public class SPopupView extends BaseView {

    public SPopupView(@NonNull Context context) {
        super(context);
    }

    public SPopupView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected int getContent() {
        return R.layout.content_set_popup;
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

    @Override
    protected void initView() {
        sv_popup_window_size.setOnClickListener(view -> {
            AlertDialog dialog = new AlertDialog.Builder(getContext()).setTitle("请选择一个尺寸").setItems(POPUP_SIZE, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    SharedPreUtil.saveSharedPreInteger(CommonData.SDATA_POPUP_SIZE, i);
                    PopupWin.self().setRank(i + 1);
                }
            }).create();
            dialog.show();
        });
        sv_popup_window_size.setSummary(POPUP_SIZE[SharedPreUtil.getSharedPreInteger(CommonData.SDATA_POPUP_SIZE, 1)]);

        sv_popup_full_screen.setOnValueChangeListener(new SetSwitchOnClickListener(CommonData.SDATA_POPUP_FULL_SCREEN) {
            @Override
            public void newValue(boolean value) {
                EventBus.getDefault().post(new PEventFSRefresh());
            }
        });
        sv_popup_full_screen.setChecked(SharedPreUtil.getSharedPreBoolean(CommonData.SDATA_POPUP_FULL_SCREEN, true));

        sv_allow_popup_window.setOnValueChangeListener(new SetSwitchOnClickListener(CommonData.SDATA_POPUP_ALLOW_SHOW));
        sv_allow_popup_window.setChecked(SharedPreUtil.getSharedPreBoolean(CommonData.SDATA_POPUP_ALLOW_SHOW, true));

        sv_popup_window_showapps.setOnClickListener(new SetAppMultipleSelectOnClickListener(getContext()) {
            @Override
            public String getCurr() {
                return SharedPreUtil.getSharedPreString(CommonData.SDATA_POPUP_SHOW_APPS);
            }

            @Override
            public void onSelect(String t) {
                SharedPreUtil.saveSharedPreString(CommonData.SDATA_POPUP_SHOW_APPS, t);
            }
        });


        sv_popup_window_showtype.setOnValueChangeListener(new SetSwitchOnClickListener(CommonData.SDATA_POPUP_SHOW_TYPE));
        sv_popup_window_showtype.setChecked(SharedPreUtil.getSharedPreBoolean(CommonData.SDATA_POPUP_SHOW_TYPE, true));

    }
}
