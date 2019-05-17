package com.wow.carlauncher.repertory.db.entiy;

import com.wow.carlauncher.repertory.db.manage.BaseEntity;
import com.wow.carlauncher.repertory.db.manage.Table;
import com.wow.carlauncher.view.activity.set.SetEnum;

@Table(name = "SkinInfo")
public class SkinInfo extends BaseEntity implements SetEnum {
    public static final int TYPE_APP_IN = 1;
    public static final int TYPE_OTHER = 2;

    private String name;//主题名称
    private String mark;//主题的标记

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

    @Override
    public String toString() {
        return "SkinInfo{" +
                "name='" + name + '\'' +
                ", mark='" + mark + '\'' +
                '}';
    }
}
