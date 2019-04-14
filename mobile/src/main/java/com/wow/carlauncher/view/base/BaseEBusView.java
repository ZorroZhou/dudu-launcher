package com.wow.carlauncher.view.base;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.lang.reflect.Method;

/**
 * Created by 10124 on 2018/4/22.
 */

public class BaseEBusView extends BaseView {
    public BaseEBusView(@NonNull Context context) {
        super(context);
    }

    public BaseEBusView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        //判断是否有注解,有的话再注册
        boolean have = false;
        Method[] methods = getClass().getMethods();
        for (Method m : methods) {
            Subscribe meta = m.getAnnotation(Subscribe.class);
            if (meta != null) {
                have = true;
                break;
            }
        }
        if (have) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
}
