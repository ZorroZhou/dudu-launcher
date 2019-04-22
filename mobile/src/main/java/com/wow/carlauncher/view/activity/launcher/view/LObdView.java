package com.wow.carlauncher.view.activity.launcher.view;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.wow.carlauncher.R;
import com.wow.carlauncher.common.util.ViewUtils;
import com.wow.carlauncher.ex.manage.ThemeManage;
import com.wow.carlauncher.ex.plugin.obd.ObdPlugin;
import com.wow.carlauncher.ex.plugin.obd.evnet.PObdEventCarInfo;
import com.wow.carlauncher.ex.plugin.obd.evnet.PObdEventConnect;
import com.wow.carlauncher.view.base.BaseEXView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static com.wow.carlauncher.ex.manage.ThemeManage.Theme.BLACK;
import static com.wow.carlauncher.ex.manage.ThemeManage.Theme.WHITE;
import static com.wow.carlauncher.view.activity.launcher.view.LShadowView.SizeEnum.FIVE;

/**
 * Created by 10124 on 2018/4/20.
 */

public class LObdView extends BaseEXView {

    public LObdView(@NonNull Context context) {
        super(context);
    }

    public LObdView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected int getContent() {
        return R.layout.content_l_obd;
    }

    @Override
    public void changedTheme(ThemeManage manage) {
        Context context = getContext();
        fl_base.setBackgroundResource(manage.getCurrentThemeRes(context, R.drawable.n_l_item1_bg));
        tv_title.setTextColor(manage.getCurrentThemeColor(context, R.color.l_text1));

        manage.setViewsBackround(this, new int[]{R.id.ll_cell1, R.id.ll_cell2, R.id.ll_cell3, R.id.ll_cell4}, R.drawable.n_cell_bg);


        manage.setTextViewsColor(this, new int[]{
                R.id.tv_text21,
                R.id.tv_text22,
                R.id.tv_text23,
                R.id.tv_text24,
                R.id.tv_sd,
                R.id.tv_zs,
                R.id.tv_sw,
                R.id.tv_yl
        }, R.color.l_text2);

        p_sd.setProgressDrawable(getResources().getDrawable(manage.getCurrentThemeRes(context, R.drawable.n_obd_progress)));
        p_zs.setProgressDrawable(getResources().getDrawable(manage.getCurrentThemeRes(context, R.drawable.n_obd_progress)));
        p_sw.setProgressDrawable(getResources().getDrawable(manage.getCurrentThemeRes(context, R.drawable.n_obd_progress)));
        p_yl.setProgressDrawable(getResources().getDrawable(manage.getCurrentThemeRes(context, R.drawable.n_obd_progress)));

        //时间组件的处理
        fl_cell1_root.removeAllViews();
        if (ll_cell1.getParent() != null) {
            ((ViewGroup) ll_cell1.getParent()).removeView(ll_cell1);
        }

        fl_cell2_root.removeAllViews();
        if (ll_cell2.getParent() != null) {
            ((ViewGroup) ll_cell2.getParent()).removeView(ll_cell2);
        }

        fl_cell3_root.removeAllViews();
        if (ll_cell3.getParent() != null) {
            ((ViewGroup) ll_cell3.getParent()).removeView(ll_cell3);
        }

        fl_cell4_root.removeAllViews();
        if (ll_cell4.getParent() != null) {
            ((ViewGroup) ll_cell4.getParent()).removeView(ll_cell4);
        }


        if (currentTheme == WHITE || currentTheme == BLACK) {
            tv_title.setGravity(Gravity.CENTER);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT);

            fl_cell1_root.addView(LShadowView.getShadowView(getContext(), ll_cell1, FIVE), params);
            fl_cell2_root.addView(LShadowView.getShadowView(getContext(), ll_cell2, FIVE), params);
            fl_cell3_root.addView(LShadowView.getShadowView(getContext(), ll_cell3, FIVE), params);
            fl_cell4_root.addView(LShadowView.getShadowView(getContext(), ll_cell4, FIVE), params);
        } else {
            tv_title.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);

            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT);
            int margin = ViewUtils.dip2px(getContext(), 5);
            params.setMargins(margin, margin, margin, margin);

            fl_cell1_root.addView(ll_cell1, params);
            fl_cell2_root.addView(ll_cell2, params);
            fl_cell3_root.addView(ll_cell3, params);
            fl_cell4_root.addView(ll_cell4, params);
        }
    }


    @Event(value = {R.id.fl_base})
    private void clickEvent(View view) {
        switch (view.getId()) {
            case R.id.rl_base: {
                if (ObdPlugin.self().notConnect()) {
                    new AlertDialog.Builder(getContext()).setTitle("警告!")
                            .setNegativeButton("取消", null)
                            .setPositiveButton("确定", (dialog2, which2) -> {
                                BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                                mBluetoothAdapter.disable();
                                x.task().postDelayed(() -> mBluetoothAdapter.enable(), 300);
                            })
                            .setMessage("是否确认重启蓝牙").show();
                }
                break;
            }
        }
    }

    @ViewInject(R.id.fl_cell1_root)
    private FrameLayout fl_cell1_root;

    @ViewInject(R.id.ll_cell1)
    private LinearLayout ll_cell1;

    @ViewInject(R.id.fl_cell2_root)
    private FrameLayout fl_cell2_root;

    @ViewInject(R.id.ll_cell2)
    private LinearLayout ll_cell2;

    @ViewInject(R.id.fl_cell3_root)
    private FrameLayout fl_cell3_root;

    @ViewInject(R.id.ll_cell3)
    private LinearLayout ll_cell3;

    @ViewInject(R.id.fl_cell4_root)
    private FrameLayout fl_cell4_root;

    @ViewInject(R.id.ll_cell4)
    private LinearLayout ll_cell4;

    @ViewInject(R.id.fl_base)
    private View fl_base;

    @ViewInject(R.id.tv_title)
    private TextView tv_title;

    @ViewInject(R.id.tv_sd)
    private TextView tv_sd;

    @ViewInject(R.id.tv_zs)
    private TextView tv_zs;

    @ViewInject(R.id.tv_sw)
    private TextView tv_sw;

    @ViewInject(R.id.tv_yl)
    private TextView tv_yl;

    @ViewInject(R.id.p_sd)
    private ProgressBar p_sd;

    @ViewInject(R.id.p_zs)
    private ProgressBar p_zs;

    @ViewInject(R.id.p_sw)
    private ProgressBar p_sw;

    @ViewInject(R.id.p_yl)
    private ProgressBar p_yl;

    @ViewInject(R.id.tv_msg)
    private TextView tv_msg;


    @ViewInject(R.id.ll_msg)
    private View ll_msg;

    @ViewInject(R.id.ll_obd)
    private View ll_obd;


    @SuppressLint("SetTextI18n")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(final PObdEventConnect event) {
        boolean show = false;
        if (event.isConnected()) {
            show = true;
        } else {
            tv_msg.setText("OBD没有连接");
        }
        ll_obd.setVisibility(show ? VISIBLE : GONE);
        ll_msg.setVisibility(show ? GONE : VISIBLE);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventCall(final PObdEventCarInfo event) {
        if (event.getSpeed() != null) {
            String msg = event.getSpeed() + "KM/H";
            tv_sd.setText(msg);
            p_sd.setProgress(event.getSpeed());
        }
        if (event.getRev() != null) {
            String msg = event.getRev() + "R/S";
            tv_zs.setText(msg);
            p_zs.setProgress(event.getRev());
        }
        if (event.getWaterTemp() != null) {
            String msg = event.getWaterTemp() + "℃";
            tv_sw.setText(msg);
            p_sw.setProgress(event.getWaterTemp());
        }
        if (event.getOilConsumption() != null) {
            String msg = event.getOilConsumption() + "%";
            tv_yl.setText(msg);
            p_yl.setProgress(event.getOilConsumption());
        }
    }
}
