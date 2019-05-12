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
import com.wow.carlauncher.common.util.CommonUtil;
import com.wow.carlauncher.ex.plugin.music.MusicPlugin;
import com.wow.carlauncher.ex.plugin.music.event.PMusicEventInfo;
import com.wow.carlauncher.ex.plugin.music.event.PMusicEventState;
import com.wow.carlauncher.view.base.BaseView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;

import static com.wow.carlauncher.common.CommonData.TAG;

/**
 * Created by 10124 on 2018/5/11.
 */

public class MusicView extends BaseView {
    public MusicView(@NonNull Context context) {
        super(context);
    }

    public MusicView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected int getContent() {
        return R.layout.content_driving_cool_black_music;
    }

    @BindView(R.id.iv_play)
    ImageView iv_play;

    @BindView(R.id.tv_title)
    TextView tv_title;

    @OnClick(value = {R.id.iv_play, R.id.ll_prew, R.id.ll_next})
    public void clickEvent(View view) {
        Log.d(TAG, "clickEvent: " + view);
        switch (view.getId()) {
            case R.id.ll_prew: {
                MusicPlugin.self().pre();
                break;
            }
            case R.id.iv_play: {
                MusicPlugin.self().playOrPause();
                break;
            }
            case R.id.ll_next: {
                MusicPlugin.self().next();
                break;
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(final PMusicEventInfo event) {
        if (tv_title != null) {
            if (CommonUtil.isNotNull(event.getTitle())) {
                String msg = event.getTitle();
                if (CommonUtil.isNotNull(event.getArtist())) {
                    msg = msg + "-" + event.getArtist();
                }
                tv_title.setText(msg);
            } else {
                tv_title.setText("音乐名称");
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(final PMusicEventState event) {
        if (iv_play != null) {
            if (event.isRun()) {
                iv_play.setImageResource(R.mipmap.ic_pause2_b);
            } else {
                iv_play.setImageResource(R.mipmap.ic_play2_b);
            }
        }
    }
}
