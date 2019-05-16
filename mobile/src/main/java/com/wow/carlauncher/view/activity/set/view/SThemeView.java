package com.wow.carlauncher.view.activity.set.view;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.os.Environment;
import android.support.v7.app.AlertDialog;

import com.wow.carlauncher.R;
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
import com.wow.carlauncher.view.activity.set.listener.SetEnumOnClickListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

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

    private List<SkinInfo> skinInfos;

    @Override
    protected void initView() {
        skinInfos = new ArrayList<>();
        TaskExecutor.self().post(() -> skinInfos.addAll(DatabaseManage.getAll(SkinInfo.class)));

        final String dayMark = SharedPreUtil.getString(SDATA_APP_SKIN_DAY);
        SkinInfo moren = DatabaseManage.getBean(SkinInfo.class, " mark='" + dayMark + "'");
        if (moren == null) {
            moren = new SkinInfo()
                    .setCanUse(IF.YES)
                    .setMark(SkinManage.DEFAULT_MARK)
                    .setName("默认皮肤")
                    .setType(SkinInfo.TYPE_APP_IN);
            DatabaseManage.insert(moren);
        }
        sv_theme_day.setSummary(moren.getName());
        sv_theme_day.setOnClickListener(new SetEnumOnClickListener<SkinInfo>(getContext(), skinInfos) {
            @Override
            public String title() {
                return "请选择默认皮肤";
            }

            @Override
            public SkinInfo getCurr() {
                return DatabaseManage.getBean(SkinInfo.class, " mark='" + dayMark + "'");
            }

            @Override
            public boolean equals(SkinInfo t1, SkinInfo t2) {
                return CommonUtil.equals(t1.getMark(), t2.getMark());
            }

            @Override
            public void onSelect(SkinInfo select) {
                System.out.println("!!!!!select:" + select);
                SharedPreUtil.saveString(SDATA_APP_SKIN_DAY, select.getMark());
                sv_theme_day.setSummary(select.getName());
                SkinManage.self().loadSkin(null);
            }
        });

        String heiseMark = SharedPreUtil.getString(SDATA_APP_SKIN_NIGHT);
        SkinInfo heise = DatabaseManage.getBean(SkinInfo.class, " mark='" + heiseMark + "'");
        if (heise == null) {
            heise = new SkinInfo()
                    .setCanUse(IF.YES)
                    .setMark(SkinManage.DEFAULT_MARK_NIGHT)
                    .setPath("heise.skin")
                    .setName("夜间皮肤")
                    .setType(SkinInfo.TYPE_APP_IN);
            DatabaseManage.insert(heise);
        }

        sv_theme_night.setSummary(heise.getName());


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

        sv_load_skin.setOnClickListener(v -> {
            searchSkin();
        });
    }

    private void searchSkin() {
        getActivity().showLoading("处理中");
        TaskExecutor.self().post(() -> {
            skinInfos.clear();
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

                    int id = skinSkinResources.getIdentifier(nameRes, "string", skinPackageName);

                    SkinInfo skinInfo = DatabaseManage.getBean(SkinInfo.class, " mark='" + skinPackageName + "'");
                    if (skinInfo == null) {
                        DatabaseManage.insert(new SkinInfo()
                                .setCanUse(IF.YES)
                                .setPath(filePath)
                                .setMark(skinPackageName)
                                .setName(skinSkinResources.getString(id))
                                .setType(SkinInfo.TYPE_OTHER));
                    } else {
                        DatabaseManage.update(new SkinInfo()
                                .setCanUse(IF.YES)
                                .setPath(filePath)
                                .setMark(skinPackageName)
                                .setName(skinSkinResources.getString(id))
                                .setType(SkinInfo.TYPE_OTHER), " mark='" + skinPackageName + "'");
                    }
                    i++;
                }
                skinInfos.addAll(DatabaseManage.getAll(SkinInfo.class));
                System.out.println(skinInfos);

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
                    System.out.println("filePath:" + filePath);
                    String skinPackageName = SkinCompatManager.getInstance().getSkinPackageName(filePath);
                    System.out.println("skinPackageName:" + skinPackageName);
                    if (CommonUtil.isNull(skinPackageName)) {
                        error++;
                        break;
                    }
                    String path = copySkinFromToCache(file, skinPackageName);
                    if (CommonUtil.isNull(path)) {
                        error++;
                        break;
                    }
                    Resources skinSkinResources = SkinCompatManager.getInstance().getSkinResources(filePath);
                    System.out.println("skinSkinResources:" + skinSkinResources);
                    if (skinSkinResources == null) {
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
        if (skinPath.exists() && skinPath.delete()) {
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
        }
        return null;
    }
}
