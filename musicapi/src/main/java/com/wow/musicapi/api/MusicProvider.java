package com.wow.musicapi.api;

import com.wow.musicapi.provider.baidu.BaiduMusicApi;
import com.wow.musicapi.provider.kugou.KugouMusicApi;
//import com.wow.musicapi.provider.kuwo.KuwoMusicApi;
//import com.wow.musicapi.provider.migu.MiguMusicApi;
//import com.wow.musicapi.provider.netease.NeteaseMusicApi;
//import com.wow.musicapi.provider.qq.QQMusicApi;
//import com.wow.musicapi.provider.weibo.WeiboMusicApi;

import com.wow.musicapi.provider.xiami.XiamiMusicApi;
//import com.wow.musicapi.provider.yiting.YitingMusicApi;

/**
 * Created by haohua on 2018/2/11.
 */
@SuppressWarnings("SpellCheckingInspection")
public enum MusicProvider {
    //    Netease("网易云音乐", NeteaseMusicApi.class),
//    QQ("QQ音乐", QQMusicApi.class),
    Kugou("酷狗音乐", KugouMusicApi.class),
    Xiami("虾米音乐", XiamiMusicApi.class),
    Baidu("百度音乐", BaiduMusicApi.class);
//    Migu("咪咕音乐", MiguMusicApi.class),
//    Yiting("一听音乐", YitingMusicApi.class),
//    Kuwo("酷我音乐", KuwoMusicApi.class),
//    Weibo("微博音乐", WeiboMusicApi.class),;

    private final Class<? extends MusicApi> musicApiClass;
    private final String providerName;

    MusicProvider(String providerName, Class<? extends MusicApi> musicApiClass) {
        this.providerName = providerName;
        this.musicApiClass = musicApiClass;
    }

    public Class<? extends MusicApi> getMusicApiClass() {
        return musicApiClass;
    }

    @Override
    public String toString() {
        return providerName;
    }
}
