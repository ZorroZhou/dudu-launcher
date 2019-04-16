package com.wow.carlauncher.view.activity.launcher.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.wow.carlauncher.R;
import com.wow.carlauncher.view.base.BaseEBusView;

/**
 * Created by 10124 on 2018/4/20.
 */

public class LObdView extends BaseEBusView {

    public LObdView(@NonNull Context context) {
        super(context);
        addContent(R.layout.content_l_obd);
    }

    public LObdView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        addContent(R.layout.content_l_obd);
    }


}
