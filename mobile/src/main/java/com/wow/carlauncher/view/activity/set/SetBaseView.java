package com.wow.carlauncher.view.activity.set;

import com.wow.carlauncher.view.base.BaseView;

public abstract class SetBaseView extends BaseView {

    public SetBaseView(SetActivity activity) {
        super(activity);
    }

    public SetActivity getActivity() {
        return (SetActivity) getContext();
    }

    public abstract String getName();

    public boolean showRight() {
        return false;
    }

    public String rightTitle() {
        return "";
    }

    public boolean rightAction() {
        return false;
    }
}
