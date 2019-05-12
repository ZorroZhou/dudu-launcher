package com.wow.carlauncher.view.activity.set.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.wow.carlauncher.R;
import com.wow.carlauncher.view.base.BaseEXView;

import butterknife.OnClick;

/**
 * Created by 10124 on 2018/4/22.
 */

public class SDrivingView extends BaseEXView {

    public SDrivingView(@NonNull Context context) {
        super(context);
    }

    public SDrivingView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
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
