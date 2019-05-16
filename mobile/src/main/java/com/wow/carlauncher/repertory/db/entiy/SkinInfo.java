package com.wow.carlauncher.repertory.db.entiy;

import com.wow.carlauncher.repertory.db.manage.BaseEntity;
import com.wow.carlauncher.repertory.db.manage.Table;
import com.wow.carlauncher.view.activity.set.SetEnum;

@Table(name = "SkinInfo")
public class SkinInfo extends BaseEntity implements SetEnum {
    public static final int TYPE_APP_IN = 1;
    public static final int TYPE_OTHER = 2;

    private String name;
    private String mark;
    private String path;
    private Integer type;
    private Integer canUse;

    public String getMark() {
        return mark;
    }

    public SkinInfo setMark(String mark) {
        this.mark = mark;
        return this;
    }

    public String getName() {
        return name;
    }

    public SkinInfo setName(String name) {
        this.name = name;
        return this;
    }

    public String getPath() {
        return path;
    }

    public SkinInfo setPath(String path) {
        this.path = path;
        return this;
    }

    public Integer getType() {
        return type;
    }

    public SkinInfo setType(Integer type) {
        this.type = type;
        return this;
    }

    public Integer getCanUse() {
        return canUse;
    }

    public SkinInfo setCanUse(Integer canUse) {
        this.canUse = canUse;
        return this;
    }

    @Override
    public String toString() {
        return "SkinInfo{" +
                "name='" + name + '\'' +
                ", mark='" + mark + '\'' +
                ", path='" + path + '\'' +
                ", type=" + type +
                ", canUse=" + canUse +
                '}';
    }
}
