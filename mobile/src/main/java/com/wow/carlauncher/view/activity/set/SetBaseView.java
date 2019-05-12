package com.wow.carlauncher.view.activity.set;

import com.wow.carlauncher.view.base.BaseView;

public abstract class SetBaseView extends BaseView {
    private SetActivity activity;

    public SetBaseView(SetActivity activity) {
        super(activity);
        this.activity = activity;
    }

    public SetActivity getActivity() {
        return activity;
    }
}
