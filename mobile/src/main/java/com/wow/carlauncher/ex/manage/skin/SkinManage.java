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

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import skin.support.SkinCompatManager;
import skin.support.utils.SkinFileUtils;

import static skin.support.SkinCompatManager.SKIN_LOADER_STRATEGY_NONE;

public class SkinManage {

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

    private Context context;

    public void init(Application context) {
        this.context = context;
        SkinCompatManager.withoutActivity(context);
    }

    private String skinName = "";

    public String getSkinName() {
        return skinName;
    }

    private String skinPackageName = "";
    private Resources skinSkinResources = null;
    private String skinPath = "";

    private List<OnSkinChangeListener> listeners = new LinkedList<>();
    private SparseArray<String> cachedIdToName = new SparseArray<>();//ID和ID名称的缓存
    private Map<String, String> cachedString = new HashMap<>();
    private Map<String, Integer> cachedDrawable = new HashMap<>();

    public void restoreDefaultTheme() {
        loadSkin("", null, SKIN_LOADER_STRATEGY_NONE);
    }

    public void loadSkin(String skinName, SkinCompatManager.SkinLoaderListener listener, int strategy) {
        SkinCompatManager.getInstance().loadSkin(skinName, new SkinCompatManager.SkinLoaderListener() {
            @Override
            public void onStart() {
                if (listener != null) {
                    listener.onStart();
                }
            }

            @Override
            public void onSuccess() {
                SkinManage.this.skinName = skinName;
                loadSkin();
                //这里如果换肤成功,则开始本地换肤操作
                if (listeners.size() > 0) {
                    TaskExecutor.self().autoPost(() -> {
                        List<OnSkinChangeListener> temp = new ArrayList<>(listeners.size());
                        temp.addAll(listeners);
                        for (OnSkinChangeListener listener : temp) {
                            listener.onSkinChanged(SkinManage.self());
                        }

                        if (listener != null) {
                            listener.onSuccess();
                        }
                    });
                } else {
                    if (listener != null) {
                        listener.onSuccess();
                    }
                }
            }

            @Override
            public void onFailed(String errMsg) {
                if (listener != null) {
                    listener.onFailed(errMsg);
                }
            }
        }, strategy);
    }

    private void loadSkin() {
        cachedIdToName.clear();
        cachedString.clear();
        if (CommonUtil.isNull(this.skinName)) {
            skinPackageName = "";
            skinPath = "";
            skinSkinResources = context.getResources();
        } else {
            skinPath = new File(SkinFileUtils.getSkinDir(context), this.skinName).getAbsolutePath();
            System.out.println("!!!!skinPath:" + skinPath);
            skinPackageName = SkinCompatManager.getInstance().getSkinPackageName(skinPath);
            skinSkinResources = SkinCompatManager.getInstance().getSkinResources(skinPath);
        }
    }

    private String getResName(int resId) {
        String name = cachedIdToName.get(resId);
        //查询缓存,如果
        if (CommonUtil.isNull(name)) {
            name = context.getResources().getResourceEntryName(resId);
            cachedIdToName.put(resId, name);
        }
        return name;
    }

    public String getString(int resId) {
        if (CommonUtil.isNull(this.skinName)) {
            return context.getResources().getString(resId);
        }

        String name = getResName(resId);
        String value = cachedString.get(name);
        if (CommonUtil.isNull(value)) {
            int id = skinSkinResources.getIdentifier(name, "string", skinPackageName);
            if (id == 0) {
                value = context.getResources().getString(resId);
            } else {
                value = skinSkinResources.getString(id);
            }
            cachedString.put(name, value);
        }
        return value;
    }

    public Drawable getDrawable(int resId) {
        if (CommonUtil.isNull(this.skinName)) {
            return context.getResources().getDrawable(resId);
        }

        String name = getResName(resId);
        Integer id = cachedDrawable.get(name);
        if (CommonUtil.isNull(id)) {
            id = skinSkinResources.getIdentifier(name, "drawable", skinPackageName);
            cachedDrawable.put(name, id);
        }
        if (id == 0) {
            return context.getResources().getDrawable(resId);
        }
        return skinSkinResources.getDrawable(id);
    }

    /**
     * 注册ThemeChangeListener
     *
     * @param listener
     */
    public void registerSkinChangeListener(OnSkinChangeListener listener) {
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
