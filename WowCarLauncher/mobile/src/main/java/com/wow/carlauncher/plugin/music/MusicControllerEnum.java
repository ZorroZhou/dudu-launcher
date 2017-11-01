package com.wow.carlauncher.plugin.music;

/**
 * Created by 10124 on 2017/10/30.
 */

public enum MusicControllerEnum {
    QQMUSIC("QQ音乐", 4), NETEASECLOUD("网易云音乐", 3), QQMUSICCAR("QQ音乐车载版", 2), SYSTEM("系统默认播放器(大部分情况不好用)", 1);

    private String name;
    private int id;


    MusicControllerEnum(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public static MusicControllerEnum valueOfId(int id) {
        for (MusicControllerEnum e : values()) {
            if (id == e.getId()) {
                return e;
            }
        }
        return SYSTEM;
    }

    public static MusicControllerEnum valueOfName(String name) {
        for (MusicControllerEnum e : values()) {
            if (name.equals(e.getName())) {
                return e;
            }
        }
        return SYSTEM;
    }
}
