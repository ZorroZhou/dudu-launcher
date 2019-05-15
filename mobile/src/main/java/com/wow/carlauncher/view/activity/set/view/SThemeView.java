package com.wow.carlauncher.view.activity.set.view;

import android.annotation.SuppressLint;

import com.wow.carlauncher.R;
import com.wow.carlauncher.common.view.SetView;
import com.wow.carlauncher.view.activity.set.SetActivity;
import com.wow.carlauncher.view.activity.set.SetBaseView;

import butterknife.BindView;

/**
 * Created by 10124 on 2018/4/22.
 */

@SuppressLint("ViewConstructor")
public class SThemeView extends SetBaseView {

    public SThemeView(SetActivity activity) {
        super(activity);
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
//        sv_theme_night.setSummary(Theme.getById(SharedPreUtil.getInteger(SDATA_APP_THEME_NIGHT, Theme.BLACK.getId())).getName());
//        sv_theme_night.setOnClickListener(new PicSelectOnClickListener<Theme>(getContext(), THEMES) {
//            @Override
//            public String title() {
//                return "请选择夜间主题";
//            }
//
//            @Override
//            public Theme getCurr() {
//                return Theme.getById(SharedPreUtil.getInteger(SDATA_APP_THEME_NIGHT, Theme.BLACK.getId()));
//            }
//
//            @Override
//            public void onSelect(Theme setEnum) {
//                SharedPreUtil.saveInteger(SDATA_APP_THEME_NIGHT, setEnum.getId());
//                sv_theme_night.setSummary(setEnum.getName());
//            }
//        });
//
//        sv_theme_day.setSummary(Theme.getById(SharedPreUtil.getInteger(SDATA_APP_THEME_DAY, Theme.WHITE.getId())).getName());
//        sv_theme_day.setOnClickListener(new PicSelectOnClickListener<Theme>(getContext(), THEMES) {
//            @Override
//            public String title() {
//                return "请选择白天主题";
//            }
//
//            @Override
//            public Theme getCurr() {
//                return Theme.getById(SharedPreUtil.getInteger(SDATA_APP_THEME_DAY, Theme.WHITE.getId()));
//            }
//
//            @Override
//            public void onSelect(Theme setEnum) {
//                SharedPreUtil.saveInteger(SDATA_APP_THEME_DAY, setEnum.getId());
//                sv_theme_day.setSummary(setEnum.getName());
//            }
//        });
//
//
//        sv_plugin_theme.setSummary(ThemeMode.getById(SharedPreUtil.getInteger(SDATA_APP_THEME, ThemeMode.SHIJIAN.getId())).getName());
//        sv_plugin_theme.setOnClickListener(new PicSelectOnClickListener<ThemeMode>(getContext(), THEME_MODEL) {
//            @Override
//            public String title() {
//                return "请选择主题模式";
//            }
//
//            @Override
//            public ThemeMode getCurr() {
//                return ThemeMode.getById(SharedPreUtil.getInteger(SDATA_APP_THEME, ThemeMode.SHIJIAN.getId()));
//            }
//
//            @Override
//            public void onSelect(ThemeMode setEnum) {
//                SharedPreUtil.saveInteger(SDATA_APP_THEME, setEnum.getId());
//                sv_plugin_theme.setSummary(setEnum.getName());
//            }
//        });
    }
}
