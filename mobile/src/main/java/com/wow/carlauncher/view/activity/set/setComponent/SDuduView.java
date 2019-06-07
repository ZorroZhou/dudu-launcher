package com.wow.carlauncher.view.activity.set.setComponent;

import android.annotation.SuppressLint;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.WindowManager;

import com.wow.carlauncher.R;
import com.wow.carlauncher.common.CommonData;
import com.wow.carlauncher.common.TaskExecutor;
import com.wow.carlauncher.common.util.SharedPreUtil;
import com.wow.carlauncher.common.util.ViewUtils;
import com.wow.carlauncher.common.view.SetView;
import com.wow.carlauncher.ex.manage.appInfo.AppInfoManage;
import com.wow.carlauncher.view.activity.launcher.ItemModel;
import com.wow.carlauncher.view.activity.launcher.ItemTransformer;
import com.wow.carlauncher.view.activity.launcher.LayoutEnum;
import com.wow.carlauncher.view.activity.launcher.event.LItemRefreshEvent;
import com.wow.carlauncher.view.activity.launcher.event.LLayoutRefreshEvent;
import com.wow.carlauncher.view.activity.launcher.event.LPageTransformerChangeEvent;
import com.wow.carlauncher.view.activity.set.LauncherItemAdapter;
import com.wow.carlauncher.view.activity.set.SetActivity;
import com.wow.carlauncher.view.activity.set.SetBaseView;
import com.wow.carlauncher.view.activity.set.commonView.SetNumSelectView;
import com.wow.carlauncher.view.activity.set.commonView.SetSingleSelectView;
import com.wow.carlauncher.view.activity.set.commonView.SetSwitchOnClickListener;
import com.wow.carlauncher.view.activity.set.event.SEventPromptShowRefresh;
import com.wow.carlauncher.view.activity.set.event.SEventRequestLauncherRecreate;

import org.greenrobot.eventbus.EventBus;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import butterknife.BindView;

import static com.wow.carlauncher.common.CommonData.SDATA_LAUNCHER_ITEM_INTERVAL;
import static com.wow.carlauncher.common.CommonData.SDATA_LAUNCHER_ITEM_TRAN;
import static com.wow.carlauncher.common.CommonData.SDATA_LAUNCHER_LAYOUT;

/**
 * Created by 10124 on 2018/4/22.
 */
@SuppressLint("ViewConstructor")
public class SDuduView extends SetBaseView {

    public SDuduView(SetActivity activity) {
        super(activity);
    }

    @Override
    protected int getContent() {
        return R.layout.content_set_dudu;
    }

    @BindView(R.id.sv_hava_dudu_music)
    SetView sv_hava_dudu_music;

    @BindView(R.id.sv_dudu_music_auto)
    SetView sv_dudu_music_auto;

    @BindView(R.id.sv_hava_dudu_fm)
    SetView sv_hava_dudu_fm;

    @BindView(R.id.sv_dudu_fm_auto)
    SetView sv_dudu_fm_auto;

    @BindView(R.id.sv_hava_dudu_voice)
    SetView sv_hava_dudu_voice;

    @BindView(R.id.sv_dudu_voice_auto)
    SetView sv_dudu_voice_auto;

    @Override
    public String getName() {
        return "嘟嘟全家桶";
    }

    private static final String DUDU_MUSIC = "com.wow.dudu.music";
    private static final String DUDU_FM = "com.wow.dudu.fm";
    private static final String DUDU_VOICE = "com.wow.dudu.voice";

    protected void initView() {
        sv_hava_dudu_music.setSummary(AppInfoManage.self().checkApp(DUDU_MUSIC) ? "已安装" : "未安装");
        sv_hava_dudu_music.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        sv_dudu_music_auto.setOnValueChangeListener(new SetSwitchOnClickListener(CommonData.SDATA_AUTO_OPEN_DUDU_MUSIC));
        sv_dudu_music_auto.setChecked(SharedPreUtil.getBoolean(CommonData.SDATA_AUTO_OPEN_DUDU_MUSIC, false));

        sv_hava_dudu_fm.setSummary(AppInfoManage.self().checkApp(DUDU_FM) ? "已安装" : "未安装");
        sv_hava_dudu_fm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        sv_dudu_fm_auto.setOnValueChangeListener(new SetSwitchOnClickListener(CommonData.SDATA_AUTO_OPEN_DUDU_FM));
        sv_dudu_fm_auto.setChecked(SharedPreUtil.getBoolean(CommonData.SDATA_AUTO_OPEN_DUDU_FM, false));

        sv_hava_dudu_voice.setSummary(AppInfoManage.self().checkApp(DUDU_VOICE) ? "已安装" : "未安装");
        sv_hava_dudu_voice.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        sv_dudu_voice_auto.setOnValueChangeListener(new SetSwitchOnClickListener(CommonData.SDATA_AUTO_OPEN_DUDU_VOICE));
        sv_dudu_voice_auto.setChecked(SharedPreUtil.getBoolean(CommonData.SDATA_AUTO_OPEN_DUDU_VOICE, false));

    }
}
