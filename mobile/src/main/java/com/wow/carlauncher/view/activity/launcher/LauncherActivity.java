package com.wow.carlauncher.view.activity.launcher;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.wow.carlauncher.CarLauncherApplication;
import com.wow.carlauncher.R;
import com.wow.carlauncher.common.CommonData;
import com.wow.carlauncher.ex.manage.ThemeManage;
import com.wow.carlauncher.ex.manage.location.event.MNewLocationEvent;
import com.wow.carlauncher.ex.plugin.console.event.PConsoleEventLightState;
import com.wow.carlauncher.view.activity.AppSelectActivity;
import com.wow.carlauncher.view.activity.launcher.view.LDockView;
import com.wow.carlauncher.view.activity.launcher.view.LPage1View;
import com.wow.carlauncher.view.activity.launcher.view.LPage2View;
import com.wow.carlauncher.view.base.BaseView;
import com.wow.frame.util.SharedPreUtil;
import com.wow.frame.util.ViewUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import static com.wow.carlauncher.common.CommonData.IDATA_APP_MARK;
import static com.wow.carlauncher.common.CommonData.IDATA_APP_PACKAGE_NAME;
import static com.wow.carlauncher.common.CommonData.REQUEST_SELECT_APP_TO_DOCK1;
import static com.wow.carlauncher.common.CommonData.REQUEST_SELECT_APP_TO_DOCK2;
import static com.wow.carlauncher.common.CommonData.REQUEST_SELECT_APP_TO_DOCK3;
import static com.wow.carlauncher.common.CommonData.REQUEST_SELECT_APP_TO_DOCK4;
import static com.wow.carlauncher.common.CommonData.REQUEST_SELECT_APP_TO_DOCK5;
import static com.wow.carlauncher.common.CommonData.SDATA_DOCK1_CLASS;
import static com.wow.carlauncher.common.CommonData.SDATA_DOCK2_CLASS;
import static com.wow.carlauncher.common.CommonData.SDATA_DOCK3_CLASS;
import static com.wow.carlauncher.common.CommonData.SDATA_DOCK4_CLASS;
import static com.wow.carlauncher.common.CommonData.SDATA_DOCK5_CLASS;
import static com.wow.carlauncher.common.CommonData.TAG;
import static com.wow.carlauncher.ex.manage.ThemeManage.BLACK;
import static com.wow.carlauncher.ex.manage.ThemeManage.WHITE;

public class LauncherActivity extends Activity implements ThemeManage.OnThemeChangeListener {
    public static LauncherActivity activity;

    @ViewInject(R.id.ll_dock)
    private LDockView ll_dock;

    @ViewInject(R.id.viewPager)
    private ViewPager viewPager;

    @ViewInject(R.id.postion)
    private LinearLayout postion;

    @ViewInject(R.id.fl_bg)
    private View fl_bg;

    @ViewInject(R.id.line1)
    private View line1;

    private boolean night = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //超级大坑,必去全局设置才能用
        int theme = SharedPreUtil.getSharedPreInteger(CommonData.SDATA_APP_THEME, WHITE);
        if (theme == WHITE || theme == BLACK) {
            ThemeManage.self().setThemeMode(theme);
        }

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        registerReceiver(homeReceiver, intentFilter);

        initView();

        ThemeManage.self().registerThemeChangeListener(this);
        onThemeChanged(ThemeManage.self());
        LauncherActivity.activity = this;
    }

    public void initView() {
        setContentView(R.layout.activity_lanncher);
        x.view().inject(this);
        EventBus.getDefault().register(this);

        LPage1View lPage1View = new LPage1View(this);
        LPage2View lPage2View = new LPage2View(this);

        viewPager.setAdapter(new ViewAdapter(new View[]{lPage1View, lPage2View}));


        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewUtils.dip2px(getApplicationContext(), 8), ViewUtils.dip2px(getApplicationContext(), 8));
        //设置小圆点左右之间的间隔
        params.setMargins(10, 0, 10, 0);

        //根据主题处理小圆点
        boolean baise = SharedPreUtil.getSharedPreInteger(CommonData.SDATA_APP_THEME, R.style.AppThemeWhile) == R.style.AppThemeWhile;
        final View[] posts = new View[2];
        for (int i = 0; i < posts.length; i++) {
            posts[i] = new View(getApplicationContext());
            if (i == 0) {
                posts[i].setBackgroundResource(baise ? R.drawable.n_l_postion : R.drawable.n_l_postion_n);
            } else {
                posts[i].setBackgroundResource(baise ? R.drawable.n_l_postion_n : R.drawable.n_l_postion);
            }
            postion.addView(posts[i], params);
        }

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                boolean baise = SharedPreUtil.getSharedPreInteger(CommonData.SDATA_APP_THEME, R.style.AppThemeWhile) == R.style.AppThemeWhile;
                for (View post : posts) {
                    post.setBackgroundResource(baise ? R.drawable.n_l_postion_n : R.drawable.n_l_postion);
                }
                posts[i].setBackgroundResource(baise ? R.drawable.n_l_postion : R.drawable.n_l_postion_n);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    @Override
    public void onThemeChanged(ThemeManage manage) {
        Context context = getApplicationContext();
        fl_bg.setBackgroundResource(manage.getCurrentThemeRes(context, R.drawable.n_desk_bg));
        line1.setBackgroundResource(manage.getCurrentThemeRes(context, R.color.line));
    }

    @Event(value = {R.id.ll_dock1, R.id.ll_dock2, R.id.ll_dock3, R.id.ll_dock4}, type = View.OnLongClickListener.class)
    private boolean longClickEvent(View view) {
        switch (view.getId()) {
            case R.id.ll_dock1: {
                startActivityForResult(new Intent(this, AppSelectActivity.class), REQUEST_SELECT_APP_TO_DOCK1);
                break;
            }
            case R.id.ll_dock2: {
                startActivityForResult(new Intent(this, AppSelectActivity.class), REQUEST_SELECT_APP_TO_DOCK2);
                break;
            }
            case R.id.ll_dock3: {
                startActivityForResult(new Intent(this, AppSelectActivity.class), REQUEST_SELECT_APP_TO_DOCK3);
                break;
            }
            case R.id.ll_dock4: {
                startActivityForResult(new Intent(this, AppSelectActivity.class), REQUEST_SELECT_APP_TO_DOCK4);
                break;
            }
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SELECT_APP_TO_DOCK1) {
            if (resultCode == RESULT_OK) {
                String packName = data.getStringExtra(IDATA_APP_PACKAGE_NAME);
                int mark = data.getIntExtra(IDATA_APP_MARK, -1);
                SharedPreUtil.saveSharedPreString(SDATA_DOCK1_CLASS, mark + CommonData.CONSTANT_APP_PACKAGE_SEPARATE + packName);
                ll_dock.loadDock();
            }
        }
        if (requestCode == REQUEST_SELECT_APP_TO_DOCK2) {
            if (resultCode == RESULT_OK) {
                String packName = data.getStringExtra(IDATA_APP_PACKAGE_NAME);
                int mark = data.getIntExtra(IDATA_APP_MARK, -1);
                SharedPreUtil.saveSharedPreString(SDATA_DOCK2_CLASS, mark + CommonData.CONSTANT_APP_PACKAGE_SEPARATE + packName);
                ll_dock.loadDock();
            }
        }
        if (requestCode == REQUEST_SELECT_APP_TO_DOCK3) {
            if (resultCode == RESULT_OK) {
                String packName = data.getStringExtra(IDATA_APP_PACKAGE_NAME);
                int mark = data.getIntExtra(IDATA_APP_MARK, -1);
                SharedPreUtil.saveSharedPreString(SDATA_DOCK3_CLASS, mark + CommonData.CONSTANT_APP_PACKAGE_SEPARATE + packName);
                ll_dock.loadDock();
            }
        }
        if (requestCode == REQUEST_SELECT_APP_TO_DOCK4) {
            if (resultCode == RESULT_OK) {
                String packName = data.getStringExtra(IDATA_APP_PACKAGE_NAME);
                int mark = data.getIntExtra(IDATA_APP_MARK, -1);
                SharedPreUtil.saveSharedPreString(SDATA_DOCK4_CLASS, mark + CommonData.CONSTANT_APP_PACKAGE_SEPARATE + packName);
                ll_dock.loadDock();
            }
        }
        if (requestCode == REQUEST_SELECT_APP_TO_DOCK5) {
            if (resultCode == RESULT_OK) {
                String packName = data.getStringExtra(IDATA_APP_PACKAGE_NAME);
                int mark = data.getIntExtra(IDATA_APP_MARK, -1);
                SharedPreUtil.saveSharedPreString(SDATA_DOCK5_CLASS, mark + CommonData.CONSTANT_APP_PACKAGE_SEPARATE + packName);
                ll_dock.loadDock();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(this, "启动时间: " + (System.currentTimeMillis() - CarLauncherApplication.stime), Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(homeReceiver);
        EventBus.getDefault().unregister(this);
        ThemeManage.self().unregisterThemeChangeListener(this);
        LauncherActivity.activity = null;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(PConsoleEventLightState event) {
//        if (event.isOpen()) {
//            fl_bg.setBackgroundResource(R.mipmap.bg_l_midnight);
//        } else {
//            fl_bg.setBackgroundResource(R.mipmap.bg_l_bright);
//        }
    }

    @Override
    public void onBackPressed() {

    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent(MNewLocationEvent event) {

    }

    class ViewAdapter extends PagerAdapter {
        private View[] datas;

        public ViewAdapter(View[] list) {
            datas = list;
        }

        @Override
        public int getCount() {
            return datas.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = datas[position];
            container.addView(datas[position]);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(datas[position]);
        }
    }

    private BroadcastReceiver homeReceiver = new BroadcastReceiver() {
        String SYSTEM_REASON = "reason";
        String SYSTEM_HOME_KEY = "homekey";

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
                String reason = intent.getStringExtra(SYSTEM_REASON);
                if (TextUtils.equals(reason, SYSTEM_HOME_KEY)) {
                    //表示按了home键,程序到了后台
                    Intent i = new Intent(Intent.ACTION_MAIN, null);
                    i.addCategory(Intent.CATEGORY_HOME);
                    context.startActivity(i);
                }
            }
        }
    };
}
