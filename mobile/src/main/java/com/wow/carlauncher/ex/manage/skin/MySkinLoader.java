package com.wow.carlauncher.ex.manage.skin;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;

import com.wow.carlauncher.common.util.CommonUtil;
import com.wow.carlauncher.repertory.db.manage.DatabaseManage;

import java.util.Map;

import skin.support.load.SkinSDCardLoader;

public class MySkinLoader extends SkinSDCardLoader {
    public static final int STRATEGY = Short.MAX_VALUE;

    private boolean isbase = true;

    @Override
    protected String getSkinPath(Context context, String skinMark) {
        if (CommonUtil.equals(skinMark, SkinManage.DEFAULT_MARK)) {
            isbase = true;
            return null;
        } else {
            isbase = false;
        }
        Map map = DatabaseManage.getMap("select path from SkinInfo where mark='" + skinMark + "'");
        if (map == null) {
            isbase = true;
            return null;
        }
        return map.get("path") + "";
    }

    @Override
    public int getType() {
        return STRATEGY;
    }

    @Override
    public String getTargetResourceEntryName(Context context, String skinName, int resId) {
        if (isbase) {
            return "";
        } else {
            return null;
        }
    }

    @Override
    public ColorStateList getColor(Context context, String skinName, int resId) {
        if (isbase) {
            return null;
        } else {
            return super.getColor(context, skinName, resId);
        }
    }

    @Override
    public ColorStateList getColorStateList(Context context, String skinName, int resId) {
        if (isbase) {
            return null;
        } else {
            return super.getColorStateList(context, skinName, resId);
        }
    }

    @Override
    public Drawable getDrawable(Context context, String skinName, int resId) {
        if (isbase) {
            return null;
        } else {
            return super.getDrawable(context, skinName, resId);
        }
    }
}
