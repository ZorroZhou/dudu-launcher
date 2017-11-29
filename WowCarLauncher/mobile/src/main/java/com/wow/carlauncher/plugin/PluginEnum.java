package com.wow.carlauncher.plugin;

/**
 * Created by 10124 on 2017/10/30.
 */

public enum PluginEnum {
    UNKNOW("未知插件", -1),
    SYSMUSIC("系统音乐", 1),
    AMAP("高德地图", 2),
    CONSOLE("控制中心", 3),
    NCMUSIC("网易云音乐", 4),
    QQMUSIC("QQ音乐手机版", 5),
    QQCARMUSIC("QQ音乐车机版", 6),
    JIDOUMUSIC("极豆音乐", 7),
    POWERAMPMUSIC("PowerAmp", 8),
    NWDMUSIC("NWD音乐", 9),
    GPS("GPS插件", 10);
    private String name;
    private Integer id;


    PluginEnum(String name, Integer id) {
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

    public static PluginEnum getById(Integer id) {
        switch (id) {
            case 1:
                return SYSMUSIC;
            case 2:
                return AMAP;
            case 3:
                return CONSOLE;
            case 4:
                return NCMUSIC;
            case 5:
                return QQMUSIC;
            case 6:
                return QQCARMUSIC;
            case 7:
                return JIDOUMUSIC;
            case 8:
                return POWERAMPMUSIC;
            case 9:
                return NWDMUSIC;
            case 10:
                return GPS;
        }
        return UNKNOW;
    }
}
