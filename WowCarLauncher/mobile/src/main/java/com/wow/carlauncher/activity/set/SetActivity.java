package com.wow.carlauncher.activity.set;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.wow.carAssistant.packet.response.common.GetAppUpdateRes;
import com.wow.carlauncher.activity.AboutActivity;
import com.wow.carlauncher.activity.set.view.SAppView;
import com.wow.carlauncher.activity.set.view.SFangKongView;
import com.wow.carlauncher.activity.set.view.SHelpView;
import com.wow.carlauncher.activity.set.view.SObdView;
import com.wow.carlauncher.activity.set.view.SPopupView;
import com.wow.carlauncher.activity.set.view.SSystemView;
import com.wow.carlauncher.activity.set.view.STimeView;
import com.wow.carlauncher.webservice.service.CommonService;
import com.wow.frame.repertory.remote.WebServiceManage;
import com.wow.frame.repertory.remote.WebTask;
import com.wow.frame.repertory.remote.callback.SCallBack;
import com.wow.frame.repertory.remote.callback.SProgressCallback;
import com.wow.frame.util.AndroidUtil;
import com.wow.frame.util.CommonUtil;
import com.wow.frame.util.SharedPreUtil;
import com.wow.carlauncher.R;
import com.wow.carlauncher.common.AppInfoManage;
import com.wow.carlauncher.common.BaseActivity;
import com.wow.carlauncher.common.BaseDialog;
import com.wow.carlauncher.common.CommonData;
import com.wow.frame.util.AppUtil.AppInfo;
import com.wow.carlauncher.common.view.SetView;
import com.wow.carlauncher.dialog.CityDialog;
import com.wow.carlauncher.event.LauncherCityRefreshEvent;
import com.wow.frame.util.ThreadObj;

import org.greenrobot.eventbus.EventBus;
import org.xutils.view.annotation.ViewInject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.wow.carlauncher.common.CommonData.*;

public class SetActivity extends BaseActivity {
    @Override
    public void init() {
        setContent(R.layout.activity_set);
    }

    @ViewInject(R.id.sg_app)
    private SetView sg_app;
    @ViewInject(R.id.sg_popup)
    private SetView sg_popup;
    @ViewInject(R.id.sg_time)
    private SetView sg_time;
    @ViewInject(R.id.sg_help)
    private SetView sg_help;
    @ViewInject(R.id.sg_system_set)
    private SetView sg_system_set;
    @ViewInject(R.id.sg_fangkong)
    private SetView sg_fangkong;
    @ViewInject(R.id.sg_obd)
    private SetView sg_obd;

    @ViewInject(R.id.ll_app)
    private SAppView ll_app;
    @ViewInject(R.id.ll_fangkong)
    private SFangKongView ll_fangkong;
    @ViewInject(R.id.ll_obd)
    private SObdView ll_obd;

    @ViewInject(R.id.ll_popup)
    private SPopupView ll_popup;

    @ViewInject(R.id.ll_time)
    private STimeView ll_time;

    @ViewInject(R.id.ll_help)
    private SHelpView ll_help;

    @ViewInject(R.id.ll_system_set)
    private SSystemView ll_system_set;

    @Override
    public void initView() {
        setTitle("设置");
        View.OnClickListener groupClick = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ll_app.setVisibility(View.GONE);
                ll_popup.setVisibility(View.GONE);
                ll_time.setVisibility(View.GONE);
                ll_help.setVisibility(View.GONE);
                ll_system_set.setVisibility(View.GONE);
                ll_fangkong.setVisibility(View.GONE);
                ll_obd.setVisibility(View.GONE);

                switch (view.getId()) {
                    case R.id.sg_app: {
                        ll_app.setVisibility(View.VISIBLE);
                        break;
                    }
                    case R.id.sg_popup: {
                        ll_popup.setVisibility(View.VISIBLE);
                        break;
                    }
                    case R.id.sg_time: {
                        ll_time.setVisibility(View.VISIBLE);
                        break;
                    }
                    case R.id.sg_fangkong: {
                        ll_fangkong.setVisibility(View.VISIBLE);
                        break;
                    }
                    case R.id.sg_help: {
                        ll_help.setVisibility(View.VISIBLE);
                        break;
                    }
                    case R.id.sg_system_set: {
                        ll_system_set.setVisibility(View.VISIBLE);
                        break;
                    }
                    case R.id.sg_obd: {
                        ll_obd.setVisibility(View.VISIBLE);
                        break;
                    }
                    default: {
                        ll_app.setVisibility(View.VISIBLE);
                    }
                }
            }
        };

        sg_app.setOnClickListener(groupClick);
        sg_popup.setOnClickListener(groupClick);
        sg_time.setOnClickListener(groupClick);
        sg_help.setOnClickListener(groupClick);
        sg_system_set.setOnClickListener(groupClick);
        sg_fangkong.setOnClickListener(groupClick);
        sg_obd.setOnClickListener(groupClick);
    }
}
