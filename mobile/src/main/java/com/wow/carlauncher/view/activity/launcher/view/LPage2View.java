package com.wow.carlauncher.view.activity.launcher.view;

import android.content.Context;
import android.support.annotation.NonNull;

import com.wow.carlauncher.R;
import com.wow.carlauncher.view.base.BaseView;

public class LPage2View extends BaseView {
    public LPage2View(@NonNull Context context) {
        super(context);
        initView();
    }

    private void initView() {
        addContent(R.layout.content_l_page2);
    }
}
