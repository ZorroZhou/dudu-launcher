package com.wow.carlauncher.view.activity.set.view;

import android.content.Context;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.telephony.TelephonyManager;
import android.util.AttributeSet;
import android.view.View;

import com.wow.carlauncher.R;
import com.wow.carlauncher.common.util.AppUtil;
import com.wow.carlauncher.repertory.web.mobile.frame.WebApiManage;
import com.wow.carlauncher.repertory.web.mobile.frame.callback.SCallBack;
import com.wow.carlauncher.repertory.web.mobile.packet.user.LoginRes;
import com.wow.carlauncher.repertory.web.mobile.service.CommonApi;
import com.wow.carlauncher.view.base.BaseEXView;

import org.xutils.view.annotation.Event;

/**
 * Created by 10124 on 2018/4/22.
 */

public class SDevView extends BaseEXView {

    public SDevView(@NonNull Context context) {
        super(context);
    }

    public SDevView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected int getContent() {
        return R.layout.content_set_dev;
    }

    @Event(value = {R.id.sv_bind})
    private void clickEvent(View view) {
        switch (view.getId()) {
            case R.id.sv_bind:
                break;
        }
    }

}
