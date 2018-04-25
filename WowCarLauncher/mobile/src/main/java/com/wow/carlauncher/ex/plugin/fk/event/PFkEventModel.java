package com.wow.carlauncher.ex.plugin.fk.event;

/**
 * Created by 10124 on 2018/4/22.
 */
public class PFkEventModel {
    private String modelName;

    public PFkEventModel(String modelName) {
        this.modelName = modelName;
    }

    public String getModelName() {
        return modelName;
    }

    public PFkEventModel setModelName(String modelName) {
        this.modelName = modelName;
        return this;
    }

    public PFkEventModel() {
    }
}
