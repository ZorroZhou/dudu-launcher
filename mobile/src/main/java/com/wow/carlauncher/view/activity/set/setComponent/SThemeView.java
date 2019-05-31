package com.wow.carlauncher.view.activity.set.setComponent;

import android.annotation.SuppressLint;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;

import com.wow.carlauncher.R;
import com.wow.carlauncher.common.TaskExecutor;
import com.wow.carlauncher.common.util.CommonUtil;
import com.wow.carlauncher.common.util.SharedPreUtil;
import com.wow.carlauncher.common.view.SetView;
import com.wow.carlauncher.ex.manage.skin.SkinManage;
import com.wow.carlauncher.ex.manage.skin.SkinModel;
import com.wow.carlauncher.repertory.db.entiy.SkinInfo;
import com.wow.carlauncher.repertory.db.manage.DatabaseManage;
import com.wow.carlauncher.view.activity.set.SetActivity;
import com.wow.carlauncher.view.activity.set.SetBaseView;
import com.wow.carlauncher.view.activity.set.commonView.SetOnlineSkinSelectView;
import com.wow.carlauncher.view.activity.set.commonView.SetSingleSelectView;
import com.wow.carlauncher.view.event.EventSkinInstall;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

import static com.wow.carlauncher.common.CommonData.SDATA_APP_SKIN;
import static com.wow.carlauncher.common.CommonData.SDATA_APP_SKIN_DAY;
import static com.wow.carlauncher.common.CommonData.SDATA_APP_SKIN_NIGHT;
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

    @Override
    public String getName() {
        return "主题设置";
    }

    @BindView(R.id.sv_plugin_theme)
    SetView sv_plugin_theme;

    @BindView(R.id.sv_theme_day)
    SetView sv_theme_day;

    @BindView(R.id.sv_theme_night)
    SetView sv_theme_night;

    @BindView(R.id.sv_load_skin)
    SetView sv_load_skin;

    @BindView(R.id.sv_skin_online)
    SetView sv_skin_online;

    private Map<String, SkinInfo> allSkinInfos;
    private Map<String, SkinInfo> allOtherSkinInfos;

    @Override
    protected void initView() {
        allSkinInfos = new HashMap<>();
        allOtherSkinInfos = new HashMap<>();

        sv_theme_day.setOnClickListener(new SetSingleSelectView<SkinInfo>(getActivity(), "请选择默认皮肤") {
            @Override
            public Collection<SkinInfo> getAll() {
                return allSkinInfos.values();
            }

            @Override
            public SkinInfo getCurr() {
                return allSkinInfos.get(SharedPreUtil.getString(SDATA_APP_SKIN_DAY));
            }

            @Override
            public boolean equals(SkinInfo t1, SkinInfo t2) {
                return t1 != null && t2 != null && CommonUtil.equals(t1.getMark(), t2.getMark());
            }

            @Override
            public boolean onSelect(SkinInfo select) {
                SharedPreUtil.saveString(SDATA_APP_SKIN_DAY, select.getMark());
                sv_theme_day.setSummary(select.getName());
                SkinManage.self().loadSkin();
                return true;
            }
        });
        sv_skin_online.setOnClickListener(new SetOnlineSkinSelectView(getActivity()));

        sv_theme_night.setOnClickListener(new SetSingleSelectView<SkinInfo>(getActivity(), "请选择夜间皮肤") {

            @Override
            public Collection<SkinInfo> getAll() {
                return allSkinInfos.values();
            }

            @Override
            public SkinInfo getCurr() {
                return allSkinInfos.get(SharedPreUtil.getString(SDATA_APP_SKIN_NIGHT));
            }

            @Override
            public boolean equals(SkinInfo t1, SkinInfo t2) {
                return t1 != null && t2 != null && CommonUtil.equals(t1.getMark(), t2.getMark());
            }

            @Override
            public boolean onSelect(SkinInfo select) {
                SharedPreUtil.saveString(SDATA_APP_SKIN_NIGHT, select.getMark());
                sv_theme_night.setSummary(select.getName());
                SkinManage.self().loadSkin();
                return true;
            }
        });


        sv_plugin_theme.setSummary(SkinModel.getById(SharedPreUtil.getInteger(SDATA_APP_SKIN, SkinModel.BAISE.getId())).getName());
        sv_plugin_theme.setOnClickListener(new SetSingleSelectView<SkinModel>(getActivity(), "请选择皮肤模式") {
            @Override
            public Collection<SkinModel> getAll() {
                return java.util.Arrays.asList(SKIN_MODEL);
            }

            @Override
            public SkinModel getCurr() {
                return SkinModel.getById(SharedPreUtil.getInteger(SDATA_APP_SKIN, SkinModel.BAISE.getId()));
            }

            @Override
            public boolean onSelect(SkinModel setEnum) {
                SharedPreUtil.saveInteger(SDATA_APP_SKIN, setEnum.getId());
                sv_plugin_theme.setSummary(setEnum.getName());
                return true;
            }
        });

        sv_load_skin.setOnClickListener(v -> {
            getActivity().showLoading("处理中");
            TaskExecutor.self().run(() -> {
                PackageManager pManager = getContext().getPackageManager();
                //获取手机内所有应用
                List<PackageInfo> paklist = pManager.getInstalledPackages(0);
                String sqlwhere = "";
                for (PackageInfo packageInfo : paklist) {
                    if (packageInfo.packageName.startsWith("com.wow.carlauncher.theme")) {
                        sqlwhere = sqlwhere + "'" + packageInfo.packageName + "',";
                        try {
                            String nameRes = getContext().getResources().getResourceEntryName(R.string.theme_name);
                            Resources resources = getContext().createPackageContext(packageInfo.packageName, 0).getResources();
                            int id = resources.getIdentifier(nameRes, "string", packageInfo.packageName);
                            String name = resources.getString(id);

                            SkinInfo skinInfo = DatabaseManage.getBean(SkinInfo.class, " mark='" + packageInfo.packageName + "'");

                            if (skinInfo == null) {
                                DatabaseManage.insert(new SkinInfo()
                                        .setMark(packageInfo.packageName)
                                        .setVersion(packageInfo.versionCode)
                                        .setName(name));
                            } else {
                                DatabaseManage.update(new SkinInfo()
                                        .setMark(packageInfo.packageName)
                                        .setVersion(packageInfo.versionCode)
                                        .setName(name), " mark='" + packageInfo.packageName + "'");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                if (CommonUtil.isNotNull(sqlwhere)) {
                    sqlwhere = sqlwhere.substring(0, sqlwhere.length() - 1);
                    DatabaseManage.delete(SkinInfo.class, "mark not in (" + sqlwhere + ")");
                }
                loadData();
                getActivity().hideLoading();
            });
        });

        TaskExecutor.self().run(this::loadData);
    }

    private void loadData() {
        allSkinInfos.clear();
        allOtherSkinInfos.clear();
        List<SkinInfo> temp = DatabaseManage.getAll(SkinInfo.class);
        for (SkinInfo skinInfo : temp) {
            if (CommonUtil.isNotNull(skinInfo.getMark())) {
                allOtherSkinInfos.put(skinInfo.getMark(), skinInfo);
            }
        }
        allSkinInfos.putAll(allOtherSkinInfos);
        allSkinInfos.put(SkinManage.self().getDefaultSkin().getMark(), SkinManage.self().getDefaultSkin());

        //拿到mark,先查询所有的,如果没有,标记为默认的
        String dayMark = SharedPreUtil.getString(SDATA_APP_SKIN_DAY);
        SkinInfo daySkinInfo = allSkinInfos.get(dayMark);
        if (daySkinInfo == null) {
            SharedPreUtil.saveString(SDATA_APP_SKIN_DAY, SkinManage.DEFAULT_MARK);
            daySkinInfo = SkinManage.self().getDefaultSkin();
            SkinManage.self().loadSkin();
        }


        final String nightMark = SharedPreUtil.getString(SDATA_APP_SKIN_NIGHT);
        SkinInfo nightSkinInfo = allSkinInfos.get(nightMark);
        if (nightSkinInfo == null) {
            SharedPreUtil.saveString(SDATA_APP_SKIN_NIGHT, SkinManage.DEFAULT_MARK);
            nightSkinInfo = SkinManage.self().getDefaultSkin();
            SkinManage.self().loadSkin();
        }

        final String daySkinInfoName = daySkinInfo.getName();
        final String nightSkinInfoName = nightSkinInfo.getName();
        TaskExecutor.self().autoPost(() -> {
            sv_theme_day.setSummary(daySkinInfoName);
            sv_theme_night.setSummary(nightSkinInfoName);
        });
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent(EventSkinInstall event) {
        loadData();
    }
}
