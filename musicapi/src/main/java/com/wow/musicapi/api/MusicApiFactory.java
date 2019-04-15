package com.wow.musicapi.api;

/**
 * Created by haohua on 2018/2/11.
 */
public class MusicApiFactory {
    public static MusicApi create(MusicProvider provider) {
        try {
            MusicApi musicApi = provider.getMusicApiClass().newInstance();
            return musicApi;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
