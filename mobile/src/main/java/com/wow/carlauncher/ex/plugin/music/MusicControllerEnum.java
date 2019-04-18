package com.wow.carlauncher.ex.plugin.music;

import com.wow.carlauncher.view.activity.set.SetEnum;

/**
 * Created by 10124 on 2018/3/28.
 */

public enum MusicControllerEnum implements SetEnum {
    UNKNOW("未知", -1),
    SYSMUSIC("系统音乐", 3),
    //NCMUSIC("网易云音乐", 4),
    //QQMUSIC("QQ音乐手机版", 5),
    QQCARMUSIC("QQ音乐车机版", 6),
    JIDOUMUSIC("极豆音乐", 7),
    NWDMUSIC("NWD音乐", 9);
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
        }
        return UNKNOW;
    }
}
