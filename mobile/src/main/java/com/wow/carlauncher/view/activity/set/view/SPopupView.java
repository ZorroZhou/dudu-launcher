package com.wow.carlauncher.view.activity.set.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;

import com.wow.carlauncher.R;
import com.wow.carlauncher.common.CommonData;
import com.wow.carlauncher.common.util.SharedPreUtil;
import com.wow.carlauncher.common.view.SetView;
import com.wow.carlauncher.view.activity.set.SetAppMultipleSelectOnClickListener;
import com.wow.carlauncher.view.activity.set.SetSwitchOnClickListener;
import com.wow.carlauncher.view.base.BaseView;
import com.wow.carlauncher.view.popup.PopupWin;

import org.greenrobot.eventbus.EventBus;
import org.xutils.view.annotation.ViewInject;

import static com.wow.carlauncher.common.CommonData.POPUP_SIZE;

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
            new AlertDialog.Builder(getContext()).setTitle("请选择一个尺寸").setItems(POPUP_SIZE, (dialogInterface, i) -> {
                SharedPreUtil.saveInteger(CommonData.SDATA_POPUP_SIZE, i);
                PopupWin.self().setRank(i + 1);
            }).show();
        });
        sv_popup_window_size.setSummary(POPUP_SIZE[SharedPreUtil.getInteger(CommonData.SDATA_POPUP_SIZE, 1)]);

        sv_popup_full_screen.setOnValueChangeListener(new SetSwitchOnClickListener(CommonData.SDATA_POPUP_FULL_SCREEN) {
            @Override
            public void newValue(boolean value) {
                EventBus.getDefault().post(new PopupWin.PEventFSRefresh());
            }
        });
        sv_popup_full_screen.setChecked(SharedPreUtil.getBoolean(CommonData.SDATA_POPUP_FULL_SCREEN, true));

        sv_allow_popup_window.setOnValueChangeListener(new SetSwitchOnClickListener(CommonData.SDATA_POPUP_ALLOW_SHOW));
        sv_allow_popup_window.setChecked(SharedPreUtil.getBoolean(CommonData.SDATA_POPUP_ALLOW_SHOW, true));

        sv_popup_window_showapps.setOnClickListener(new SetAppMultipleSelectOnClickListener(getContext()) {
            @Override
            public String getCurr() {
                return SharedPreUtil.getString(CommonData.SDATA_POPUP_SHOW_APPS);
            }

            @Override
            public void onSelect(String t) {
                SharedPreUtil.saveString(CommonData.SDATA_POPUP_SHOW_APPS, t);
            }
        });


        sv_popup_window_showtype.setOnValueChangeListener(new SetSwitchOnClickListener(CommonData.SDATA_POPUP_SHOW_TYPE));
        sv_popup_window_showtype.setChecked(SharedPreUtil.getBoolean(CommonData.SDATA_POPUP_SHOW_TYPE, true));

    }
}
