package com.wow.carlauncher.view.activity.set.view;

import android.annotation.SuppressLint;
import android.support.v7.app.AlertDialog;

import com.wow.carlauncher.R;
import com.wow.carlauncher.common.CommonData;
import com.wow.carlauncher.common.util.SharedPreUtil;
import com.wow.carlauncher.common.view.SetView;
import com.wow.carlauncher.view.activity.set.SetActivity;
import com.wow.carlauncher.view.activity.set.SetBaseView;
import com.wow.carlauncher.view.activity.set.listener.SetAppMultipleSelectOnClickListener;
import com.wow.carlauncher.view.activity.set.listener.SetSwitchOnClickListener;
import com.wow.carlauncher.view.popup.PopupWin;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;

import static com.wow.carlauncher.common.CommonData.POPUP_SIZE;

/**
 * Created by 10124 on 2018/4/22.
 */

@SuppressLint("ViewConstructor")
public class SPopupView extends SetBaseView {

    public SPopupView(SetActivity activity) {
        super(activity);
    }

    @Override
    protected int getContent() {
        return R.layout.content_set_popup;
    }

    @Override
    public String getName() {
        return "悬浮窗设置";
    }

    @BindView(R.id.sv_allow_popup_window)
    SetView sv_allow_popup_window;

    @BindView(R.id.sv_popup_window_showapps)
    SetView sv_popup_window_showapps;

    @BindView(R.id.sv_popup_window_showtype)
    SetView sv_popup_window_showtype;

    @BindView(R.id.sv_popup_full_screen)
    SetView sv_popup_full_screen;

    @BindView(R.id.sv_popup_window_size)
    SetView sv_popup_window_size;

    @Override
    protected void initView() {
        sv_popup_window_size.setOnClickListener(view -> {
            new AlertDialog.Builder(getContext()).setTitle("请选择一个尺寸").setItems(POPUP_SIZE, (dialogInterface, i) -> {
                SharedPreUtil.saveInteger(CommonData.SDATA_POPUP_SIZE, i);
                PopupWin.self().setRank(i + 1);

                sv_popup_window_size.setSummary(POPUP_SIZE[i]);
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
