package com.wow.carlauncher.view.activity.set.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.wow.carlauncher.R;
import com.wow.carlauncher.common.util.SharedPreUtil;
import com.wow.carlauncher.common.view.SetView;
import com.wow.carlauncher.ex.manage.ThemeManage;
import com.wow.carlauncher.ex.manage.ThemeManage.Theme;
import com.wow.carlauncher.ex.manage.ThemeManage.ThemeMode;
import com.wow.carlauncher.ex.plugin.console.ConsoleProtoclEnum;
import com.wow.carlauncher.view.base.BaseEXView;
import com.wow.carlauncher.view.clickListener.PicSelectOnClickListener;

import butterknife.BindView;

import static com.wow.carlauncher.common.CommonData.SDATA_APP_THEME;
import static com.wow.carlauncher.common.CommonData.SDATA_APP_THEME_DAY;
import static com.wow.carlauncher.common.CommonData.SDATA_APP_THEME_NIGHT;
import static com.wow.carlauncher.common.CommonData.THEMES;
import static com.wow.carlauncher.common.CommonData.THEME_MODEL;

/**
 * Created by 10124 on 2018/4/22.
 */

public class SThemeView extends BaseEXView {

    private static final ConsoleProtoclEnum[] ALL_CONSOLES = {ConsoleProtoclEnum.SYSTEM, ConsoleProtoclEnum.NWD};

    public SThemeView(@NonNull Context context) {
        super(context);
    }

    public SThemeView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected int getContent() {
        return R.layout.content_set_theme;
    }

    @BindView(R.id.sv_plugin_theme)
    SetView sv_plugin_theme;

    @BindView(R.id.sv_theme_day)
    SetView sv_theme_day;

    @BindView(R.id.sv_theme_night)
    SetView sv_theme_night;

    @Override
    protected void initView() {
        sv_theme_night.setSummary(Theme.getById(SharedPreUtil.getInteger(SDATA_APP_THEME_NIGHT, Theme.BLACK.getId())).getName());
        sv_theme_night.setOnClickListener(new PicSelectOnClickListener<Theme>(getContext(), THEMES) {
            @Override
            public String title() {
                return "请选择夜间主题";
            }

            @Override
            public Theme getCurr() {
                return Theme.getById(SharedPreUtil.getInteger(SDATA_APP_THEME_NIGHT, Theme.BLACK.getId()));
            }

            @Override
            public void onSelect(Theme setEnum) {
                SharedPreUtil.saveInteger(SDATA_APP_THEME_NIGHT, setEnum.getId());
                sv_theme_night.setSummary(setEnum.getName());
                ThemeManage.self().refreshTheme();
            }
        });

        sv_theme_day.setSummary(Theme.getById(SharedPreUtil.getInteger(SDATA_APP_THEME_DAY, Theme.WHITE.getId())).getName());
        sv_theme_day.setOnClickListener(new PicSelectOnClickListener<Theme>(getContext(), THEMES) {
            @Override
            public String title() {
                return "请选择白天主题";
            }

            @Override
            public Theme getCurr() {
                return Theme.getById(SharedPreUtil.getInteger(SDATA_APP_THEME_DAY, Theme.WHITE.getId()));
            }

            @Override
            public void onSelect(Theme setEnum) {
                SharedPreUtil.saveInteger(SDATA_APP_THEME_DAY, setEnum.getId());
                sv_theme_day.setSummary(setEnum.getName());
                ThemeManage.self().refreshTheme();
            }
        });


        sv_plugin_theme.setSummary(ThemeMode.getById(SharedPreUtil.getInteger(SDATA_APP_THEME, ThemeMode.SHIJIAN.getId())).getName());
        sv_plugin_theme.setOnClickListener(new PicSelectOnClickListener<ThemeMode>(getContext(), THEME_MODEL) {
            @Override
            public String title() {
                return "请选择主题模式";
            }

            @Override
            public ThemeMode getCurr() {
                return ThemeMode.getById(SharedPreUtil.getInteger(SDATA_APP_THEME, ThemeMode.SHIJIAN.getId()));
            }

            @Override
            public void onSelect(ThemeMode setEnum) {
                SharedPreUtil.saveInteger(SDATA_APP_THEME, setEnum.getId());
                sv_plugin_theme.setSummary(setEnum.getName());
                ThemeManage.self().refreshTheme();
            }
        });
    }
}
