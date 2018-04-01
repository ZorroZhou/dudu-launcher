package com.wow.carlauncher.popupWindow;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.os.Build;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wow.carlauncher.plugin.amapcar.AMapCarPlugin;
import com.wow.carlauncher.plugin.amapcar.AMapCarPluginListener;
import com.wow.carlauncher.plugin.amapcar.NaviInfo;
import com.wow.carlauncher.plugin.music.MusicPlugin;
import com.wow.carlauncher.plugin.music.MusicPluginListener;
import com.wow.frame.util.AppUtil;
import com.wow.frame.util.CommonUtil;
import com.wow.frame.util.DateUtil;
import com.wow.frame.util.SharedPreUtil;
import com.wow.carlauncher.CarLauncherApplication;
import com.wow.carlauncher.R;
import com.wow.carlauncher.common.CommonData;
import com.wow.carlauncher.event.PopupIsFullScreenRefreshEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.xutils.x;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import static com.wow.carlauncher.common.CommonData.*;
import static com.wow.carlauncher.plugin.amapcar.AMapCarConstant.*;

public class PopupWin {
    private static PopupWin self;

    public static PopupWin self() {
        if (self == null) {
            self = new PopupWin();
        }
        return self;
    }

    private PopupWin() {

    }

    //窗口管理器
    private WindowManager wm = null;
    //窗口的布局参数
    private WindowManager.LayoutParams winparams;
    //是否展示了
    private Boolean isShow = false;
    private CarLauncherApplication context;
    //窗口视图
    private View popupWindow;
    //插件试图
    private LinearLayout pluginHome;
    private TextView tv_time;

    private int rank = 2;

    private int owidth = 0;
    private int oheight = 0;

    public void setRank(int rank) {
        winparams.width = owidth * rank;
        winparams.height = oheight * rank;
        this.rank = rank;
    }

    public void init(CarLauncherApplication context) {


        this.context = context;
        wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        EventBus.getDefault().register(this);

        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        int screenWidth = outMetrics.widthPixels;


        oheight = (int) (screenWidth * 0.15 / 3);
        owidth = oheight / 2 * 3;

        winparams = new WindowManager.LayoutParams();
        // 类型
        winparams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        if (SharedPreUtil.getSharedPreBoolean(CommonData.SDATA_POPUP_FULL_SCREEN, true)) {
            winparams.flags = WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN | WindowManager.LayoutParams.FLAG_FULLSCREEN;
        } else {
            winparams.flags = WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_FULLSCREEN;
        }
        winparams.format = PixelFormat.TRANSLUCENT;
        winparams.width = owidth * rank;
        winparams.height = oheight * rank;

        winparams.gravity = Gravity.TOP | Gravity.START;
        winparams.x = SharedPreUtil.getSharedPreInteger(CommonData.SDATA_POPUP_WIN_X, 0);
        winparams.y = SharedPreUtil.getSharedPreInteger(CommonData.SDATA_POPUP_WIN_Y, 0);

        popupWindow = View.inflate(context, R.layout.popup_window, null);
        pluginHome = (LinearLayout) popupWindow.findViewById(R.id.ll_plugin);
        tv_time = (TextView) popupWindow.findViewById(R.id.tv_time);

        popupWindow.findViewById(R.id.ll_yidong).setOnTouchListener(moveTouchListener);
        popupWindow.findViewById(R.id.ll_xunhuan).setOnClickListener(onClickListener);

        MusicPlugin.self().addListener(musicPluginListener);
        AMapCarPlugin.self().addListener(aMapCarPluginListener);
    }

    private MusicPluginListener musicPluginListener = new MusicPluginListener() {
        @Override
        public void refreshInfo(String title, String artist) {
            if (CommonUtil.isNotNull(title)) {
                ((TextView) musicView().findViewById(R.id.tv_title)).setText(title);
            } else {
                ((TextView) musicView().findViewById(R.id.tv_title)).setText("标题");
            }

        }

        @Override
        public void refreshProgress(int curr_time, int total_time) {
            if (total_time > 0) {
                ProgressBar progressBar = ((ProgressBar) musicView().findViewById(R.id.pb_music));
                progressBar.setProgress(curr_time);
                progressBar.setMax(total_time);
            }
        }

        @Override
        public void refreshCover(Bitmap cover) {

        }

        @Override
        public void refreshState(boolean run) {
            if (!run) {
                ((ImageView) musicView().findViewById(R.id.iv_play)).setImageResource(R.mipmap.ic_play);
            } else {
                ((ImageView) musicView().findViewById(R.id.iv_play)).setImageResource(R.mipmap.ic_pause);
            }
        }
    };

    private AMapCarPluginListener aMapCarPluginListener = new AMapCarPluginListener() {
        @Override
        public void refreshNaviInfo(NaviInfo naviBean) {
            ImageView popupIcon = (ImageView) amapView.findViewById(R.id.iv_icon);
            TextView popupdis = (TextView) amapView.findViewById(R.id.tv_dis);
            TextView popupmsg = (TextView) amapView.findViewById(R.id.tv_msg);
            LinearLayout popupcontroller = (LinearLayout) amapView.findViewById(R.id.ll_controller);
            RelativeLayout popupnavi = (RelativeLayout) amapView.findViewById(R.id.ll_navi);
            switch (naviBean.getType()) {
                case NaviInfo.TYPE_STATE: {
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
                    if (popupIcon != null && naviBean.getIcon() - 1 >= 0 && naviBean.getIcon() - 1 < ICONS.length) {
                        popupIcon.setImageResource(ICONS[naviBean.getIcon() - 1]);
                    }
                    if (popupdis != null && naviBean.getDis() > -1) {
                        String msg = "";
                        if (naviBean.getDis() < 10) {
                            msg = "现在";
                        } else {
                            if (naviBean.getDis() > 1000) {
                                msg = naviBean.getDis() / 1000 + "公里后";
                            } else {
                                msg = naviBean.getDis() + "米后";
                            }
                        }
                        msg = msg + naviBean.getWroad();
                        popupdis.setText(msg);
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
    };

    @Subscribe
    public void onEventMainThread(PopupIsFullScreenRefreshEvent event) {
        Log.e(TAG, "onEventMainThread: " + event);
        if (SharedPreUtil.getSharedPreBoolean(CommonData.SDATA_POPUP_FULL_SCREEN, true)) {
            winparams.flags = WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN | WindowManager.LayoutParams.FLAG_FULLSCREEN;
        } else {
            winparams.flags = WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        }
        if (isShow) {
            wm.updateViewLayout(popupWindow, winparams);
        }
    }

    private String nowApp = "";

    private void checkShowApp(final String app) {
        //如果APP是空的,则说明用户没有打开权限,则直接不显示了
        if (!SharedPreUtil.getSharedPreBoolean(CommonData.SDATA_POPUP_SHOW_TYPE, true)) {
            if (CommonUtil.isNull(app)) {
                x.task().autoPost(new Runnable() {
                    @Override
                    public void run() {
                        popupWindow.setVisibility(View.GONE);
                    }
                });
            }
        }

        //如果不显示了,或者传进来的app参数是空的
        if (!isShow) {
            return;
        }
        //如果APP没切换,也不用处理了
        if (nowApp != null && nowApp.equals(app)) {
            return;
        }
        this.nowApp = app;
        if (this.nowApp == null) {
            this.nowApp = PACKAGE_NAME;
        }
        x.task().autoPost(new Runnable() {
            @Override
            public void run() {
                if (!SharedPreUtil.getSharedPreBoolean(CommonData.SDATA_POPUP_SHOW_TYPE, true)) {
                    final String selectapp = SharedPreUtil.getSharedPreString(CommonData.SDATA_POPUP_SHOW_APPS);
                    if (selectapp.contains("[" + app + "]")) {
                        popupWindow.setVisibility(View.VISIBLE);
                    } else {
                        popupWindow.setVisibility(View.GONE);
                    }
                } else {
                    popupWindow.setVisibility(View.VISIBLE);
                }
                if (popupWindow.getVisibility() == View.GONE) {
                    return;
                }
                showPlugin(false);
            }
        });
    }

    private void showPlugin(boolean goNext) {
        int pluginId = SharedPreUtil.getSharedPreInteger(SDATA_POPUP_CURRENT_PLUGIN, 1);
        if (goNext) {
            if (pluginId >= 3) {
                pluginId = 1;
            } else {
                pluginId = pluginId + 1;
            }
        }
        SharedPreUtil.saveSharedPreInteger(SDATA_POPUP_CURRENT_PLUGIN, pluginId);
        if (pluginId == 1) {
            pluginHome.setVisibility(View.GONE);
            winparams.width = owidth * rank;
            winparams.height = oheight * rank;

            wm.updateViewLayout(popupWindow, winparams);

        } else if (pluginId == 2) {
            pluginHome.setVisibility(View.VISIBLE);
            pluginHome.removeAllViews();
            pluginHome.addView(musicView(), LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

            winparams.width = owidth * 3 * rank;
            winparams.height = oheight * rank;
            wm.updateViewLayout(popupWindow, winparams);

        } else if (pluginId == 3) {
            pluginHome.setVisibility(View.VISIBLE);
            pluginHome.removeAllViews();
            pluginHome.addView(amapView(), LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

            winparams.width = owidth * 3 * rank;
            winparams.height = oheight * rank;
            wm.updateViewLayout(popupWindow, winparams);

        }
    }

    private FrameLayout amapView;

    private View amapView() {
        if (amapView == null) {
            amapView = (FrameLayout) View.inflate(context, R.layout.plugin_amap_popup, null);
            View.OnClickListener clickListener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (view.getId()) {
                        case R.id.btn_search:
                            Intent appIntent = context.getPackageManager().getLaunchIntentForPackage(AMAP_PACKAGE);
                            if (appIntent == null) {
                                Toast.makeText(context, "没有安装高德地图", Toast.LENGTH_SHORT).show();
                                break;
                            }
                            appIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(appIntent);
                            break;
                        case R.id.btn_go_home:
                            AMapCarPlugin.self().getHome();
                            break;
                        case R.id.btn_go_company:
                            AMapCarPlugin.self().getComp();
                            break;
                    }
                }
            };
            amapView.findViewById(R.id.btn_search).setOnClickListener(clickListener);
            amapView.findViewById(R.id.btn_go_home).setOnClickListener(clickListener);
            amapView.findViewById(R.id.btn_go_company).setOnClickListener(clickListener);
        }
        return amapView;
    }

    private LinearLayout musicView;

    private View musicView() {
        if (musicView == null) {
            musicView = (LinearLayout) View.inflate(context, R.layout.plugin_music_popup, null);
            View.OnClickListener clickListener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (view.getId()) {
                        case R.id.ll_play:
                            MusicPlugin.self().playOrPause();
                            break;
                        case R.id.ll_next:
                            MusicPlugin.self().next();
                            break;
                        case R.id.ll_prew:
                            MusicPlugin.self().pre();
                            break;
                    }
                }
            };
            musicView.findViewById(R.id.ll_prew).setOnClickListener(clickListener);
            musicView.findViewById(R.id.ll_next).setOnClickListener(clickListener);
            musicView.findViewById(R.id.ll_play).setOnClickListener(clickListener);
        }
        return musicView;
    }

    public synchronized void checkShow(int count) {
        if (context.checkActivity(count) > 0) {
            hide();
        } else {
            show();
        }
    }

    //隐藏方法
    private void hide() {
        if (isShow) {
            wm.removeView(popupWindow);
            isShow = false;

            if (timer != null) {
                timer.cancel();
                timer = null;
            }
        }
    }

    private Timer timer;

    //显示方法
    private void show() {
        if (Build.VERSION.SDK_INT >= 23 && !Settings.canDrawOverlays(context)) {
            return;
        }
        if (!SharedPreUtil.getSharedPreBoolean(SDATA_POPUP_ALLOW_SHOW, true)) {
            return;
        }
        if (!isShow) {
            wm.addView(popupWindow, winparams);
            isShow = true;
        }
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                checkShowApp(AppUtil.getForegroundApp(context));
                setTime();
            }
        }, 500 - System.currentTimeMillis() % 500, 500);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ll_xunhuan: {
                    showPlugin(true);
                    break;
                }
            }
        }
    };

    private View.OnTouchListener moveTouchListener = new View.OnTouchListener() {
        private int tx, ty;

        @Override
        public boolean onTouch(View v, MotionEvent e) {
            if (e.getAction() == MotionEvent.ACTION_DOWN) {
                tx = (int) e.getX();
                ty = (int) e.getY();
                return true;
            } else if (e.getAction() == MotionEvent.ACTION_MOVE) {
                winparams.x = (int) (e.getRawX() - tx);
                winparams.y = (int) (e.getRawY() - ty);
                wm.updateViewLayout(popupWindow, winparams);
                return true;
            } else if (e.getAction() == MotionEvent.ACTION_UP) {
                SharedPreUtil.saveSharedPreInteger(CommonData.SDATA_POPUP_WIN_X, winparams.x);
                SharedPreUtil.saveSharedPreInteger(CommonData.SDATA_POPUP_WIN_Y, winparams.y);
                return true;
            }
            return false;
        }
    };

    private void setTime() {
        x.task().autoPost(new Runnable() {
            @Override
            public void run() {
                Date d = new Date();
                String datetime = DateUtil.dateToString(d, "MM月dd日 HH:mm");
                tv_time.setText(datetime);
            }
        });
    }
}
