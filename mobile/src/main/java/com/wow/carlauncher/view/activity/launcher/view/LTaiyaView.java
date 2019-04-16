package com.wow.carlauncher.view.activity.launcher.view;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wow.carlauncher.R;
import com.wow.carlauncher.common.LukuangView;
import com.wow.carlauncher.ex.plugin.amapcar.AMapCarPlugin;
import com.wow.carlauncher.ex.plugin.amapcar.event.PAmapEventNavInfo;
import com.wow.carlauncher.ex.plugin.amapcar.event.PAmapEventState;
import com.wow.carlauncher.ex.plugin.amapcar.event.PAmapLukuangInfo;
import com.wow.carlauncher.ex.plugin.amapcar.event.PAmapMuteStateInfo;
import com.wow.carlauncher.ex.plugin.amapcar.model.Lukuang;
import com.wow.carlauncher.ex.plugin.obd.ObdPlugin;
import com.wow.carlauncher.ex.plugin.obd.evnet.PObdEventCarTp;
import com.wow.carlauncher.ex.plugin.obd.evnet.PObdEventConnect;
import com.wow.carlauncher.view.base.BaseEBusView;
import com.wow.frame.util.AppUtil;
import com.wow.frame.util.CommonUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.wow.carlauncher.common.CommonData.TAG;
import static com.wow.carlauncher.ex.plugin.amapcar.AMapCarConstant.AMAP_PACKAGE;
import static com.wow.carlauncher.ex.plugin.amapcar.AMapCarConstant.ICONS;

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


    private void init() {
        addContent(R.layout.content_l_taiya);
        onEventMainThread(ObdPlugin.self().getCurrentPObdEventCarTp());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(final PObdEventConnect event) {
        if (event.isConnected() && ObdPlugin.self().supportTp()) {

        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(final PObdEventCarTp event) {
        if (tv_lt != null && event.getlFTirePressure() != null) {
            tv_lt.setText(getContext().getString(R.string.launcher_tp, event.getlFTirePressure(), event.getlFTemp()));
        }

        if (tv_lb != null && event.getlBTirePressure() != null) {
            tv_lb.setText(getContext().getString(R.string.launcher_tp, event.getlBTirePressure(), event.getlBTemp()));
        }

        if (tv_rt != null && event.getrFTirePressure() != null) {
            tv_rt.setText(getContext().getString(R.string.launcher_tp, event.getrFTirePressure(), event.getrFTemp()));
        }

        if (tv_rb != null && event.getrBTirePressure() != null) {
            tv_rb.setText(getContext().getString(R.string.launcher_tp, event.getrBTirePressure(), event.getrBTemp()));
        }
    }
}
