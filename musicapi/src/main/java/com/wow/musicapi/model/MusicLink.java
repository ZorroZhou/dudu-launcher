package com.wow.musicapi.model;

import java.io.Serializable;

/**
 * Created by haohua on 2018/2/10.
 */
public interface MusicLink extends Serializable {
    String getUrl();

    String getType();

    long getSize();

    String getSongId();

    long getBitRate();

    String getMd5();
}
