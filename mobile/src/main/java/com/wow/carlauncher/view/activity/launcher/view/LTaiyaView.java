package com.wow.carlauncher.view.activity.launcher.view;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wow.carlauncher.R;
import com.wow.carlauncher.common.LukuangView;
import com.wow.carlauncher.ex.plugin.amapcar.AMapCarPlugin;
import com.wow.carlauncher.ex.plugin.amapcar.event.PAmapEventNavInfo;
import com.wow.carlauncher.ex.plugin.amapcar.event.PAmapEventState;
import com.wow.carlauncher.ex.plugin.amapcar.event.PAmapLukuangInfo;
import com.wow.carlauncher.ex.plugin.amapcar.event.PAmapMuteStateInfo;
import com.wow.carlauncher.ex.plugin.amapcar.model.Lukuang;
import com.wow.carlauncher.view.base.BaseEBusView;
import com.wow.frame.util.AppUtil;
import com.wow.frame.util.CommonUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.wow.carlauncher.common.CommonData.TAG;
import static com.wow.carlauncher.ex.plugin.amapcar.AMapCarConstant.AMAP_PACKAGE;
import static com.wow.carlauncher.ex.plugin.amapcar.AMapCarConstant.ICONS;

/**
 * Created by 10124 on 2018/4/20.
 */

public class LTaiyaView extends BaseEBusView {

    public LTaiyaView(@NonNull Context context) {
        super(context);
        addContent(R.layout.content_l_taiya);
    }

    public LTaiyaView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        addContent(R.layout.content_l_taiya);
    }


}
