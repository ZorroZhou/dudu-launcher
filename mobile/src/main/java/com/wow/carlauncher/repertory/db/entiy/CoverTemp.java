package com.wow.carlauncher.repertory.db.entiy;

import com.wow.carlauncher.repertory.db.manage.BaseEntity;
import com.wow.carlauncher.repertory.db.manage.Table;

@Table(name = "CoverTemp")
public class CoverTemp extends BaseEntity {
    private String key;
    private String url;

    public String getKey() {
        return key;
    }

    public CoverTemp setKey(String key) {
        this.key = key;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public CoverTemp setUrl(String url) {
        this.url = url;
        return this;
    }

    @Override
    public String toString() {
        return "CoverTemp{" +
                "key='" + key + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
