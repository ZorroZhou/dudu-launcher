package com.wow.carlauncher.ex.manage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.support.v4.content.ContextCompat;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wow.carlauncher.R;
import com.wow.carlauncher.common.LogEx;
import com.wow.carlauncher.common.TaskExecutor;
import com.wow.carlauncher.common.util.SharedPreUtil;
import com.wow.carlauncher.common.util.SunRiseSetUtil;
import com.wow.carlauncher.ex.manage.location.event.MNewLocationEvent;
import com.wow.carlauncher.ex.manage.time.event.MTimeMinuteEvent;
import com.wow.carlauncher.ex.plugin.console.event.PConsoleEventLightState;
import com.wow.carlauncher.view.adapter.PicSelectAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.wow.carlauncher.common.CommonData.HOUR_MILL;
import static com.wow.carlauncher.common.CommonData.SDATA_APP_THEME;
import static com.wow.carlauncher.common.CommonData.SDATA_APP_THEME_DAY;
import static com.wow.carlauncher.common.CommonData.SDATA_APP_THEME_NIGHT;
import static com.wow.carlauncher.ex.manage.ThemeManage.Theme.BLACK;
import static com.wow.carlauncher.ex.manage.ThemeManage.Theme.CBLACK;
import static com.wow.carlauncher.ex.manage.ThemeManage.Theme.KBLACK;
import static com.wow.carlauncher.ex.manage.ThemeManage.Theme.WHITE;

public class ThemeManage {

    private static class SingletonHolder {
        @SuppressLint("StaticFieldLeak")
        private static ThemeManage instance = new ThemeManage();
    }

    public static ThemeManage self() {
        return ThemeManage.SingletonHolder.instance;
    }

    private ThemeManage() {
        super();
    }

    private Context context;

    public void init(Context context) {
        this.context = context;
        EventBus.getDefault().register(this);
        LogEx.d(this, "init ");
    }

    private double lat = 36.0577034969, lon = 120.3210639954;//这是青岛的某个坐标
    private Theme theme = WHITE;
    private List<OnThemeChangeListener> listeners = new LinkedList<>();
    private SparseArray<Map<String, Map<String, Integer>>> cachedResrouces = new SparseArray<>();
    private long lastChangeShijian = 0;
    private boolean currentDay = true;//0是未初始化,1是白天,2是黑天

    public void refreshTheme() {
        ThemeMode model = ThemeMode.getById(SharedPreUtil.getInteger(SDATA_APP_THEME, ThemeMode.SHIJIAN.getId()));
        LogEx.d(this, "refreshTheme : " + model);
        switch (model) {
            case BAISE:
                setTheme(WHITE);
                break;
            case HEISE:
                setTheme(BLACK);
                break;
            case CHUNHEI:
                setTheme(CBLACK);
                break;
            case KUHEI:
                setTheme(KBLACK);
                break;
            case SHIJIAN:
                if (SunRiseSetUtil.isNight(lon, lat, new Date())) {
                    if (!currentDay) {
                        if (System.currentTimeMillis() - lastChangeShijian < HOUR_MILL) {
                            break;
                        }
                        lastChangeShijian = System.currentTimeMillis();
                        currentDay = true;
                    }
                    LogEx.d(this, "refreshTheme : night");
                    setTheme(Theme.getById(SharedPreUtil.getInteger(SDATA_APP_THEME_NIGHT, Theme.BLACK.getId())));
                } else {
                    if (currentDay) {
                        if (System.currentTimeMillis() - lastChangeShijian < HOUR_MILL) {
                            break;
                        }
                        lastChangeShijian = System.currentTimeMillis();
                        currentDay = false;
                    }
                    LogEx.d(this, "refreshTheme : day");
                    setTheme(Theme.getById(SharedPreUtil.getInteger(SDATA_APP_THEME_DAY, Theme.WHITE.getId())));
                }
                break;
        }
    }

    /**
     * 设置主题
     */
    public void setTheme(Theme theme) {
        if (this.theme != theme) {
            LogEx.d(this, "setTheme : " + theme);
            this.theme = theme;
            if (listeners.size() > 0) {
                TaskExecutor.self().autoPost(() -> {
                    List<OnThemeChangeListener> temp = new ArrayList<>(listeners.size());
                    temp.addAll(listeners);
                    for (OnThemeChangeListener listener : temp) {
                        listener.onThemeChanged(ThemeManage.this);
                    }
                });
            }
        }
    }


    public int getCurrentThemeColor(int dayResId) {
        return ContextCompat.getColor(context, getCurrentThemeRes(dayResId));
    }

    public int getCurrentThemeRes(int dayResId) {
        return getCurrentThemeRes(getTheme(), dayResId);
    }

    public int getCurrentThemeRes(Theme theme, int originResId) {
        if (theme == WHITE) {
            return originResId;
        }
        Map<String, Map<String, Integer>> cachedResrouce = cachedResrouces.get(theme.id);
        if (cachedResrouce == null) {
            cachedResrouce = new HashMap<>();
            cachedResrouces.put(theme.id, cachedResrouce);
        }
        String entryName;
        String typeName;
        try {
            // 资源名
            entryName = context.getResources().getResourceEntryName(originResId);
            // 资源类型
            typeName = context.getResources().getResourceTypeName(originResId);
        } catch (Exception e) {
            return originResId;
        }
        Map<String, Integer> cachedRes = cachedResrouce.get(typeName);
        // 先从缓存中去取，如果有直接返回该id
        if (cachedRes == null) {
            cachedRes = new HashMap<>();
            cachedResrouce.put(typeName, cachedRes);
        }
        Integer resId = cachedRes.get(entryName + theme.suffix);
        if (resId != null && resId != 0) {
            return resId;
        }

        //如果缓存中没有再根据资源id去动态获取
        try {
            // 通过资源名，资源类型，包名得到资源int值
            int nightResId = context.getResources().getIdentifier(entryName + theme.suffix, typeName, context.getPackageName());
            // 放入缓存中
            if (nightResId == 0) {
                nightResId = context.getResources().getIdentifier(entryName + theme.bsuffix, typeName, context.getPackageName());
            }
            if (nightResId == 0) {
                nightResId = originResId;
            }
            cachedRes.put(entryName + theme.suffix, nightResId);
            return nightResId;
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
        return originResId;
    }

    public void setViewsBackround(ViewGroup viewGroup, int[] ids, int dayResId) {
        for (int id : ids) {
            View view = viewGroup.findViewById(id);
            if (view != null) {
                view.setBackgroundResource(getCurrentThemeRes(dayResId));
            }
        }
    }

    public void setTextViewsColor(ViewGroup viewGroup, int[] ids, int dayResId) {
        for (int id : ids) {
            View view = viewGroup.findViewById(id);
            if (view instanceof TextView) {
                ((TextView) view).setTextColor(getCurrentThemeColor(dayResId));
            }
        }
    }

    /**
     * 注册ThemeChangeListener
     *
     * @param listener
     */
    public void registerThemeChangeListener(OnThemeChangeListener listener) {
        if (!listeners.contains(listener)) {
            LogEx.d(this, "registerThemeChangeListener:" + listener);
            listeners.add(listener);
        }
    }

    /**
     * 反注册ThemeChangeListener
     *
     * @param listener
     */
    public void unregisterThemeChangeListener(OnThemeChangeListener listener) {
        LogEx.d(this, "unregisterThemeChangeListener:" + listener);
        listeners.remove(listener);
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent(MNewLocationEvent event) {
        this.lat = event.getLatitude();
        this.lon = event.getLongitude();
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent(MTimeMinuteEvent event) {
        refreshTheme();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(PConsoleEventLightState event) {
        ThemeManage.ThemeMode model = ThemeManage.ThemeMode.getById(SharedPreUtil.getInteger(SDATA_APP_THEME, ThemeManage.ThemeMode.SHIJIAN.getId()));
        if (model.equals(ThemeManage.ThemeMode.DENGGUANG)) {
            if (event.isOpen()) {
                setTheme(Theme.getById(SharedPreUtil.getInteger(SDATA_APP_THEME_NIGHT, Theme.BLACK.getId())));
            } else {
                setTheme(Theme.getById(SharedPreUtil.getInteger(SDATA_APP_THEME_DAY, Theme.WHITE.getId())));
            }
        }
    }

    /**
     * 得到主题模式
     *
     * @return
     */
    public Theme getTheme() {
        return theme;
    }

    public interface OnThemeChangeListener {
        /**
         * 主题切换时回调
         */
        void onThemeChanged(ThemeManage manage);
    }

    public enum Theme implements PicSelectAdapter.PicModel {
        WHITE("白色主题", 0, "", ""),
        BLACK("黑色主题", 1, "_b", ""),
        CBLACK("纯黑主题", 2, "_cb", ""),
        KBLACK("酷黑主题(自定义背景)", 3, "_kb", "_cb");

        private String name;
        private Integer id;
        private String suffix;
        private String bsuffix;

        Theme(String name, Integer id, String suffix, String bsuffix) {
            this.name = name;
            this.id = id;
            this.suffix = suffix;
            this.bsuffix = bsuffix;
        }


        @Override
        public int getPicRes() {
            switch (this) {
                case WHITE:
                    return R.mipmap.img_app1;
                case BLACK:
                    return R.mipmap.img_app3;
                case CBLACK:
                    return R.mipmap.img_app4;
                case KBLACK:
                    return R.mipmap.img_app5;
                default:
                    return R.mipmap.img_app1;
            }
        }

        public String getName() {
            return name;
        }

        public Theme setName(String name) {
            this.name = name;
            return this;
        }

        public Integer getId() {
            return id;
        }

        public Theme setId(Integer id) {
            this.id = id;
            return this;
        }

        public String getSuffix() {
            return suffix;
        }

        public Theme setSuffix(String suffix) {
            this.suffix = suffix;
            return this;
        }

        public static Theme getById(Integer id) {
            switch (id) {
                case 0:
                    return WHITE;
                case 1:
                    return BLACK;
                case 2:
                    return CBLACK;
                case 3:
                    return KBLACK;
            }
            return WHITE;
        }
    }

    public enum ThemeMode implements PicSelectAdapter.PicModel {
        SHIJIAN("根据日出日落切换", 0),
        DENGGUANG("根据灯光切换(部分车型支持)", 1),
        BAISE("强制白色主题", 2),
        HEISE("强制黑色主题", 3),
        CHUNHEI("强制纯黑主题", 4),
        KUHEI("强制酷黑主题", 5);
        private String name;
        private Integer id;

        ThemeMode(String name, Integer id) {
            this.name = name;
            this.id = id;
        }


        public String getName() {
            return name;
        }

        @Override
        public int getPicRes() {
            switch (this) {
                case SHIJIAN:
                    return R.mipmap.img_app1;
                case DENGGUANG:
                    return R.mipmap.img_app1;
                case BAISE:
                    return R.mipmap.img_app1;
                case HEISE:
                    return R.mipmap.img_app3;
                case CHUNHEI:
                    return R.mipmap.img_app4;
                case KUHEI:
                    return R.mipmap.img_app5;
                default:
                    return R.mipmap.img_app1;
            }
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public static ThemeMode getById(Integer id) {
            switch (id) {
                case 0:
                    return SHIJIAN;
                case 1:
                    return DENGGUANG;
                case 2:
                    return BAISE;
                case 3:
                    return HEISE;
                case 4:
                    return CHUNHEI;
                case 5:
                    return KUHEI;
            }
            return SHIJIAN;
        }
    }
}
