package com.wow.carlauncher.plugin;

/**
 * Created by 10124 on 2017/10/30.
 */

public enum PluginTypeEnum {
    UNKNOW("未知插件", -1), MUSIC("音乐", 1), AMAP("高德地图", 2), CONSOLE("控制中心", 3), NCMUSIC("网易云音乐", 4), QQMUSIC("QQ音乐", 5), QQCARMUSIC("QQ音乐车机版", 6);

    private String name;
    private Integer id;


    PluginTypeEnum(String name, Integer id) {
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

    public static PluginTypeEnum getById(Integer id) {
        switch (id) {
            case 1:
                return MUSIC;
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
        }
        return UNKNOW;
    }
}
