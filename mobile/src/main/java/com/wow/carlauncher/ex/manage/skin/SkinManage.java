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
import com.wow.carlauncher.repertory.db.entiy.SkinInfo;
import com.wow.carlauncher.repertory.db.manage.DatabaseManage;
import com.wow.carlauncher.repertory.db.manage.DatabaseManage.IF;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import skin.support.SkinCompatManager;
import skin.support.utils.SkinConstants;
import skin.support.utils.SkinFileUtils;

import static com.wow.carlauncher.common.CommonData.SDATA_APP_SKIN_DAY;

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
    public static final String DEFAULT_MARK = "com.wow.carlauncher.theme";
    public static final String DEFAULT_MARK_NIGHT = "com.wow.carlauncher.theme.heise";
    public static final String DEFAULT_MARK_BLACK = "com.wow.carlauncher.theme.chunhei";

    public void init(Application context) {
        long t1 = System.currentTimeMillis();
        this.context = context;
        //这里不能异步加载!!!
        SkinCompatManager.withoutActivity(context).addStrategy(new MySkinLoader());
        copySkinFromAssets(context, "heise.skin");
        copySkinFromAssets(context, "chunhei.skin");
        LogEx.d(this, "copy time:" + (System.currentTimeMillis() - t1));
        //初始化数据库
        if (DatabaseManage.getCount("select * from SkinInfo where mark='" + DEFAULT_MARK + "'") == 0) {
            DatabaseManage.insert(new SkinInfo()
                    .setCanUse(IF.YES)
                    .setMark(DEFAULT_MARK)
                    .setName("默认主题")
                    .setType(SkinInfo.TYPE_APP_IN));
        }
        if (DatabaseManage.getCount("select * from SkinInfo where mark='" + DEFAULT_MARK_NIGHT + "'") == 0) {
            DatabaseManage.insert(new SkinInfo()
                    .setCanUse(IF.YES)
                    .setMark(DEFAULT_MARK_NIGHT)
                    .setPath(SkinFileUtils.getSkinDir(context) + "/heise.skin")
                    .setName("夜间主题")
                    .setType(SkinInfo.TYPE_APP_IN));
        }

        if (DatabaseManage.getCount("select * from SkinInfo where mark='" + DEFAULT_MARK_BLACK + "'") == 0) {
            DatabaseManage.insert(new SkinInfo()
                    .setCanUse(IF.YES)
                    .setMark(DEFAULT_MARK_BLACK)
                    .setPath(SkinFileUtils.getSkinDir(context) + "/chunhei.skin")
                    .setName("纯黑主题")
                    .setType(SkinInfo.TYPE_APP_IN));
        }
        loadSkin(null);
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

    //这里以主题的mark作为唯一标记,不要用路径
    public void loadSkin(SkinCompatManager.SkinLoaderListener listener) {
        SkinInfo skinInfo = DatabaseManage.getBean(SkinInfo.class, " mark='" + SharedPreUtil.getString(SDATA_APP_SKIN_DAY) + "' and canUse=" + IF.YES);
        //存储的主题丢失了,直接使用默认主题
        if (skinInfo == null) {
            skinInfo = new SkinInfo()
                    .setMark(DEFAULT_MARK)
                    .setName("默认主题");
        }

        this.skinMark = skinInfo.getMark();
        //清理缓存
        cachedIdToName.clear();
        cachedString.clear();
        //如果是默认主题,则不加载额外信息
        if (CommonUtil.equals(this.skinMark, DEFAULT_MARK)) {
            skinPackageName = "";
            skinSkinResources = context.getResources();
        } else {
            //如果不是默认主题,加载额外信息,如果加载失败,则加载默认主题
            try {
                skinPackageName = SkinCompatManager.getInstance().getSkinPackageName(skinInfo.getPath());
                skinSkinResources = SkinCompatManager.getInstance().getSkinResources(skinInfo.getPath());
            } catch (Exception e) {
                this.skinMark = DEFAULT_MARK;
                skinPackageName = "";
                skinSkinResources = context.getResources();
            }
        }

        SkinCompatManager.SkinLoaderListener loaderListener = new SkinCompatManager.SkinLoaderListener() {
            @Override
            public void onStart() {
                if (listener != null) {
                    listener.onStart();
                }
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
        };
        //加载主题,使用自己的加载器
        SkinCompatManager.getInstance().loadSkin(this.skinMark, loaderListener, MySkinLoader.STRATEGY);
    }

    //
//    private int getSkinTypeByPath(String path) {
//        LogEx.d(SkinManage.class, "getSkinTypeByPath:Start");
//        try {
//            SkinInfo skinInfo = DatabaseManage.getBean(SkinInfo.class, " path='" + path + "'");
//            if (skinInfo == null) {
//                LogEx.e(SkinManage.class, "skinInfo not in database!  path:" + path);
//                return SkinInfo.TYPE_NONE;
//            }
//            String skinPackageName = SkinCompatManager.getInstance().getSkinPackageName(skinInfo.getPath());
//            if (CommonUtil.isNull(skinPackageName)) {
//                LogEx.e(SkinManage.class, "skinPackageName is null!  path:" + path);
//                return SkinInfo.TYPE_NONE;
//            }
//            Resources skinSkinResources = SkinCompatManager.getInstance().getSkinResources(skinInfo.getPath());
//            if (skinSkinResources == null) {
//                LogEx.e(SkinManage.class, "skinSkinResources is null!  path:" + path);
//                return SkinInfo.TYPE_NONE;
//            }
//            LogEx.d(SkinManage.class, "getSkinTypeByPath:end");
//            return skinInfo.getType();
//        } catch (Exception e) {
//            return SkinInfo.TYPE_NONE;
//        }
//    }
    //将asset的文件复制到缓存,以便使用
    private String copySkinFromAssets(Context context, String name) {
        String skinPath = new File(SkinFileUtils.getSkinDir(context), name).getAbsolutePath();
        try {
            InputStream is = context.getAssets().open(
                    SkinConstants.SKIN_DEPLOY_PATH + File.separator + name);
            OutputStream os = new FileOutputStream(skinPath);
            int byteCount;
            byte[] bytes = new byte[1024];
            while ((byteCount = is.read(bytes)) != -1) {
                os.write(bytes, 0, byteCount);
            }
            os.close();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return skinPath;
    }

    //获取一个资源的名称,这个必须缓存起来,每个皮肤的名称都是一样的
    private String getResName(int resId) {
        String name = cachedIdToName.get(resId);
        //查询缓存,如果
        if (CommonUtil.isNull(name)) {
            name = context.getResources().getResourceEntryName(resId);
            cachedIdToName.put(resId, name);
        }
        return name;
    }

    //获取皮肤内的字符串信息
    public String getString(int resId) {
        LogEx.d(this, "getString mark:" + skinMark);
        if (CommonUtil.equals(skinMark, DEFAULT_MARK)) {
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

    //获取皮肤内图的资源
    public Drawable getDrawable(int resId) {
        LogEx.d(this, "getDrawable mark:" + skinMark);
        if (CommonUtil.equals(skinMark, DEFAULT_MARK)) {
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
