package com.wow.carlauncher.ex.manage.skin;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;

import com.wow.carlauncher.common.util.CommonUtil;
import com.wow.carlauncher.repertory.db.entiy.SkinInfo;

import skin.support.SkinCompatManager;
import skin.support.content.res.SkinCompatResources;
import skin.support.utils.SkinFileUtils;

import static android.content.Context.CONTEXT_IGNORE_SECURITY;

public class MySkinLoader implements SkinCompatManager.SkinLoaderStrategy {
    public static final int STRATEGY = Short.MAX_VALUE;

    @Override
    public String loadSkinInBackground(Context context, String skinName) {
        System.out.println("!!!!!" + skinName);
        if (CommonUtil.equals(skinName, SkinManage.DEFAULT_MARK)) {
            return null;
        }
        SkinInfo skinInfo = SkinManage.self().getBuiltInSkin().get(skinName);
        //说明是内置皮肤,使用内置皮肤的装载方式
        if (skinInfo != null && SkinFileUtils.isFileExists(skinInfo.getPath())) {
            String pkgName = SkinCompatManager.getInstance().getSkinPackageName(skinInfo.getPath());
            Resources resources = SkinCompatManager.getInstance().getSkinResources(skinInfo.getPath());
            if (resources != null && !TextUtils.isEmpty(pkgName)) {
                SkinCompatResources.getInstance().setupSkin(
                        resources,
                        pkgName,
                        skinName,
                        this);
                return skinName;
            }
        } else {
            //外置皮肤装载
            try {
                Context con = context.createPackageContext(skinName, CONTEXT_IGNORE_SECURITY);
                Resources skinResources = con.getResources();
                SkinCompatResources.getInstance().setupSkin(
                        skinResources,
                        skinName,
                        skinName,
                        this);
                return skinName;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public ColorStateList getColor(Context context, String skinName, int resId) {
        return null;
    }

    @Override
    public ColorStateList getColorStateList(Context context, String skinName, int resId) {
        return null;
    }

    @Override
    public Drawable getDrawable(Context context, String skinName, int resId) {
        return null;
    }


    @Override
    public int getType() {
        return STRATEGY;
    }

    @Override
    public String getTargetResourceEntryName(Context context, String skinName, int resId) {
        return null;
    }
}
