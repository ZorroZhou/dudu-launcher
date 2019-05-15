package com.wow.carlauncher.view.activity.set.view;

import android.annotation.SuppressLint;

import com.wow.carlauncher.R;
import com.wow.carlauncher.common.util.SharedPreUtil;
import com.wow.carlauncher.common.view.SetView;
import com.wow.carlauncher.ex.manage.skin.SkinModel;
import com.wow.carlauncher.view.activity.set.SetActivity;
import com.wow.carlauncher.view.activity.set.SetBaseView;
import com.wow.carlauncher.view.activity.set.listener.SetEnumOnClickListener;

import butterknife.BindView;

import static com.wow.carlauncher.common.CommonData.SDATA_APP_SKIN;
import static com.wow.carlauncher.common.CommonData.SKIN_MODEL;

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
//        sv_theme_night.setSummary(Theme.getById(SharedPreUtil.getInteger(SDATA_APP_SKIN_NIGHT, Theme.BLACK.getId())).getName());
//        sv_theme_night.setOnClickListener(new PicSelectOnClickListener<Theme>(getContext(), THEMES) {
//            @Override
//            public String title() {
//                return "请选择夜间主题";
//            }
//
//            @Override
//            public Theme getCurr() {
//                return Theme.getById(SharedPreUtil.getInteger(SDATA_APP_SKIN_NIGHT, Theme.BLACK.getId()));
//            }
//
//            @Override
//            public void onSelect(Theme setEnum) {
//                SharedPreUtil.saveInteger(SDATA_APP_SKIN_NIGHT, setEnum.getId());
//                sv_theme_night.setSummary(setEnum.getName());
//            }
//        });
//
//        sv_theme_day.setSummary(Theme.getById(SharedPreUtil.getInteger(SDATA_APP_SKIN_DAY, Theme.WHITE.getId())).getName());
//        sv_theme_day.setOnClickListener(new PicSelectOnClickListener<Theme>(getContext(), THEMES) {
//            @Override
//            public String title() {
//                return "请选择白天主题";
//            }
//
//            @Override
//            public Theme getCurr() {
//                return Theme.getById(SharedPreUtil.getInteger(SDATA_APP_SKIN_DAY, Theme.WHITE.getId()));
//            }
//
//            @Override
//            public void onSelect(Theme setEnum) {
//                SharedPreUtil.saveInteger(SDATA_APP_SKIN_DAY, setEnum.getId());
//                sv_theme_day.setSummary(setEnum.getName());
//            }
//        });
//

        sv_plugin_theme.setSummary(SkinModel.getById(SharedPreUtil.getInteger(SDATA_APP_SKIN, SkinModel.BAISE.getId())).getName());
        sv_plugin_theme.setOnClickListener(new SetEnumOnClickListener<SkinModel>(getContext(), SKIN_MODEL) {
            @Override
            public String title() {
                return "请选择皮肤模式";
            }

            @Override
            public SkinModel getCurr() {
                return SkinModel.getById(SharedPreUtil.getInteger(SDATA_APP_SKIN, SkinModel.BAISE.getId()));
            }

            @Override
            public void onSelect(SkinModel setEnum) {
                SharedPreUtil.saveInteger(SDATA_APP_SKIN, setEnum.getId());
                sv_plugin_theme.setSummary(setEnum.getName());
            }
        });
    }
}
