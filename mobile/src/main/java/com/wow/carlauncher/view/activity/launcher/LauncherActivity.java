package com.wow.carlauncher.view.activity.launcher;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.wow.carlauncher.R;
import com.wow.carlauncher.common.CommonData;
import com.wow.carlauncher.ex.manage.ThemeManage;
import com.wow.carlauncher.ex.manage.location.event.MNewLocationEvent;
import com.wow.carlauncher.ex.manage.toast.ToastManage;
import com.wow.carlauncher.ex.plugin.console.ConsolePlugin;
import com.wow.carlauncher.ex.plugin.console.event.PConsoleEventCallState;
import com.wow.carlauncher.ex.plugin.console.event.PConsoleEventLightState;
import com.wow.carlauncher.ex.plugin.fk.event.PFkEventAction;
import com.wow.carlauncher.ex.plugin.music.MusicPlugin;
import com.wow.carlauncher.view.activity.AppSelectActivity;
import com.wow.carlauncher.view.activity.launcher.event.LItemRefreshEvent;
import com.wow.carlauncher.view.activity.launcher.view.LDockView;
import com.wow.carlauncher.view.activity.launcher.view.LPageView;
import com.wow.carlauncher.common.util.SharedPreUtil;
import com.wow.carlauncher.common.util.ViewUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import per.goweii.anypermission.AnyPermission;
import per.goweii.anypermission.RequestInterceptor;
import per.goweii.anypermission.RequestListener;
import per.goweii.anypermission.RuntimeRequester;

import static com.wow.carlauncher.common.CommonData.IDATA_APP_MARK;
import static com.wow.carlauncher.common.CommonData.IDATA_APP_PACKAGE_NAME;
import static com.wow.carlauncher.common.CommonData.REQUEST_SELECT_APP_TO_DOCK1;
import static com.wow.carlauncher.common.CommonData.REQUEST_SELECT_APP_TO_DOCK2;
import static com.wow.carlauncher.common.CommonData.REQUEST_SELECT_APP_TO_DOCK3;
import static com.wow.carlauncher.common.CommonData.REQUEST_SELECT_APP_TO_DOCK4;
import static com.wow.carlauncher.common.CommonData.REQUEST_SELECT_APP_TO_DOCK5;
import static com.wow.carlauncher.common.CommonData.SDATA_APP_THEME;
import static com.wow.carlauncher.common.CommonData.SDATA_DOCK1_CLASS;
import static com.wow.carlauncher.common.CommonData.SDATA_DOCK2_CLASS;
import static com.wow.carlauncher.common.CommonData.SDATA_DOCK3_CLASS;
import static com.wow.carlauncher.common.CommonData.SDATA_DOCK4_CLASS;
import static com.wow.carlauncher.common.CommonData.SDATA_DOCK5_CLASS;
import static com.wow.carlauncher.ex.manage.ThemeManage.BLACK;
import static com.wow.carlauncher.ex.manage.ThemeManage.WHITE;
import static com.wow.carlauncher.ex.plugin.fk.FangkongProtocolEnum.YLFK;
import static com.wow.carlauncher.ex.plugin.fk.protocol.YiLianProtocol.*;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //超级大坑,必去全局设置才能用
        ThemeManage.self().refreshTheme();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        registerReceiver(homeReceiver, intentFilter);
        initView();

        //主题处理
        onThemeChanged(ThemeManage.self());
        ThemeManage.self().registerThemeChangeListener(this);
        ThemeManage.self().refreshTheme();

        LauncherActivity.activity = this;
    }

    public void initView() {
        setContentView(R.layout.activity_lanncher);
        x.view().inject(this);

        EventBus.getDefault().register(this);
        initItem();
        //requestRuntime();
    }

    private void initItem() {
        //计算排序
        List<ItemModel> items = new ArrayList<>();
        for (ItemEnum item : CommonData.LAUNCHER_ITEMS) {
            if (SharedPreUtil.getSharedPreBoolean(CommonData.SDATA_LAUNCHER_ITEM_OPEN_ + item.getId(), true)) {
                items.add(new ItemModel(item,
                        SharedPreUtil.getSharedPreInteger(CommonData.SDATA_LAUNCHER_ITEM_SORT_ + item.getId(), item.getId()),
                        SharedPreUtil.getSharedPreBoolean(CommonData.SDATA_LAUNCHER_ITEM_OPEN_ + item.getId(), true)
                ));
            }
        }

        Collections.sort(items, (o1, o2) -> o1.index - o2.index);
        int psize = SharedPreUtil.getSharedPreInteger(CommonData.SDATA_LAUNCHER_ITEM_NUM, 3);
        if (psize != 3) {
            psize = 4;
        }
        int pnum = items.size() / psize;
        if (items.size() % psize != 0) {
            pnum++;
        }

        LPageView[] pageViews = new LPageView[pnum];

        int npage = 0;
        LPageView pageView = null;
        View[] pcell = new View[psize];
        for (int i = 0; i < items.size(); i++) {
            if (i % psize == 0) {
                pageViews[npage] = new LPageView(this, psize);
                pcell = new View[psize];
                pageView = pageViews[npage];
                npage++;
            }
            pcell[i % psize] = ItemEnum.createView(this, items.get(i).info);
            //如果是一组的最后一个,或者是列表最后一个
            if (((i % psize == psize - 1) || (i == items.size() - 1)) && pageView != null) {
                pageView.setItem(pcell);
            }
        }
        viewPager.setAdapter(new ViewAdapter(pageViews));
        viewPager.clearOnPageChangeListeners();
        postion.removeAllViews();

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewUtils.dip2px(getApplicationContext(), 8), ViewUtils.dip2px(getApplicationContext(), 8));
        //设置小圆点左右之间的间隔
        params.setMargins(10, 0, 10, 0);

        //根据主题处理小圆点
        boolean baise = ThemeManage.self().getThemeMode() == WHITE;
        final View[] posts = new View[pageViews.length];
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
                boolean baise = ThemeManage.self().getThemeMode() == WHITE;
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
        if (mRuntimeRequester != null) {
            mRuntimeRequester.onActivityResult(requestCode);
        }
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
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(homeReceiver);
        EventBus.getDefault().unregister(this);
        ThemeManage.self().unregisterThemeChangeListener(this);
        LauncherActivity.activity = null;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(LItemRefreshEvent event) {
        initItem();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(PConsoleEventLightState event) {
        ThemeManage.ThemeMode model = ThemeManage.ThemeMode.getById(SharedPreUtil.getSharedPreInteger(SDATA_APP_THEME, ThemeManage.ThemeMode.SHIJIAN.getId()));
        if (model.equals(ThemeManage.ThemeMode.DENGGUANG)) {
            if (event.isOpen()) {
                ThemeManage.self().setThemeMode(BLACK);
            } else {
                ThemeManage.self().setThemeMode(WHITE);
            }
        }
    }

    @Override
    public void onBackPressed() {

    }

    private String lastadCode;

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent(MNewLocationEvent event) {
        if (!event.getAdCode().equals(lastadCode)) {
            lastadCode = event.getAdCode();
            ToastManage.self().show("定位成功:" + event.getCity());
        }
    }

    private boolean isCalling = false;

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent(final PConsoleEventCallState event) {
        isCalling = event.isCalling();
    }

    @Subscribe
    public void onEvent(PFkEventAction event) {
        if (YLFK.equals(event.getFangkongProtocol())) {
            switch (event.getAction()) {
                case LEFT_TOP_CLICK: {
                    ConsolePlugin.self().decVolume();
                    break;
                }
                case LEFT_TOP_LONG_CLICK: {
                    MusicPlugin.self().pre();
                    break;
                }
                case RIGHT_TOP_CLICK: {
                    ConsolePlugin.self().incVolume();
                    break;
                }
                case RIGHT_TOP_LONG_CLICK: {
                    MusicPlugin.self().next();
                    break;
                }
                case CENTER_CLICK: {
                    Intent home = new Intent(Intent.ACTION_MAIN);
                    home.addCategory(Intent.CATEGORY_HOME);
                    home.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(home);
                    break;
                }
                case CENTER_LONG_CLICK: {
                    //TODO 这里弹出来悬浮窗
                    break;
                }
                case LEFT_BOTTOM_CLICK: {
                    if (isCalling) {
                        ConsolePlugin.self().callAnswer();
                    } else {
                        ConsolePlugin.self().mute();
                    }
                    break;
                }
                case RIGHT_BOTTOM_CLICK:
                    if (isCalling) {
                        ConsolePlugin.self().callHangup();
                    } else {
                        //切换页面?
                        if (viewPager != null && viewPager.getAdapter() != null) {
                            if (viewPager.getAdapter().getCount() == viewPager.getCurrentItem() + 1) {
                                viewPager.setCurrentItem(0, true);
                            } else {
                                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);
                            }
                        }
                    }
                    break;
            }
        }
    }

    private RuntimeRequester mRuntimeRequester;

    private void requestRuntime() {
        mRuntimeRequester = AnyPermission.with(this).runtime(1)
                .permissions(Manifest.permission.LOCATION_HARDWARE,
                        Manifest.permission.CALL_PHONE)
                .onBeforeRequest(new RequestInterceptor<String>() {
                    @Override
                    public void intercept(@NonNull final String permission, @NonNull final Executor executor) {
                        // TODO 在每个权限申请之前调用，多次回调。可弹窗向用户说明下面将进行某个权限的申请。
                        // processor有两个方法，必须调用其一，否则申请流程终止。
                    }
                })
                .onBeenDenied(new RequestInterceptor<String>() {
                    @Override
                    public void intercept(@NonNull final String permission, @NonNull final Executor executor) {
                        // TODO 在每个权限被拒后调用，多次回调。可弹窗向用户说明为什么需要该权限，否则用户可能在下次申请勾选不再提示。
                        // processor有两个方法，必须调用其一，否则申请流程终止。
                    }
                })
                .onGoSetting(new RequestInterceptor<String>() {
                    @Override
                    public void intercept(@NonNull final String permission, @NonNull final Executor executor) {
                        // TODO 在每个权限永久被拒后调用（即用户勾选不再提示），多次回调。可弹窗引导用户前往设置打开权限，调用executor.execute()会自动跳转设置。
                        // processor有两个方法，必须调用其一，否则申请流程将终止。
                    }
                })
                .request(new RequestListener() {
                    @Override
                    public void onSuccess() {
                        // TODO 授权成功
                    }

                    @Override
                    public void onFailed() {
                        // TODO 授权失败
                    }
                });
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
