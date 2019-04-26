package com.wow.carlauncher.view.activity.set.view;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.view.WindowManager;

import com.wow.carlauncher.R;
import com.wow.carlauncher.common.CommonData;
import com.wow.carlauncher.common.util.SharedPreUtil;
import com.wow.carlauncher.common.util.ThreadObj;
import com.wow.carlauncher.common.util.ViewUtils;
import com.wow.carlauncher.common.view.SetView;
import com.wow.carlauncher.ex.plugin.music.MusicControllerEnum;
import com.wow.carlauncher.ex.plugin.music.MusicPlugin;
import com.wow.carlauncher.view.activity.launcher.ItemEnum;
import com.wow.carlauncher.view.activity.launcher.ItemModel;
import com.wow.carlauncher.view.activity.launcher.ItemTransformer;
import com.wow.carlauncher.view.activity.launcher.event.LItemRefreshEvent;
import com.wow.carlauncher.view.activity.launcher.event.LPageTransformerChangeEvent;
import com.wow.carlauncher.view.activity.set.LauncherItemAdapter;
import com.wow.carlauncher.view.activity.set.SetEnumOnClickListener;
import com.wow.carlauncher.view.activity.set.SetSwitchOnClickListener;
import com.wow.carlauncher.view.base.BaseEXView;

import org.greenrobot.eventbus.EventBus;
import org.xutils.view.annotation.ViewInject;

import java.util.List;

import static com.wow.carlauncher.common.CommonData.SDATA_LAUNCHER_ITEM_TRAN;
import static com.wow.carlauncher.common.CommonData.SDATA_MUSIC_CONTROLLER;

/**
 * Created by 10124 on 2018/4/22.
 */

public class SHomeView extends BaseEXView {

    public SHomeView(@NonNull Context context) {
        super(context);
    }

    public SHomeView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected int getContent() {
        return R.layout.content_set_home;
    }

    @ViewInject(R.id.sv_launcher_item_sort)
    private SetView sv_launcher_item_sort;

    @ViewInject(R.id.sv_launcher_item_sort_re)
    private SetView sv_launcher_item_sort_re;

    @ViewInject(R.id.sv_launcher_item_num)
    private SetView sv_launcher_item_num;

    @ViewInject(R.id.sv_item_tran)
    private SetView sv_item_tran;

    @ViewInject(R.id.sv_plugin_select)
    private SetView sv_plugin_select;

    @ViewInject(R.id.sv_music_inside_cover)
    private SetView sv_music_inside_cover;

    @Override
    protected void initView() {


        sv_item_tran.setSummary(ItemTransformer.getById(SharedPreUtil.getInteger(SDATA_LAUNCHER_ITEM_TRAN, ItemTransformer.None.getId())).getName());
        sv_item_tran.setOnClickListener(new SetEnumOnClickListener<ItemTransformer>(getContext(), CommonData.LAUNCHER_ITEMS_TRANS) {
            @Override
            public String title() {
                return "请选择首页切换动画";
            }

            @Override
            public ItemTransformer getCurr() {
                return ItemTransformer.getById(SharedPreUtil.getInteger(SDATA_LAUNCHER_ITEM_TRAN, ItemTransformer.None.getId()));
            }

            @Override
            public void onSelect(ItemTransformer setEnum) {
                SharedPreUtil.saveInteger(SDATA_LAUNCHER_ITEM_TRAN, setEnum.getId());
                sv_item_tran.setSummary(setEnum.getName());
                EventBus.getDefault().post(new LPageTransformerChangeEvent());
            }
        });

        final String[] itemsNum = {
                "3个", "4个"
        };
        sv_launcher_item_num.setSummary(SharedPreUtil.getInteger(CommonData.SDATA_LAUNCHER_ITEM_NUM, 3) + "个");
        sv_launcher_item_num.setOnClickListener(v -> {
            int select = SharedPreUtil.getInteger(CommonData.SDATA_LAUNCHER_ITEM_NUM, 3) - 3;
            final ThreadObj<Integer> obj = new ThreadObj<>(select);
            new AlertDialog.Builder(getContext()).setTitle("请选择首页的插件数量").setNegativeButton("取消", null).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    new AlertDialog.Builder(getContext()).setTitle("警告!").setNegativeButton("取消", null).setPositiveButton("确定", (dialog2, which2) -> {
                        SharedPreUtil.saveInteger(CommonData.SDATA_LAUNCHER_ITEM_NUM, obj.getObj() + 3);
                        sv_launcher_item_num.setSummary(itemsNum[obj.getObj()]);
                        EventBus.getDefault().post(new LItemRefreshEvent());
                    }).setMessage("是否确认更改,会导致桌面插件重新加载").show();
                }
            }).setSingleChoiceItems(itemsNum, select, (dialog12, which) -> obj.setObj(which)).show();
        });

        sv_launcher_item_sort_re.setOnClickListener(v -> {
            AlertDialog queren = new AlertDialog.Builder(getContext()).setTitle("警告!").setNegativeButton("取消", null).setPositiveButton("确定", (dialog2, which2) -> {
                for (ItemEnum item : CommonData.LAUNCHER_ITEMS) {
                    SharedPreUtil.saveInteger(CommonData.SDATA_LAUNCHER_ITEM_SORT_ + item.getId(), item.getId());
                    SharedPreUtil.saveBoolean(CommonData.SDATA_LAUNCHER_ITEM_OPEN_ + item.getId(), true);
                }
                EventBus.getDefault().post(new LItemRefreshEvent());
            }).setMessage("是否确认更改,会导致桌面插件重新加载").show();
        });

        sv_launcher_item_sort.setOnClickListener(v -> {
            final LauncherItemAdapter adapter = new LauncherItemAdapter(getContext());
            AlertDialog dialog = new AlertDialog.Builder(getContext()).setTitle("调整插件顺序").setNegativeButton("取消", null).setPositiveButton("确定", (dialog1, which1) -> {
                new AlertDialog.Builder(getContext()).setTitle("警告!").setNegativeButton("取消", null).setPositiveButton("确定", (dialog2, which2) -> {
                    List<ItemModel> items = adapter.getItems();
                    for (ItemModel item : items) {
                        SharedPreUtil.saveInteger(CommonData.SDATA_LAUNCHER_ITEM_SORT_ + item.info.getId(), item.index);
                        SharedPreUtil.saveBoolean(CommonData.SDATA_LAUNCHER_ITEM_OPEN_ + item.info.getId(), item.check);
                    }
                    EventBus.getDefault().post(new LItemRefreshEvent());
                }).setMessage("是否确认更改,会导致桌面插件重新加载").show();
            }).setAdapter(adapter, null).show();

            WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
            lp.width = (int) (ViewUtils.getDisplayMetriocs(getContext()).widthPixels * 0.8);//定义宽度
            dialog.getWindow().setAttributes(lp);
        });

        sv_plugin_select.setSummary(MusicControllerEnum.getById(SharedPreUtil.getInteger(SDATA_MUSIC_CONTROLLER, MusicControllerEnum.SYSMUSIC.getId())).getName());
        sv_plugin_select.setOnClickListener(new SetEnumOnClickListener<MusicControllerEnum>(getContext(), CommonData.MUSIC_CONTROLLER) {

            @Override
            public String title() {
                return "请选择音乐控制协议";
            }

            @Override
            public MusicControllerEnum getCurr() {
                return MusicControllerEnum.getById(SharedPreUtil.getInteger(SDATA_MUSIC_CONTROLLER, MusicControllerEnum.SYSMUSIC.getId()));
            }

            @Override
            public void onSelect(MusicControllerEnum setEnum) {
                SharedPreUtil.saveInteger(SDATA_MUSIC_CONTROLLER, setEnum.getId());
                MusicPlugin.self().setController(setEnum);
                sv_plugin_select.setSummary(setEnum.getName());
            }
        });


        sv_music_inside_cover.setOnValueChangeListener(new SetSwitchOnClickListener(CommonData.SDATA_MUSIC_INSIDE_COVER));
        sv_music_inside_cover.setChecked(SharedPreUtil.getBoolean(CommonData.SDATA_MUSIC_INSIDE_COVER, true));


    }
}
