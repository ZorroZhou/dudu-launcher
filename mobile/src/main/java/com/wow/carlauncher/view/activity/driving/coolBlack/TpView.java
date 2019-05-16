package com.wow.carlauncher.view.activity.driving.coolBlack;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wow.carlauncher.R;
import com.wow.carlauncher.ex.manage.ImageManage;
import com.wow.carlauncher.ex.plugin.music.event.PMusicEventCoverRefresh;
import com.wow.carlauncher.ex.plugin.obd.ObdPlugin;
import com.wow.carlauncher.ex.plugin.obd.evnet.PObdEventCarTp;
import com.wow.carlauncher.ex.plugin.obd.evnet.PObdEventConnect;
import com.wow.carlauncher.view.base.BaseView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;

/**
 * Created by 10124 on 2018/5/11.
 */

public class TpView extends BaseView {
    public TpView(@NonNull Context context) {
        super(context);
    }

    public TpView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected int getContent() {
        return R.layout.content_driving_cool_black_tp;
    }

    @Override
    protected void initView() {
        onEvent(new PObdEventConnect().setConnected(ObdPlugin.self().isConnect()));
        onEvent(ObdPlugin.self().getCurrentPObdEventCarTp());
    }

    @BindView(R.id.tv_lf)
    TextView tv_lf;

    @BindView(R.id.tv_lb)
    TextView tv_lb;

    @BindView(R.id.tv_rf)
    TextView tv_rf;

    @BindView(R.id.tv_rb)
    TextView tv_rb;

    @BindView(R.id.music_iv_cover)
    ImageView music_iv_cover;

    @BindView(R.id.ll_cover)
    View ll_cover;

    @BindView(R.id.ll_tp)
    View ll_tp;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(final PObdEventCarTp event) {
        if (tv_lf != null && event.getlFTirePressure() != null) {
            tv_lf.setText(getContext().getString(R.string.driving_cool_black_tp, event.getlFTirePressure(), event.getlFTemp()));
        }

        if (tv_lb != null && event.getlBTirePressure() != null) {
            tv_lb.setText(getContext().getString(R.string.driving_cool_black_tp, event.getlBTirePressure(), event.getlBTemp()));
        }

        if (tv_rf != null && event.getrFTirePressure() != null) {
            tv_rf.setText(getContext().getString(R.string.driving_cool_black_tp, event.getrFTirePressure(), event.getrFTemp()));
        }

        if (tv_rb != null && event.getrBTirePressure() != null) {
            tv_rb.setText(getContext().getString(R.string.driving_cool_black_tp, event.getrBTirePressure(), event.getrBTemp()));
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(final PObdEventConnect event) {
        boolean show = false;
        if (event.isConnected()) {
            if (ObdPlugin.self().supportTp()) {
                show = true;
            }
        }
        ll_tp.setVisibility(show ? VISIBLE : GONE);
        ll_cover.setVisibility(show ? GONE : VISIBLE);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(final PMusicEventCoverRefresh event) {
        if (event.isHave()) {
            ImageManage.self().loadImage(event.getUrl(), music_iv_cover, R. drawable.theme_music_dcover);
        } else {
            music_iv_cover.setImageResource(R. drawable.theme_music_dcover);
        }
    }

}
