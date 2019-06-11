package com.wow.carlauncher.view.activity.set.setComponent;

import android.annotation.SuppressLint;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.WindowManager;

import com.wow.carlauncher.R;
import com.wow.carlauncher.common.CommonData;
import com.wow.carlauncher.common.LogEx;
import com.wow.carlauncher.common.TaskExecutor;
import com.wow.carlauncher.common.util.AppUtil;
import com.wow.carlauncher.common.util.DownUtil;
import com.wow.carlauncher.common.util.SharedPreUtil;
import com.wow.carlauncher.common.util.ViewUtils;
import com.wow.carlauncher.common.view.SetView;
import com.wow.carlauncher.ex.manage.appInfo.AppInfoManage;
import com.wow.carlauncher.ex.manage.appInfo.event.MAppInfoRefreshShowEvent;
import com.wow.carlauncher.ex.manage.toast.ToastManage;
import com.wow.carlauncher.repertory.server.CommonService;
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
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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

        sv_hava_dudu_music.setOnClickListener(v -> {
            getActivity().showLoading("请求中");
            int type = CommonService.UPDATE_TYPE_RELEASE;
            if (SharedPreUtil.getBoolean(CommonData.SDATA_ALLOW_DEBUG_APP, false)) {
                type = CommonService.UPDATE_TYPE_DEBUG;
            }
            CommonService.getUpdate(type, CommonService.APP_TYPE_MUSIC, (success, msg, appUpdate) -> {
                getActivity().hideLoading();
                if (success == 0) {
                    int version = 0;
                    if (AppInfoManage.self().checkApp(DUDU_MUSIC)) {
                        version = AppUtil.getLocalVersion(getContext(), DUDU_MUSIC);
                    }
                    if (version < appUpdate.getVersion()) {
                        TaskExecutor.self().autoPost(() -> new AlertDialog.Builder(getContext()).setTitle("发现新版本")
                                .setNegativeButton("忽略", null)
                                .setPositiveButton("下载新版本", (dialog12, which) -> {
                                    DownUtil.loadDownloadApk(getActivity()
                                            , "正在下载嘟嘟音乐新版本"
                                            , "/dudu-music-V" + appUpdate.getVersion() + ".apk"
                                            , appUpdate.getUrl());
                                }).setMessage(appUpdate.getAbout()).show());
                    } else {
                        ToastManage.self().show("没有新版本");
                    }
                } else {
                    ToastManage.self().show("没有新版本");
                }
            });
        });

        sv_dudu_music_auto.setOnValueChangeListener(new SetSwitchOnClickListener(CommonData.SDATA_AUTO_OPEN_DUDU_MUSIC));
        sv_dudu_music_auto.setChecked(SharedPreUtil.getBoolean(CommonData.SDATA_AUTO_OPEN_DUDU_MUSIC, false));


        sv_hava_dudu_fm.setOnClickListener(v -> {
            getActivity().showLoading("请求中");
            int type = CommonService.UPDATE_TYPE_RELEASE;
            if (SharedPreUtil.getBoolean(CommonData.SDATA_ALLOW_DEBUG_APP, false)) {
                type = CommonService.UPDATE_TYPE_DEBUG;
            }
            CommonService.getUpdate(type, CommonService.APP_TYPE_FM, (success, msg, appUpdate) -> {
                getActivity().hideLoading();
                if (success == 0) {
                    int localVersion = 0;
                    if (AppInfoManage.self().checkApp(DUDU_FM)) {
                        localVersion = AppUtil.getLocalVersion(getContext(), DUDU_FM);
                    }
                    if (localVersion < appUpdate.getVersion()) {
                        TaskExecutor.self().autoPost(() -> new AlertDialog.Builder(getContext()).setTitle("发现新版本")
                                .setNegativeButton("忽略", null)
                                .setPositiveButton("下载新版本", (dialog12, which) -> {
                                    DownUtil.loadDownloadApk(getActivity()
                                            , "正在下载嘟嘟FM新版本"
                                            , "/dudu-fm-V" + appUpdate.getVersion() + ".apk"
                                            , appUpdate.getUrl());
                                }).setMessage(appUpdate.getAbout()).show());
                    } else {
                        ToastManage.self().show("没有新版本");
                    }
                } else {
                    ToastManage.self().show("没有新版本");
                }
            });
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

        loadDuduAppInfo();
    }

    private void loadDuduAppInfo() {
        int localMusicVersion = 0;
        if (AppInfoManage.self().checkApp(DUDU_MUSIC)) {
            localMusicVersion = AppUtil.getLocalVersion(getContext(), DUDU_MUSIC);
        }

        sv_hava_dudu_music.setSummary(localMusicVersion > 0 ? "已安装:" + localMusicVersion : "未安装");

        int localFmVersion = 0;
        if (AppInfoManage.self().checkApp(DUDU_FM)) {
            localFmVersion = AppUtil.getLocalVersion(getContext(), DUDU_FM);
        }

        sv_hava_dudu_fm.setSummary(localFmVersion > 0 ? "已安装:" + localFmVersion : "未安装");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(final MAppInfoRefreshShowEvent event) {
        loadDuduAppInfo();
    }
}
