package com.wow.carlauncher.ex.plugin.xmlyfm;

import android.annotation.SuppressLint;
import android.content.Context;

import com.wow.carlauncher.common.gsonType.GsonBaseResultType;
import com.wow.carlauncher.common.LogEx;
import com.wow.carlauncher.common.gsonType.GsonListType;
import com.wow.carlauncher.common.util.GsonUtil;
import com.wow.carlauncher.common.util.SharedPreUtil;
import com.wow.carlauncher.ex.ContextEx;
import com.wow.carlauncher.ex.manage.toast.ToastManage;
import com.wow.carlauncher.ex.plugin.music.MusicPlugin;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.model.PlayableModel;
import com.ximalaya.ting.android.opensdk.model.live.radio.Radio;
import com.ximalaya.ting.android.opensdk.player.XmPlayerManager;
import com.ximalaya.ting.android.opensdk.player.service.IXmPlayerStatusListener;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayerException;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import static com.wow.carlauncher.common.CommonData.SDATA_MY_FAV_RADIOS;

/**
 * Created by 10124 on 2017/11/9.
 */

public class XmlyfmPlugin extends ContextEx {
    private static class SingletonHolder {
        @SuppressLint("StaticFieldLeak")
        private static XmlyfmPlugin instance = new XmlyfmPlugin();
    }

    public static XmlyfmPlugin self() {
        return XmlyfmPlugin.SingletonHolder.instance;
    }

    private XmlyfmPlugin() {
    }

    private XmPlayerManager xmPlayerManager;

    private List<Radio> radios;
    private Radio nowRadio;

    public List<Radio> getRadios() {
        return radios;
    }

    public void addRadio(Radio r) {
        if (r == null) {
            return;
        }
        if (radios.size() >= 10) {
            ToastManage.self().show("最多添加10个节目!");
            return;
        }
        boolean have = false;
        for (Radio radio : radios) {
            if (radio.getDataId() == r.getDataId()) {
                have = true;
                break;
            }
        }
        if (!have) {
            radios.add(r);
            try {
                SharedPreUtil.saveString(SDATA_MY_FAV_RADIOS, GsonUtil.getGson().toJson(radios));
            } catch (Exception ignored) {
            }
        }
    }

    public void removeRadio(Radio r) {
        if (r == null) {
            return;
        }
        Radio temp = null;
        for (Radio radio : radios) {
            if (radio.getDataId() == r.getDataId()) {
                temp = radio;
                break;
            }
        }
        if (temp != null) {
            radios.remove(temp);
            try {
                SharedPreUtil.saveString(SDATA_MY_FAV_RADIOS, GsonUtil.getGson().toJson(radios));
            } catch (Exception ignored) {
            }
        }
    }

    public void play(Radio radio) {
        nowRadio = radio;
        xmPlayerManager.playActivityRadio(radio);
        System.out.println(GsonUtil.getGson().toJson(nowRadio));
        EventBus.getDefault().post(new PXmlyfmEventRadioInfo().setProgramName(nowRadio.getProgramName()).setCover(nowRadio.getCoverUrlLarge()).setRun(true).setTitle(nowRadio.getRadioName()));
    }


    public void playOrStop() {
        if (run) {
            xmPlayerManager.stop();
        } else {
            MusicPlugin.self().pause();
            if (radios.size() < 1) {
                ToastManage.self().show("请先添加");
                return;
            }
            nowRadio = radios.get(0);
            play(nowRadio);
        }
    }

    public void stop() {
        xmPlayerManager.stop();
    }

    public void next() {
        if (nowRadio == null) {
            playOrStop();
            return;
        }
        int index = 0;
        for (int i = 0; i < radios.size(); i++) {
            Radio temp = radios.get(i);
            if (temp.getDataId() == nowRadio.getDataId()) {
                index = i;
                break;
            }
        }
        index++;
        if (index == radios.size()) {
            index = 0;
        }
        play(radios.get(index));
    }

    public void prev() {
        if (nowRadio == null) {
            playOrStop();
            return;
        }
        int index = radios.size() - 1;
        for (int i = 0; i < radios.size(); i++) {
            Radio temp = radios.get(i);
            if (temp.getDataId() == nowRadio.getDataId()) {
                index = i;
                break;
            }
        }
        index--;
        if (index < 0) {
            index = radios.size() - 1;
        }
        play(radios.get(index));
    }

    public void init(Context context) {
        long t1 = System.currentTimeMillis();
        setContext(context);
        radios = new ArrayList<>();
        try {
            radios.addAll(GsonUtil.getGson().fromJson(SharedPreUtil.getString(SDATA_MY_FAV_RADIOS), new GsonListType(Radio.class)));
        } catch (Exception ignored) {
        }


        CommonRequest mXimalaya = CommonRequest.getInstanse();
        mXimalaya.setAppkey("c8a2b804a8d5b105ed8ccb33c7417c4a");
        mXimalaya.setPackid("com.wow.carlauncher");
        mXimalaya.init(context, "3278974c9911b8c6e79800423be56bec");

        xmPlayerManager = XmPlayerManager.getInstance(context);
        xmPlayerManager.addPlayerStatusListener(mPlayerStatusListener);
        xmPlayerManager.play();

        LogEx.d(this, "init time:" + (System.currentTimeMillis() - t1));
    }

    private boolean run = false;
    private IXmPlayerStatusListener mPlayerStatusListener = new IXmPlayerStatusListener() {

        @Override
        public void onSoundSwitch(PlayableModel laModel, PlayableModel curModel) {

        }

        @Override
        public void onSoundPrepared() {
        }

        @Override
        public void onSoundPlayComplete() {
        }

        @Override
        public void onPlayStop() {
            run = false;
            EventBus.getDefault().post(new PXmlyfmEventRadioInfo().setRun(false));
        }

        @Override
        public void onPlayStart() {
            run = true;
        }

        @Override
        public void onPlayProgress(int currPos, int duration) {
        }

        @Override
        public void onPlayPause() {
            run = false;
        }

        @Override
        public boolean onError(XmPlayerException exception) {
            exception.printStackTrace();
            return false;

        }

        @Override
        public void onBufferingStop() {
        }

        @Override
        public void onBufferingStart() {
        }

        @Override
        public void onBufferProgress(int percent) {
        }

    };
}
