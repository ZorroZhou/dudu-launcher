package com.wow.carlauncher.view.activity.driving.coolBlack;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wow.carlauncher.R;
import com.wow.carlauncher.ex.plugin.music.MusicPlugin;
import com.wow.carlauncher.ex.plugin.music.event.PMusicEventInfo;
import com.wow.carlauncher.ex.plugin.music.event.PMusicEventState;
import com.wow.carlauncher.ex.plugin.obd.evnet.PObdEventCarTp;
import com.wow.carlauncher.view.base.BaseEBusView;
import com.wow.frame.util.CommonUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import static com.wow.carlauncher.common.CommonData.TAG;

/**
 * Created by 10124 on 2018/5/11.
 */

public class BottomTpView extends BaseEBusView {
    public BottomTpView(@NonNull Context context) {
        super(context);
        addContent(R.layout.content_driving_cool_black_tp);
    }

    public BottomTpView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        addContent(R.layout.content_driving_cool_black_tp);
    }

    @ViewInject(R.id.tv_lf)
    private TextView tv_lf;

    @ViewInject(R.id.tv_lb)
    private TextView tv_lb;

    @ViewInject(R.id.tv_rf)
    private TextView tv_rf;

    @ViewInject(R.id.tv_rb)
    private TextView tv_rb;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(final PObdEventCarTp event) {
        if (tv_lf != null) {
            tv_lf.setText(getContext().getString(R.string.driving_cool_black_tp, event.getlFTirePressure()));
        }

        if (tv_lb != null) {
            tv_lb.setText(getContext().getString(R.string.driving_cool_black_tp, event.getlBTirePressure()));
        }

        if (tv_rf != null) {
            tv_rf.setText(getContext().getString(R.string.driving_cool_black_tp, event.getrFTirePressure()));
        }

        if (tv_rb != null) {
            tv_rb.setText(getContext().getString(R.string.driving_cool_black_tp, event.getrBTirePressure()));
        }
    }
}
