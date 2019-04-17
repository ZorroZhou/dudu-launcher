package com.wow.carlauncher.view.activity.launcher.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wow.carlauncher.R;
import com.wow.carlauncher.common.GsdFastBlurUtil;
import com.wow.carlauncher.ex.plugin.obd.ObdPlugin;
import com.wow.carlauncher.ex.plugin.obd.evnet.PObdEventCarTp;
import com.wow.carlauncher.ex.plugin.obd.evnet.PObdEventConnect;
import com.wow.carlauncher.view.base.BaseEBusView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by 10124 on 2018/4/20.
 */

public class LTaiyaView extends BaseEBusView {

    public LTaiyaView(@NonNull Context context) {
        super(context);
        init();
    }

    public LTaiyaView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @ViewInject(R.id.tv_lt)
    private TextView tv_lt;

    @ViewInject(R.id.tv_rt)
    private TextView tv_rt;

    @ViewInject(R.id.tv_lb)
    private TextView tv_lb;

    @ViewInject(R.id.tv_rb)
    private TextView tv_rb;

    @ViewInject(R.id.rl_base)
    private View rl_base;

    private void init() {
        addContent(R.layout.content_l_taiya);
        onEventMainThread(ObdPlugin.self().getCurrentPObdEventCarTp());
    }

    @SuppressLint("SetTextI18n")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(final PObdEventConnect event) {
//        boolean show = false;
//        if (event.isConnected()) {
//            if (ObdPlugin.self().supportTp()) {
//                show = true;
//            } else {
//                tv_msg.setText("OBD设备不支持胎压");
//            }
//        } else {
//            tv_msg.setText("没有连接OBD");
//        }
//        ll_ty.setVisibility(show ? VISIBLE : GONE);
//        ll_msg.setVisibility(show ? GONE : VISIBLE);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(final PObdEventCarTp event) {
        if (tv_lt != null && event.getlFTirePressure() != null) {
            tv_lt.setText(getContext().getString(R.string.launcher_tp, "左前", event.getlFTirePressure(), event.getlFTemp()));
        }

        if (tv_lb != null && event.getlBTirePressure() != null) {
            tv_lb.setText(getContext().getString(R.string.launcher_tp, "左后", event.getlBTirePressure(), event.getlBTemp()));
        }

        if (tv_rt != null && event.getrFTirePressure() != null) {
            tv_rt.setText(getContext().getString(R.string.launcher_tp, "右前", event.getrFTirePressure(), event.getrFTemp()));
        }

        if (tv_rb != null && event.getrBTirePressure() != null) {
            tv_rb.setText(getContext().getString(R.string.launcher_tp, "右后", event.getrBTirePressure(), event.getrBTemp()));
        }
    }
}
