package com.wow.carlauncher.activity.launcher.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wow.carlauncher.R;
import com.wow.carlauncher.plugin11.music.MusicPlugin;
import com.wow.carlauncher.plugin11.music.event.PMusicEventInfo;
import com.wow.carlauncher.plugin11.music.event.PMusicEventState;
import com.wow.frame.util.CommonUtil;

import org.greenrobot.eventbus.Subscribe;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import static com.wow.carlauncher.common.CommonData.TAG;

/**
 * Created by 10124 on 2018/4/20.
 */

@ContentView(R.layout.content_l_music)
public class LMusicView extends LBaseView {

    public LMusicView(@NonNull Context context) {
        super(context);
        addContent(R.layout.content_l_music);
    }

    public LMusicView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        addContent(R.layout.content_l_music);
    }

    @ViewInject(R.id.iv_play)
    private ImageView music_iv_play;

    @ViewInject(R.id.tv_title)
    private TextView music_tv_title;

    @Event(value = {R.id.iv_play, R.id.ll_prew, R.id.ll_next})
    private void clickEvent(View view) {
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

    @Subscribe
    public void onEventMainThread(final PMusicEventInfo event) {
        if (music_tv_title != null) {
            if (CommonUtil.isNotNull(event.getTitle())) {
                music_tv_title.setText(event.getTitle());
            } else {
                music_tv_title.setText("音乐");
            }
        }
    }

    @Subscribe
    public void onEventMainThread(final PMusicEventState event) {
        if (music_iv_play != null) {
            if (event.isRun()) {
                music_iv_play.setImageResource(R.drawable.ic_pause);
            } else {
                music_iv_play.setImageResource(R.drawable.ic_play);
            }
        }
    }
}
