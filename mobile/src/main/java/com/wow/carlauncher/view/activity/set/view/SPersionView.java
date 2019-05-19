package com.wow.carlauncher.view.activity.set.view;

import android.annotation.SuppressLint;

import com.wow.carlauncher.R;
import com.wow.carlauncher.view.activity.set.SetActivity;
import com.wow.carlauncher.view.activity.set.SetBaseView;

/**
 * Created by 10124 on 2018/4/22.
 */
@SuppressLint("ViewConstructor")
public class SPersionView extends SetBaseView {
    public SPersionView(SetActivity activity) {
        super(activity);
    }

    @Override
    protected int getContent() {
        return R.layout.content_set_persion;
    }

    @Override
    public String getName() {
        return "个人中心";
    }

    protected void initView() {

    }
}
