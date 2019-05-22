package com.wow.carlauncher.view.activity.launcher.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wow.carlauncher.R;
import com.wow.carlauncher.ex.manage.ImageManage;
import com.wow.carlauncher.ex.plugin.xmlyfm.PXmlyfmEventRadioInfo;
import com.wow.carlauncher.ex.plugin.xmlyfm.XmlyfmPlugin;
import com.wow.carlauncher.view.activity.launcher.BaseThemeView;
import com.wow.carlauncher.view.activity.radios.RadiosActivity;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by 10124 on 2018/4/20.
 */
@SuppressLint("SetTextI18n")
public class LXmlyFmView extends BaseThemeView {

    public LXmlyFmView(@NonNull Context context) {
        super(context);
    }

    public LXmlyFmView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected int getContent() {
        return R.layout.content_l_xmly_fm;
    }


    @BindView(R.id.tv_name)
    TextView tv_name;

    @BindView(R.id.tv_about)
    TextView tv_about;

    @BindView(R.id.iv_cover)
    ImageView iv_cover;

    @BindView(R.id.iv_play)
    ImageView iv_play;


    @OnClick(value = {R.id.rl_base, R.id.ll_prew, R.id.ll_next, R.id.ll_play})
    public void clickEvent(View view) {
        try {
            switch (view.getId()) {
                case R.id.rl_base: {
                    getContext().startActivity(new Intent(getContext(), RadiosActivity.class));
                    break;
                }
                case R.id.ll_play: {
                    XmlyfmPlugin.self().playOrStop();
                    break;
                }
                case R.id.ll_next: {
                    XmlyfmPlugin.self().next();
                    break;
                }
                case R.id.ll_prew: {
                    XmlyfmPlugin.self().prev();
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean run;

    private void refreshPlay() {
        if (run) {
            iv_play.setImageResource(R.drawable.theme_ic_pause);
        } else {
            iv_play.setImageResource(R.drawable.theme_ic_play);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(final PXmlyfmEventRadioInfo event) {
        run = event.isRun();
        refreshPlay();
        if (run) {
            tv_name.setText(event.getTitle());
            tv_about.setText(event.getProgramName());
            ImageManage.self().loadImage(event.getCover(), iv_cover, R.drawable.theme_music_dcover);
        } else {
            tv_name.setText("FM广播");
            tv_about.setText("欢迎收听");
        }
    }
}
