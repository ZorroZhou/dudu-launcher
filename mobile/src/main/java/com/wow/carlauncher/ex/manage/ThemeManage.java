package com.wow.carlauncher.ex.manage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.support.v4.content.ContextCompat;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wow.carlauncher.common.util.SharedPreUtil;
import com.wow.carlauncher.common.util.SunRiseSetUtil;
import com.wow.carlauncher.ex.manage.location.event.MNewLocationEvent;
import com.wow.carlauncher.ex.manage.time.event.MTimeMinuteEvent;
import com.wow.carlauncher.ex.plugin.console.event.PConsoleEventLightState;
import com.wow.carlauncher.view.activity.set.SetEnum;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.x;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.wow.carlauncher.common.CommonData.SDATA_APP_THEME;
import static com.wow.carlauncher.ex.manage.ThemeManage.Theme.BLACK;
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

    public void init(Context context) {
        EventBus.getDefault().register(this);
    }

    private double lat = 36.0577034969, lon = 120.3210639954;//这是青岛的某个坐标
    private Theme theme = WHITE;
    private List<OnThemeChangeListener> mThemeChangeListenerList = new LinkedList<>();
    private SparseArray<Map<String, Map<String, Integer>>> cachedResrouces = new SparseArray<>();

    public void refreshTheme() {
        ThemeMode model = ThemeMode.getById(SharedPreUtil.getSharedPreInteger(SDATA_APP_THEME, ThemeMode.SHIJIAN.getId()));
        switch (model) {
            case BAISE:
                setTheme(WHITE);
                break;
            case HEISE:
                setTheme(BLACK);
                break;
            case SHIJIAN:
                if (SunRiseSetUtil.isNight(lon, lat, new Date())) {
                    setTheme(BLACK);
                } else {
                    setTheme(WHITE);
                }
                break;
        }
    }

    /**
     * 设置主题
     */
    public void setTheme(Theme theme) {
        if (this.theme != theme) {
            this.theme = theme;
            if (mThemeChangeListenerList.size() > 0) {
                x.task().autoPost(() -> {
                    for (OnThemeChangeListener listener : mThemeChangeListenerList) {
                        listener.onThemeChanged(ThemeManage.this);
                    }
                });
            }
        }
    }


    public int getCurrentThemeColor(Context context, int dayResId) {
        return ContextCompat.getColor(context, getCurrentThemeRes(context, dayResId));
    }

    /**
     * 根据传入的日间模式的resId得到相应主题的resId，注意：必须是日间模式的resId
     *
     * @param dayResId 日间模式的resId
     * @return 相应主题的resId，若为日间模式，则得到dayResId；反之夜间模式得到nightResId
     */
    public int getCurrentThemeRes(Context context, int dayResId) {
        if (getTheme() == WHITE) {
            return dayResId;
        }
        Map<String, Map<String, Integer>> cachedResrouce = cachedResrouces.get(getTheme().id);
        if (cachedResrouce == null) {
            cachedResrouce = new HashMap<>();
            cachedResrouces.put(getTheme().id, cachedResrouce);
        }
        // 资源名
        String entryName = context.getResources().getResourceEntryName(dayResId);
        // 资源类型
        String typeName = context.getResources().getResourceTypeName(dayResId);
        Map<String, Integer> cachedRes = cachedResrouce.get(typeName);
        // 先从缓存中去取，如果有直接返回该id
        if (cachedRes == null) {
            cachedRes = new HashMap<>();
            cachedResrouce.put(typeName, cachedRes);
        }
        Integer resId = cachedRes.get(entryName + getTheme().suffix);
        if (resId != null && resId != 0) {
            return resId;
        } else {
            //如果缓存中没有再根据资源id去动态获取
            try {
                // 通过资源名，资源类型，包名得到资源int值
                int nightResId = context.getResources().getIdentifier(entryName + getTheme().suffix, typeName, context.getPackageName());
                // 放入缓存中
                if (nightResId == 0) {
                    return dayResId;
                }
                cachedRes.put(entryName + getTheme().suffix, nightResId);
                return nightResId;
            } catch (Resources.NotFoundException e) {
                e.printStackTrace();
            }
        }
        return dayResId;
    }

    public void setViewsBackround(ViewGroup viewGroup, int[] ids, int dayResId) {
        for (int id : ids) {
            View view = viewGroup.findViewById(id);
            if (view != null) {
                view.setBackgroundResource(getCurrentThemeRes(viewGroup.getContext(), dayResId));
            }
        }
    }

    public void setTextViewsColor(ViewGroup viewGroup, int[] ids, int dayResId) {
        for (int id : ids) {
            View view = viewGroup.findViewById(id);
            if (view instanceof TextView) {
                ((TextView) view).setTextColor(getCurrentThemeColor(viewGroup.getContext(), dayResId));
            }
        }
    }

    /**
     * 注册ThemeChangeListener
     *
     * @param listener
     */
    public void registerThemeChangeListener(OnThemeChangeListener listener) {
        if (!mThemeChangeListenerList.contains(listener)) {
            mThemeChangeListenerList.add(listener);
        }
    }

    /**
     * 反注册ThemeChangeListener
     *
     * @param listener
     */
    public void unregisterThemeChangeListener(OnThemeChangeListener listener) {
        mThemeChangeListenerList.remove(listener);
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
//        ThemeManage.ThemeMode model = ThemeManage.ThemeMode.getById(SharedPreUtil.getSharedPreInteger(SDATA_APP_THEME, ThemeManage.ThemeMode.SHIJIAN.getId()));
//        if (model.equals(ThemeManage.ThemeMode.DENGGUANG)) {
//            if (event.isOpen()) {
//                ThemeManage.self().setTheme(BLACK);
//            } else {
//                ThemeManage.self().setTheme(WHITE);
//            }
//        }
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

    public enum Theme {
        WHITE("白色主题", 0, ""),
        BLACK("黑色主题", 1, "_b"),
        CBLACK("纯黑主题", 2, "_cb");

        private String name;
        private Integer id;
        private String suffix;

        Theme(String name, Integer id, String suffix) {
            this.name = name;
            this.id = id;
            this.suffix = suffix;
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
    }

    public enum ThemeMode implements SetEnum {
        SHIJIAN("根据日出日落切换", 0),
        DENGGUANG("根据灯光切换(部分车型支持)", 1),
        BAISE("强制白色主题", 2),
        HEISE("强制黑色主题", 3);
        private String name;
        private Integer id;

        ThemeMode(String name, Integer id) {
            this.name = name;
            this.id = id;
        }

        public String getName() {
            return name;
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
            }
            return SHIJIAN;
        }
    }
}
