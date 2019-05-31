package com.wow.carlauncher.repertory.db.entiy;

import com.wow.carlauncher.repertory.db.manage.BaseEntity;
import com.wow.carlauncher.repertory.db.manage.Table;
import com.wow.carlauncher.view.activity.set.setItem.SetEnum;

@Table(name = "SkinInfo")
public class SkinInfo extends BaseEntity implements SetEnum {

    private String name;//主题名称
    private String mark;//主题的标记
    private Integer version;

    public Integer getVersion() {
        return version;
    }

    public SkinInfo setVersion(Integer version) {
        this.version = version;
        return this;
    }

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
