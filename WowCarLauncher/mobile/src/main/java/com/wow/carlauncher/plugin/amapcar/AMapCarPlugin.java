package com.wow.carlauncher.plugin.amapcar;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wow.carlauncher.R;
import com.wow.carlauncher.common.BaseDialog;
import com.wow.carlauncher.common.util.AppUtil;
import com.wow.carlauncher.common.util.CommonUtil;
import com.wow.carlauncher.dialog.InputDialog;
import com.wow.carlauncher.plugin.IPlugin;
import com.wow.carlauncher.plugin.PluginManage;

import java.math.BigDecimal;

import static com.wow.carlauncher.plugin.amapcar.AMapCarConstant.*;

/**
 * Created by 10124 on 2017/11/4.
 */

public class AMapCarPlugin implements IPlugin, View.OnClickListener {
    public static final String TAG = "AMapCarPlugin";

    private PluginManage pluginManage;

    public PluginManage getPluginManage() {
        return pluginManage;
    }

    private Context context;

    public Context getContext() {
        return context;
    }

    private FrameLayout popupView;
    private ImageView popupIcon;
    private TextView popupdis;
    private TextView popuproad;
    private TextView popupmsg;
    private LinearLayout popupcontroller;
    private LinearLayout popupnavi;

    private RelativeLayout launcherView;
    private View launcherController;
    private ImageView launcherIcon;
    private LinearLayout launchernavi;
    private TextView launcherdis;
    private TextView launcherroad;
    private TextView launchermsg;

    private AMapCartReceiver amapReceiver;
    private AMapCartSend amapSend;

    public AMapCartSend getAmapSend() {
        return amapSend;
    }

    public AMapCarPlugin(Context context, PluginManage pluginManage) {
        this.pluginManage = pluginManage;
        this.context = context;
        amapReceiver = new AMapCartReceiver(this);
        amapSend = new AMapCartSend(this);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(RECEIVE_ACTION);
        context.registerReceiver(amapReceiver, intentFilter);
    }

    private void initPopupView(View view) {
        popupIcon = view.findViewById(R.id.iv_icon);
        popupdis = view.findViewById(R.id.tv_dis);
        popuproad = view.findViewById(R.id.tv_road);
        popupmsg = view.findViewById(R.id.tv_msg);
        popupcontroller = view.findViewById(R.id.ll_controller);
        popupnavi = view.findViewById(R.id.ll_navi);

        view.findViewById(R.id.btn_search).setOnClickListener(this);
        view.findViewById(R.id.btn_go_home).setOnClickListener(this);
        view.findViewById(R.id.btn_go_company).setOnClickListener(this);
    }

    private void initLauncherView(View view) {
        view.findViewById(R.id.rl_base).setOnClickListener(this);
        view.findViewById(R.id.btn_search).setOnClickListener(this);
        view.findViewById(R.id.btn_go_home).setOnClickListener(this);
        view.findViewById(R.id.btn_go_company).setOnClickListener(this);

        launcherIcon = view.findViewById(R.id.iv_icon);
        launcherController = view.findViewById(R.id.ll_controller);
        launchernavi = view.findViewById(R.id.ll_navi);
        launcherdis = view.findViewById(R.id.tv_dis);
        launcherroad = view.findViewById(R.id.tv_road);
        launchermsg = view.findViewById(R.id.tv_msg);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_base: {
                Intent appIntent = context.getPackageManager().getLaunchIntentForPackage(AMAP_PACKAGE);
                if (appIntent == null) {
                    Toast.makeText(context, "没有安装高德地图", Toast.LENGTH_SHORT).show();
                    break;
                }
                appIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(appIntent);
                break;
            }
            case R.id.btn_search: {
                if (!AppUtil.isInstall(context, AMAP_PACKAGE)) {
                    Toast.makeText(context, "没有安装高德地图", Toast.LENGTH_SHORT).show();
                    break;
                }
                new InputDialog(pluginManage.getCurrentActivity())
                        .setBtn1("取消", null)
                        .setBtn2("确定", new BaseDialog.OnBtnClickListener() {
                            @Override
                            public boolean onClick(BaseDialog dialog) {
                                String text = ((EditText) dialog.findViewById(R.id.et_input)).getText().toString();
                                if (CommonUtil.isNotNull(text)) {
                                    amapSend.search(text);
                                } else {
                                    Toast.makeText(context, "请输入目的地", Toast.LENGTH_SHORT).show();
                                }
                                return true;
                            }
                        }).show();
                break;
            }
            case R.id.btn_go_home: {
                if (!AppUtil.isInstall(context, AMAP_PACKAGE)) {
                    Toast.makeText(context, "没有安装高德地图", Toast.LENGTH_SHORT).show();
                    break;
                }
                amapSend.getHome();
                break;
            }
            case R.id.btn_go_company: {
                if (!AppUtil.isInstall(context, AMAP_PACKAGE)) {
                    Toast.makeText(context, "没有安装高德地图", Toast.LENGTH_SHORT).show();
                    break;
                }
                amapSend.getComp();
                break;
            }
        }
    }

    public void refreshNaviInfo(NaviInfo naviBean) {
        Log.e(TAG, "refreshNaviInfo:" + naviBean);
        switch (naviBean.getType()) {
            case NaviInfo.TYPE_STATE: {
                if (launcherController != null) {
                    if (naviBean.getState() == 8 || naviBean.getState() == 10) {
                        launcherController.setVisibility(View.GONE);
                        launchernavi.setVisibility(View.VISIBLE);
                    } else if (naviBean.getState() == 9 || naviBean.getState() == 11) {
                        launcherController.setVisibility(View.VISIBLE);
                        launchernavi.setVisibility(View.GONE);
                        launcherIcon.setImageResource(R.mipmap.ic_amap);
                    }
                }

                if (popupcontroller != null && popupnavi != null) {
                    if (naviBean.getState() == 8 || naviBean.getState() == 10) {
                        popupcontroller.setVisibility(View.GONE);
                        popupnavi.setVisibility(View.VISIBLE);
                    } else if (naviBean.getState() == 9 || naviBean.getState() == 11) {
                        popupcontroller.setVisibility(View.VISIBLE);
                        popupnavi.setVisibility(View.GONE);
                    }
                }
                break;
            }
            case NaviInfo.TYPE_NAVI: {
                if (launcherIcon != null && naviBean.getIcon() - 1 >= 0 && naviBean.getIcon() - 1 < ICONS.length) {
                    launcherIcon.setImageResource(ICONS[naviBean.getIcon() - 1]);
                }
                if (launcherdis != null && naviBean.getDis() > -1) {
                    if (naviBean.getDis() < 10) {
                        launcherdis.setText("现在");
                    } else {
                        if (naviBean.getDis() > 1000) {
                            String msg = naviBean.getDis() / 1000 + "公里后";
                            launcherdis.setText(msg);
                        } else {
                            String msg = naviBean.getDis() + "米后";
                            launcherdis.setText(msg);
                        }

                    }
                }
                if (launcherroad != null && CommonUtil.isNotNull(naviBean.getWroad())) {
                    launcherroad.setText(naviBean.getWroad());
                }
                if (launchermsg != null && naviBean.getRemainTime() > -1 && naviBean.getRemainDis() > -1) {
                    if (naviBean.getRemainTime() == 0 || naviBean.getRemainDis() == 0) {
                        launchermsg.setText("到达");
                    } else {
                        String msg = "剩余" + new BigDecimal(naviBean.getRemainDis() / 1000f).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue() + "公里  " +
                                naviBean.getRemainTime() / 60 + "分钟";
                        launchermsg.setText(msg);
                    }
                }


                if (popupIcon != null && naviBean.getIcon() - 1 >= 0 && naviBean.getIcon() - 1 < ICONS.length) {
                    popupIcon.setImageResource(ICONS[naviBean.getIcon() - 1]);
                }

                if (popupdis != null && naviBean.getDis() > -1) {
                    if (naviBean.getDis() < 10) {
                        popupdis.setText("现在");
                    } else {
                        if (naviBean.getDis() > 1000) {
                            String msg = naviBean.getDis() / 1000 + "公里后";
                            popupdis.setText(msg);
                        } else {
                            String msg = naviBean.getDis() + "米后";
                            popupdis.setText(msg);
                        }

                    }
                }
                if (popuproad != null && CommonUtil.isNotNull(naviBean.getWroad())) {
                    popuproad.setText(naviBean.getWroad());
                }
                if (popupmsg != null && naviBean.getRemainTime() > -1 && naviBean.getRemainDis() > -1) {
                    if (naviBean.getRemainTime() == 0 || naviBean.getRemainDis() == 0) {
                        popupmsg.setText("到达");
                    } else {
                        String msg = "剩余" + new BigDecimal(naviBean.getRemainDis() / 1000f).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue() + "公里  " +
                                naviBean.getRemainTime() / 60 + "分钟";
                        popupmsg.setText(msg);
                    }
                }
                break;
            }
        }

    }

    @Override
    public View getLauncherView() {
        if (launcherView == null) {
            launcherView = (RelativeLayout) View.inflate(context, R.layout.plugin_amap_launcher, null);
            initLauncherView(launcherView);
        }
        return launcherView;
    }

    @Override
    public View getPopupView() {
        if (popupView == null) {
            popupView = (FrameLayout) View.inflate(context, R.layout.plugin_amap_popup, null);
            initPopupView(popupView);
        }
        return popupView;
    }

    @Override
    public void destroy() {
        context.unregisterReceiver(amapReceiver);
    }
}
