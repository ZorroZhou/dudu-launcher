package com.wow.carlauncher.view.activity.driving.blue1;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

import com.wow.carlauncher.R;
import com.wow.carlauncher.ex.manage.time.event.MTimeSecondEvent;
import com.wow.carlauncher.ex.manage.trip.TripManage;
import com.wow.carlauncher.view.base.BaseEBusView;
import com.wow.frame.util.DateUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.view.annotation.ViewInject;

import java.util.Date;
import java.util.Locale;

/**
 * Created by 10124 on 2018/5/11.
 */

public class Blue1View extends BaseEBusView {
    public Blue1View(@NonNull Context context) {
        super(context);
        addContent(R.layout.content_driving_blue1);
    }

    public Blue1View(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        addContent(R.layout.content_driving_blue1);
    }
}

