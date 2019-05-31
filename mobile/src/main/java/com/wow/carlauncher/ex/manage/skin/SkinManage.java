package com.wow.carlauncher.ex.manage.skin;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.SparseArray;

import com.wow.carlauncher.common.LogEx;
import com.wow.carlauncher.common.TaskExecutor;
import com.wow.carlauncher.common.util.CommonUtil;
import com.wow.carlauncher.common.util.SharedPreUtil;
import com.wow.carlauncher.common.util.SunRiseSetUtil;
import com.wow.carlauncher.ex.ContextEx;
import com.wow.carlauncher.ex.manage.location.LMEventNewLocation;
import com.wow.carlauncher.ex.manage.time.event.TMEvent5Minute;
import com.wow.carlauncher.ex.plugin.console.event.PConsoleEventLightState;
import com.wow.carlauncher.repertory.db.entiy.SkinInfo;
import com.wow.carlauncher.repertory.db.manage.DatabaseManage;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import skin.support.SkinCompatManager;

import static com.wow.carlauncher.common.CommonData.HOUR_MILL;
import static com.wow.carlauncher.common.CommonData.SDATA_APP_SKIN;
import static com.wow.carlauncher.common.CommonData.SDATA_APP_SKIN_DAY;
import static com.wow.carlauncher.common.CommonData.SDATA_APP_SKIN_NIGHT;

public class SkinManage extends ContextEx {

    private static class SingletonHolder {
        @SuppressLint("StaticFieldLeak")
        private static SkinManage instance = new SkinManage();
    }

    public static SkinManage self() {
        return SkinManage.SingletonHolder.instance;
    }

    private SkinManage() {
        super();
    }

    public static final String DEFAULT_MARK = "com.wow.carlauncher.theme";

    private SkinInfo defaultSkin;

    public SkinInfo getDefaultSkin() {
        return defaultSkin;
    }

    public void init(Application context) {
        long t1 = System.currentTimeMillis();
        setContext(context);
        SkinCompatManager.withoutActivity(context).addStrategy(new MySkinLoader());

        defaultSkin = new SkinInfo()
                .setMark(DEFAULT_MARK)
                .setName("默认主题");

        //初始化皮肤
        loadSkin();

        EventBus.getDefault().register(this);
        LogEx.d(this, "init time:" + (System.currentTimeMillis() - t1));
    }

    private String skinMark = "";

    public String getSkinMark() {
        return skinMark;
    }

    private String skinPackageName = "";
    private Resources skinSkinResources = null;

    private List<OnSkinChangeListener> listeners = new LinkedList<>();
    private SparseArray<String> cachedIdToName = new SparseArray<>();//ID和ID名称的缓存
    private Map<String, String> cachedString = new HashMap<>();
    private Map<String, Integer> cachedDrawable = new HashMap<>();
    private double lat = 36.0577034969, lon = 120.3210639954;//这是青岛的某个坐标
    private long lastChangeShijian = 0;
    private boolean currentDay = true;//0是未初始化,1是白天,2是黑天

    public void loadSkin() {
        SkinModel skinModel = SkinModel.getById(SharedPreUtil.getInteger(SDATA_APP_SKIN, SkinModel.BAISE.getId()));
        switch (skinModel) {
            case BAISE: {
                SkinInfo skinInfo = getSkininfoByMark(SharedPreUtil.getString(SDATA_APP_SKIN_DAY));
                if (skinInfo == null) {
                    SharedPreUtil.saveString(SDATA_APP_SKIN_DAY, DEFAULT_MARK);
                    skinInfo = defaultSkin;
                }
                loadSkin(skinInfo);
                break;
            }
            case SHIJIAN:
                if (SunRiseSetUtil.isNight(lon, lat, new Date())) {
                    if (!currentDay) {
                        if (System.currentTimeMillis() - lastChangeShijian < HOUR_MILL) {
                            return;
                        }
                        lastChangeShijian = System.currentTimeMillis();
                        currentDay = true;
                    }
                    SkinInfo skinInfo = getSkininfoByMark(SharedPreUtil.getString(SDATA_APP_SKIN_NIGHT));
                    if (skinInfo == null) {
                        SharedPreUtil.saveString(SDATA_APP_SKIN_NIGHT, DEFAULT_MARK);
                        skinInfo = defaultSkin;
                    }
                    loadSkin(skinInfo);
                } else {
                    if (currentDay) {
                        if (System.currentTimeMillis() - lastChangeShijian < HOUR_MILL) {
                            return;
                        }
                        lastChangeShijian = System.currentTimeMillis();
                        currentDay = false;
                    }

                    SkinInfo skinInfo = getSkininfoByMark(SharedPreUtil.getString(SDATA_APP_SKIN_DAY));
                    if (skinInfo == null) {
                        SharedPreUtil.saveString(SDATA_APP_SKIN_DAY, DEFAULT_MARK);
                        skinInfo = defaultSkin;
                    }
                    loadSkin(skinInfo);
                }
                break;
            case DENGGUANG:
                if (lightState) {
                    SkinInfo skinInfo = getSkininfoByMark(SharedPreUtil.getString(SDATA_APP_SKIN_NIGHT));
                    if (skinInfo == null) {
                        SharedPreUtil.saveString(SDATA_APP_SKIN_NIGHT, DEFAULT_MARK);
                        skinInfo = defaultSkin;
                    }
                    loadSkin(skinInfo);
                } else {
                    SkinInfo skinInfo = getSkininfoByMark(SharedPreUtil.getString(SDATA_APP_SKIN_DAY));
                    if (skinInfo == null) {
                        SharedPreUtil.saveString(SDATA_APP_SKIN_DAY, DEFAULT_MARK);
                        skinInfo = defaultSkin;
                    }
                    loadSkin(skinInfo);
                }
                break;
        }
    }

    //这里以主题的mark作为唯一标记,不要用路径
    private void loadSkin(SkinInfo skinInfo) {
        if (CommonUtil.equals(this.skinMark, skinInfo.getMark())) {
            LogEx.e(this, "一样的皮肤,不需要更换");
            return;
        }

        this.skinMark = skinInfo.getMark();
        //清理缓存
        cachedIdToName.clear();
        cachedString.clear();
        //如果是默认主题,则不加载额外信息
        if (CommonUtil.equals(this.skinMark, DEFAULT_MARK)) {
            skinPackageName = "";
            skinSkinResources = getContext().getResources();
        } else {
            //如果不是默认主题,加载额外信息,如果加载失败,则加载默认主题
            try {
                skinPackageName = this.skinMark;
                skinSkinResources = getContext().createPackageContext(skinPackageName, 0).getResources();
                if (skinSkinResources == null) {
                    skinPackageName = "";
                    skinSkinResources = getContext().getResources();
                }
            } catch (Exception e) {
                e.printStackTrace();
                skinPackageName = "";
                skinSkinResources = getContext().getResources();
            }
        }

        SkinCompatManager.SkinLoaderListener loaderListener = new SkinCompatManager.SkinLoaderListener() {
            @Override
            public void onStart() {
            }

            @Override
            public void onSuccess() {
                //主题管理器加载完毕后,自己再处理一遍,为了保证某些主题管理器处理不了的组件自己可以二次处理
                //这里如果换肤成功,则开始本地换肤操作
                if (listeners.size() > 0) {
                    TaskExecutor.self().autoPost(() -> {
                        List<OnSkinChangeListener> temp = new ArrayList<>(listeners.size());
                        temp.addAll(listeners);
                        for (OnSkinChangeListener listener1 : temp) {
                            listener1.onSkinChanged(SkinManage.self());
                        }
                    });
                }
            }

            @Override
            public void onFailed(String errMsg) {
            }
        };
        //加载主题,使用自己的加载器
        SkinCompatManager.getInstance().loadSkin(this.skinMark, loaderListener, MySkinLoader.STRATEGY);
    }

    private boolean lightState = false;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(PConsoleEventLightState event) {
        lightState = event.isOpen();
        loadSkin();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(TMEvent5Minute event) {
        loadSkin();
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent(LMEventNewLocation event) {
        if (event.getLatitude() > 0 && event.getLongitude() > 0) {
            this.lat = event.getLatitude();
            this.lon = event.getLongitude();
        }
    }

    //根据mark获取主题实体bean,这里单独做了一个,为了可以查询
    private SkinInfo getSkininfoByMark(String mark) {
        SkinInfo skinInfo = null;
        if (CommonUtil.equals(mark, DEFAULT_MARK)) {
            skinInfo = defaultSkin;
        }
        if (skinInfo == null) {
            skinInfo = DatabaseManage.getBean(SkinInfo.class, " mark='" + mark + "'");
        }
        return skinInfo;
    }

    //获取一个资源的名称,这个必须缓存起来,每个皮肤的名称都是一样的
    private String getResName(int resId) {
        String name = cachedIdToName.get(resId);
        //查询缓存,如果
        if (CommonUtil.isNull(name)) {
            name = getContext().getResources().getResourceEntryName(resId);
            cachedIdToName.put(resId, name);
        }
        return name;
    }

    //获取皮肤内的字符串信息
    public String getString(int resId) {
        LogEx.d(this, "getString mark:" + skinMark);
        if (CommonUtil.equals(skinMark, DEFAULT_MARK) || skinSkinResources == null) {
            return getContext().getResources().getString(resId);
        }
        String name = getResName(resId);
        String value = cachedString.get(name);
        if (CommonUtil.isNull(value)) {
            int id = skinSkinResources.getIdentifier(name, "string", skinPackageName);
            if (id == 0) {
                value = getContext().getResources().getString(resId);
            } else {
                value = skinSkinResources.getString(id);
            }
            cachedString.put(name, value);
        }
        return value;
    }

    //获取皮肤内图的资源
    public Drawable getDrawable(int resId) {
        LogEx.d(this, "getDrawable mark:" + skinMark);
        if (CommonUtil.equals(skinMark, DEFAULT_MARK) || skinSkinResources == null) {
            return getContext().getResources().getDrawable(resId);
        }

        String name = getResName(resId);
        Integer id = cachedDrawable.get(name);
        if (CommonUtil.isNull(id)) {
            id = skinSkinResources.getIdentifier(name, "drawable", skinPackageName);
            cachedDrawable.put(name, id);
        }
        if (id == 0) {
            return getContext().getResources().getDrawable(resId);
        }
        return skinSkinResources.getDrawable(id);
    }

    /**
     * 注册ChangeListener
     */
    public void registerSkinChangeListener(OnSkinChangeListener listener) {
        if (!listeners.contains(listener)) {
            LogEx.d(this, "registerThemeChangeListener:" + listener);
            listeners.add(listener);
        }
    }

    /**
     * 反注册ChangeListener
     */
    public void unregisterSkinChangeListener(OnSkinChangeListener listener) {
        LogEx.d(this, "unregisterThemeChangeListener:" + listener);
        listeners.remove(listener);
    }

    public interface OnSkinChangeListener {
        /**
         * 主题切换时回调
         */
        void onSkinChanged(SkinManage manage);
    }
}
