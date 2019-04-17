package com.wow.carlauncher.view.base;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.AbsSavedState;
import android.view.View;
import android.widget.FrameLayout;

import com.wow.carlauncher.R;
import com.wow.carlauncher.common.CommonData;

import org.xutils.x;

/**
 * Created by 10124 on 2018/4/22.
 */

public abstract class BaseView extends FrameLayout {
    public BaseView(@NonNull Context context) {
        super(context);
//        initView(null);
    }

    public BaseView(@NonNull Context context, Bundle savedInstanceState) {
        super(context);
//        initView(savedInstanceState);
    }

    public BaseView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
//        initView(null);
    }

//    protected void initView(Bundle savedInstanceState){
//
//    };

//    protected abstract int getContent();

    protected void addContent(int r) {
        View amapView = View.inflate(getContext(), r, null);
        this.addView(amapView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        x.view().inject(this);
    }

    public void onSaveInstanceState(Bundle outState) {
        System.out.println("!!!!!!!!!!1");
    }

}
