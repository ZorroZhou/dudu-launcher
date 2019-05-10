package com.wow.carlauncher.ex.plugin.music;

import com.wow.carlauncher.view.activity.set.SetEnum;

/**
 * Created by 10124 on 2018/3/28.
 */

public enum MusicControllerEnum implements SetEnum {
    SYSMUSIC("系统音乐(兼容性未知,需自己尝试)", 3),
    QQCARMUSIC("QQ音乐车机版(封面,歌名,歌手,进度,歌词)", 6),
    JIDOUMUSIC("极豆音乐(封面,歌名,歌手)", 7),
    NWDMUSIC("NWD音乐(封面,歌名,歌手)", 9),
    ZXMUSIC("掌讯音乐(封面,歌名,进度)", 10),
    KUWOMUSIC("酷我音乐(封面,歌名,歌手,进度,歌词,语音控制(待添加))", 11);
    private String name;
    private Integer id;


    MusicControllerEnum(String name, Integer id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public static MusicControllerEnum getById(Integer id) {
        switch (id) {
            case 3:
                return SYSMUSIC;
            case 6:
                return QQCARMUSIC;
            case 7:
                return JIDOUMUSIC;
            case 9:
                return NWDMUSIC;
            case 10:
                return ZXMUSIC;
            case 11:
                return KUWOMUSIC;
        }
        return SYSMUSIC;
    }
}
