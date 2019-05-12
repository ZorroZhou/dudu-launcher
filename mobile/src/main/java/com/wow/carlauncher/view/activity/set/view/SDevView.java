package com.wow.carlauncher.view.activity.set.view;

import android.annotation.SuppressLint;
import android.view.View;

import com.wow.carlauncher.R;
import com.wow.carlauncher.view.activity.set.SetActivity;
import com.wow.carlauncher.view.activity.set.SetBaseView;

import butterknife.OnClick;

/**
 * Created by 10124 on 2018/4/22.
 */

@SuppressLint("ViewConstructor")
public class SDevView extends SetBaseView {
    public SDevView(SetActivity activity) {
        super(activity);
    }
    @Override
    protected int getContent() {
        return R.layout.content_set_dev;
    }

    @OnClick(value = {R.id.sv_bind})
    public void clickEvent(View view) {
        switch (view.getId()) {
            case R.id.sv_bind:
                break;
        }
    }

}
