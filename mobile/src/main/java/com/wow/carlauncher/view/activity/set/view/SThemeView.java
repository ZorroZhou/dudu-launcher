package com.wow.carlauncher.view.activity.set.view;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.os.Environment;
import android.support.v7.app.AlertDialog;

import com.wow.carlauncher.R;
import com.wow.carlauncher.common.LogEx;
import com.wow.carlauncher.common.TaskExecutor;
import com.wow.carlauncher.common.util.CommonUtil;
import com.wow.carlauncher.common.util.SharedPreUtil;
import com.wow.carlauncher.common.view.SetView;
import com.wow.carlauncher.ex.manage.skin.SkinManage;
import com.wow.carlauncher.ex.manage.skin.SkinModel;
import com.wow.carlauncher.ex.manage.toast.ToastManage;
import com.wow.carlauncher.repertory.db.entiy.SkinInfo;
import com.wow.carlauncher.repertory.db.manage.DatabaseManage;
import com.wow.carlauncher.repertory.db.manage.DatabaseManage.IF;
import com.wow.carlauncher.view.activity.set.SetActivity;
import com.wow.carlauncher.view.activity.set.SetBaseView;
import com.wow.carlauncher.view.activity.set.listener.SetMultipleSelect;
import com.wow.carlauncher.view.activity.set.listener.SetSingleSelect;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import skin.support.SkinCompatManager;
import skin.support.utils.SkinFileUtils;

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

    @BindView(R.id.sv_plugin_theme)
    SetView sv_plugin_theme;

    @BindView(R.id.sv_theme_day)
    SetView sv_theme_day;

    @BindView(R.id.sv_theme_night)
    SetView sv_theme_night;

    @BindView(R.id.sv_load_skin)
    SetView sv_load_skin;

    @BindView(R.id.sv_delete_skin)
    SetView sv_delete_skin;


    private Map<String, SkinInfo> allSkinInfos;
    private Map<String, SkinInfo> allOtherSkinInfos;

    @Override
    protected void initView() {
        allSkinInfos = new HashMap<>();
        allOtherSkinInfos = new HashMap<>();
        sv_delete_skin.setOnClickListener(new SetMultipleSelect<SkinInfo>(getContext()) {
            @Override
            public Collection<SkinInfo> getAll() {
                return allOtherSkinInfos.values();
            }

            @Override
            public SkinInfo[] getCurr() {
                return new SkinInfo[0];
            }

            @Override
            public boolean equals(SkinInfo t1, SkinInfo t2) {
                return t1 != null && t2 != null && CommonUtil.equals(t1.getMark(), t2.getMark());
            }

            @Override
            public void onSelect(List<SkinInfo> temp) {
                getActivity().showLoading("处理中");
                TaskExecutor.self().run(() -> {
                    for (SkinInfo skinInfo : temp) {
                        DatabaseManage.delete(SkinInfo.class, " mark='" + skinInfo.getMark() + "'");
                        if (!new File(skinInfo.getPath()).delete()) {
                            LogEx.e(SThemeView.this, "主题文件删除失败!");
                        }
                    }
                    loadData();
                    TaskExecutor.self().autoPost(() -> getActivity().hideLoading());
                });
            }

            @Override
            public String title() {
                return "请选择要删除的皮肤";
            }
        });

        sv_theme_day.setOnClickListener(new SetSingleSelect<SkinInfo>(getContext()) {
            @Override
            public Collection<SkinInfo> getAll() {
                return allSkinInfos.values();
            }

            @Override
            public String title() {
                return "请选择默认皮肤";
            }

            @Override
            public SkinInfo getCurr() {
                System.out.println(SharedPreUtil.getString(SDATA_APP_SKIN_DAY));
                return allSkinInfos.get(SharedPreUtil.getString(SDATA_APP_SKIN_DAY));
            }

            @Override
            public boolean equals(SkinInfo t1, SkinInfo t2) {
                return t1 != null && t2 != null && CommonUtil.equals(t1.getMark(), t2.getMark());
            }

            @Override
            public void onSelect(SkinInfo select) {
                SharedPreUtil.saveString(SDATA_APP_SKIN_DAY, select.getMark());
                sv_theme_day.setSummary(select.getName());
                SkinManage.self().loadSkin();
            }
        });

        sv_theme_night.setOnClickListener(new SetSingleSelect<SkinInfo>(getContext()) {
            @Override
            public Collection<SkinInfo> getAll() {
                return allSkinInfos.values();
            }

            @Override
            public String title() {
                return "请选择夜间皮肤";
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
            public void onSelect(SkinInfo select) {
                SharedPreUtil.saveString(SDATA_APP_SKIN_NIGHT, select.getMark());
                sv_theme_night.setSummary(select.getName());
                SkinManage.self().loadSkin();
            }
        });


        sv_plugin_theme.setSummary(SkinModel.getById(SharedPreUtil.getInteger(SDATA_APP_SKIN, SkinModel.BAISE.getId())).getName());
        sv_plugin_theme.setOnClickListener(new SetSingleSelect<SkinModel>(getContext()) {
            @Override
            public Collection<SkinModel> getAll() {
                return java.util.Arrays.asList(SKIN_MODEL);
            }

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

        sv_load_skin.setOnClickListener(v -> {
            searchSkin();
        });

        TaskExecutor.self().run(this::loadData);
    }

    private void loadData() {
        loadSkinInfos();
        //拿到mark,先查询所有的,如果没有,标记为默认的
        String dayMark = SharedPreUtil.getString(SDATA_APP_SKIN_DAY);
        SkinInfo daySkinInfo = allSkinInfos.get(dayMark);
        if (daySkinInfo == null) {
            SharedPreUtil.saveString(SDATA_APP_SKIN_DAY, SkinManage.DEFAULT_MARK);
            daySkinInfo = SkinManage.self().getBuiltInSkin().get(SkinManage.DEFAULT_MARK);
            SkinManage.self().loadSkin();
        }


        final String nightMark = SharedPreUtil.getString(SDATA_APP_SKIN_NIGHT);
        SkinInfo nightSkinInfo = allSkinInfos.get(nightMark);
        if (nightSkinInfo == null) {
            SharedPreUtil.saveString(SDATA_APP_SKIN_NIGHT, SkinManage.DEFAULT_MARK_NIGHT);
            nightSkinInfo = SkinManage.self().getBuiltInSkin().get(SkinManage.DEFAULT_MARK_NIGHT);
            SkinManage.self().loadSkin();
        }

        final String daySkinInfoName = daySkinInfo.getName();
        final String nightSkinInfoName = nightSkinInfo.getName();
        TaskExecutor.self().autoPost(() -> {
            sv_theme_day.setSummary(daySkinInfoName);
            sv_theme_night.setSummary(nightSkinInfoName);
        });
    }

    private void loadSkinInfos() {
        allSkinInfos.clear();
        allOtherSkinInfos.clear();
        List<SkinInfo> temp = DatabaseManage.getAll(SkinInfo.class);
        for (SkinInfo skinInfo : temp) {
            if (CommonUtil.isNotNull(skinInfo.getMark())) {
                allOtherSkinInfos.put(skinInfo.getMark(), skinInfo);
            }
        }
        allSkinInfos.putAll(allOtherSkinInfos);
        allSkinInfos.putAll(SkinManage.self().getBuiltInSkin());
    }


    private void searchSkin() {
        getActivity().showLoading("处理中");
        TaskExecutor.self().post(() -> {
            allSkinInfos.clear();
            String path;
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {// 优先保存到SD卡中
                path = Environment.getExternalStorageDirectory()
                        .getAbsolutePath() + File.separator + "duduLauncher/skin/";
            } else {// 如果SD卡不存在，就保存到本应用的目录下
                path = getContext().getFilesDir().getAbsolutePath()
                        + File.separator + "duduLauncher/skin/";
            }
            File pathFile = new File(path);
            String nameRes = getContext().getResources().getResourceEntryName(R.string.theme_name);

            List<File> fileList = new ArrayList<>();
            if (pathFile.exists()) {
                File[] files = pathFile.listFiles();
                int i = 0;
                for (File file : files) {
                    if (i == 100) {
                        continue;
                    }
                    String filePath = file.getAbsolutePath();
                    System.out.println("filePath:" + filePath);
                    String skinPackageName = SkinCompatManager.getInstance().getSkinPackageName(filePath);
                    System.out.println("skinPackageName:" + skinPackageName);
                    if (CommonUtil.isNull(skinPackageName)) {
                        break;
                    }
                    Resources skinSkinResources = SkinCompatManager.getInstance().getSkinResources(filePath);
                    System.out.println("skinSkinResources:" + skinSkinResources);
                    if (skinSkinResources == null) {
                        break;
                    }
                    fileList.add(file);
                    i++;
                }
                //这里需要把文件复制到相关目录
                new AlertDialog.Builder(getActivity()).setTitle("找到" + fileList.size() + "个皮肤,是否继续导入(重复皮肤会被覆盖)").setNegativeButton("取消", null)
                        .setPositiveButton("继续", (dialog, which) -> {
                            addSkin(fileList);
                        }).show();
            } else {
                System.out.println("文件夹不存在???");
            }
            getActivity().hideLoading();
        });
    }

    private void addSkin(List<File> fileList) {
        getActivity().showLoading("导入中...");
        TaskExecutor.self().run(new Runnable() {
            @Override
            public void run() {
                String nameRes = getContext().getResources().getResourceEntryName(R.string.theme_name);
                int error = 0;
                for (File file : fileList) {
                    String filePath = file.getAbsolutePath();
                    String skinPackageName = SkinCompatManager.getInstance().getSkinPackageName(filePath);
                    if (CommonUtil.isNull(skinPackageName)) {
                        LogEx.e(this, "skinPackageName can not get");
                        error++;
                        break;
                    }
                    String path = copySkinFromToCache(file, skinPackageName);
                    if (CommonUtil.isNull(path)) {
                        LogEx.e(this, "copySkinFromToCache error");
                        error++;
                        break;
                    }
                    Resources skinSkinResources = SkinCompatManager.getInstance().getSkinResources(path);
                    if (skinSkinResources == null) {
                        LogEx.e(this, "skinSkinResources is null");
                        error++;
                        break;
                    }
                    int id = skinSkinResources.getIdentifier(nameRes, "string", skinPackageName);
                    SkinInfo skinInfo = DatabaseManage.getBean(SkinInfo.class, " mark='" + skinPackageName + "'");
                    if (skinInfo == null) {
                        DatabaseManage.insert(new SkinInfo()
                                .setCanUse(IF.YES)
                                .setPath(path)
                                .setMark(skinPackageName)
                                .setName(skinSkinResources.getString(id))
                                .setType(SkinInfo.TYPE_OTHER));
                    } else {
                        DatabaseManage.update(new SkinInfo()
                                .setCanUse(IF.YES)
                                .setPath(path)
                                .setMark(skinPackageName)
                                .setName(skinSkinResources.getString(id))
                                .setType(SkinInfo.TYPE_OTHER), " mark='" + skinPackageName + "'");
                    }
                }
                loadSkinInfos();
                getActivity().hideLoading();
                if (error == 0) {
                    ToastManage.self().show("导入成功");
                } else {
                    ToastManage.self().show(error + "个导入失败");
                }
            }
        });
    }

    private String copySkinFromToCache(File file, String mark) {
        File skinPath = new File(SkinFileUtils.getSkinDir(getActivity()), mark);
        if (skinPath.exists()) {
            skinPath.delete();
        }
        try {
            InputStream is = new FileInputStream(file);
            OutputStream os = new FileOutputStream(skinPath);
            int byteCount;
            byte[] bytes = new byte[1024];
            while ((byteCount = is.read(bytes)) != -1) {
                os.write(bytes, 0, byteCount);
            }
            os.close();
            is.close();
            return skinPath.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
