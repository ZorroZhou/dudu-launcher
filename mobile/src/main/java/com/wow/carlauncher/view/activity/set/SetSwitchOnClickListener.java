package com.wow.carlauncher.view.activity.set;

import com.wow.carlauncher.common.util.SharedPreUtil;
import com.wow.carlauncher.common.view.SetView;

public class SetSwitchOnClickListener implements SetView.OnValueChangeListener {
    private String key;

    public SetSwitchOnClickListener(String key) {
        this.key = key;
    }

    @Override
    public void onValueChange(String newValue, String oldValue) {
        if ("1".equals(newValue)) {
            SharedPreUtil.saveBoolean(this.key, true);
            newValue(true);
        } else {
            SharedPreUtil.saveBoolean(this.key, false);
            newValue(false);
        }
    }

    public void newValue(boolean value) {

    }
}
