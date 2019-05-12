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
import com.wow.carlauncher.common.TaskExecutor;
import com.wow.carlauncher.common.util.SharedPreUtil;
import com.wow.carlauncher.common.util.ThreadObj;
import com.wow.carlauncher.common.util.ViewUtils;
import com.wow.carlauncher.common.view.SetView;
import com.wow.carlauncher.ex.manage.baiduVoice.BaiduVoiceAssistant;
import com.wow.carlauncher.ex.manage.toast.ToastManage;
import com.wow.carlauncher.view.activity.launcher.ItemEnum;
import com.wow.carlauncher.view.activity.launcher.ItemModel;
import com.wow.carlauncher.view.activity.launcher.ItemTransformer;
import com.wow.carlauncher.view.activity.launcher.LayoutEnum;
import com.wow.carlauncher.view.activity.launcher.event.LItemRefreshEvent;
import com.wow.carlauncher.view.activity.launcher.event.LLayoutRefreshEvent;
import com.wow.carlauncher.view.activity.launcher.event.LPageTransformerChangeEvent;
import com.wow.carlauncher.view.activity.set.LauncherItemAdapter;
import com.wow.carlauncher.view.activity.set.SetEnumOnClickListener;
import com.wow.carlauncher.view.activity.set.SetSwitchOnClickListener;
import com.wow.carlauncher.view.activity.set.event.SEventPromptShowRefresh;
import com.wow.carlauncher.view.activity.set.event.SEventSetHomeFull;
import com.wow.carlauncher.view.base.BaseView;
import com.wow.carlauncher.view.clickListener.PicSelectOnClickListener;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;

import static com.wow.carlauncher.common.CommonData.LAUNCHER_LAYOUTS;
import static com.wow.carlauncher.common.CommonData.SDATA_LAUNCHER_ITEM_TRAN;
import static com.wow.carlauncher.common.CommonData.SDATA_LAUNCHER_LAYOUT;

/**
 * Created by 10124 on 2018/4/22.
 */

public class SHomeView extends BaseView {

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

    @BindView(R.id.sv_launcher_item_sort)
    SetView sv_launcher_item_sort;

    @BindView(R.id.sv_launcher_item_sort_re)
    SetView sv_launcher_item_sort_re;

    @BindView(R.id.sv_launcher_item_num)
    SetView sv_launcher_item_num;

    @BindView(R.id.sv_item_tran)
    SetView sv_item_tran;

    @BindView(R.id.sv_home_full)
    SetView sv_home_full;

    @BindView(R.id.sv_use_va)
    SetView sv_use_va;

    @BindView(R.id.sv_home_layout)
    SetView sv_home_layout;

    @BindView(R.id.sv_prompt_show)
    SetView sv_prompt_show;


    protected void initView() {
        sv_prompt_show.setOnValueChangeListener(new SetSwitchOnClickListener(CommonData.SDATA_LAUNCHER_PROMPT_SHOW) {
            @Override
            public void newValue(boolean value) {
                EventBus.getDefault().post(new SEventPromptShowRefresh());
            }
        });
        sv_prompt_show.setChecked(SharedPreUtil.getBoolean(CommonData.SDATA_LAUNCHER_PROMPT_SHOW, true));


        sv_home_layout.setSummary(LayoutEnum.getById(SharedPreUtil.getInteger(SDATA_LAUNCHER_LAYOUT, LayoutEnum.LAYOUT1.getId())).getName());
        sv_home_layout.setOnClickListener(new PicSelectOnClickListener<LayoutEnum>(getContext(), LAUNCHER_LAYOUTS) {
            @Override
            public String title() {
                return "请选择首页的布局";
            }

            @Override
            public LayoutEnum getCurr() {
                return LayoutEnum.getById(SharedPreUtil.getInteger(SDATA_LAUNCHER_LAYOUT, LayoutEnum.LAYOUT1.getId()));
            }

            @Override
            public void onSelect(LayoutEnum setEnum) {
                SharedPreUtil.saveInteger(SDATA_LAUNCHER_LAYOUT, setEnum.getId());
                sv_home_layout.setSummary(setEnum.getName());
                EventBus.getDefault().post(new LLayoutRefreshEvent());
            }
        });


        sv_use_va.setOnValueChangeListener(new SetSwitchOnClickListener(CommonData.SDATA_USE_VA) {
            @Override
            public void newValue(boolean value) {
                if (!value) {
                    ToastManage.self().show("语音助手将在下次启动时关闭");
                } else {
                    BaiduVoiceAssistant.self().init(getContext());
                    TaskExecutor.self().run(() -> {
                        ToastManage.self().show("正在开启语音助手,如果没有异常提示,则开启成功!");
                        BaiduVoiceAssistant.self().startWakeUp();
                    }, 1000);
                }
            }
        });
        sv_use_va.setChecked(SharedPreUtil.getBoolean(CommonData.SDATA_USE_VA, false));


        sv_home_full.setOnValueChangeListener(new SetSwitchOnClickListener(CommonData.SDATA_HOME_FULL) {
            @Override
            public void newValue(boolean value) {
                new AlertDialog.Builder(getContext()).setTitle("确认").setNegativeButton("下次重启", null).setPositiveButton("立即生效", (dialog, which) -> {
                    EventBus.getDefault().post(new SEventSetHomeFull());
                }).setMessage("是否立即生效,立即生效首页将会重新加载").show();
            }
        });
        sv_home_full.setChecked(SharedPreUtil.getBoolean(CommonData.SDATA_HOME_FULL, true));

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
                "2个", "3个", "4个", "5个"
        };
        sv_launcher_item_num.setSummary(SharedPreUtil.getInteger(CommonData.SDATA_LAUNCHER_ITEM_NUM, 3) + "个");
        sv_launcher_item_num.setOnClickListener(v -> {
            int select = SharedPreUtil.getInteger(CommonData.SDATA_LAUNCHER_ITEM_NUM, 3) - 2;
            final ThreadObj<Integer> obj = new ThreadObj<>(select);
            new AlertDialog.Builder(getContext()).setTitle("请选择首页的插件数量").setNegativeButton("取消", null).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    new AlertDialog.Builder(getContext()).setTitle("警告!").setNegativeButton("取消", null).setPositiveButton("确定", (dialog2, which2) -> {
                        SharedPreUtil.saveInteger(CommonData.SDATA_LAUNCHER_ITEM_NUM, obj.getObj() + 2);
                        sv_launcher_item_num.setSummary(itemsNum[obj.getObj()]);
                        EventBus.getDefault().post(new LItemRefreshEvent());
                    }).setMessage("是否确认更改,会导致桌面插件重新加载").show();
                }
            }).setSingleChoiceItems(itemsNum, select, (dialog12, which) -> obj.setObj(which)).show();
        });

        sv_launcher_item_sort_re.setOnClickListener(v -> {
            new AlertDialog.Builder(getContext()).setTitle("警告!").setNegativeButton("取消", null).setPositiveButton("确定", (dialog2, which2) -> {
                for (ItemEnum item : CommonData.LAUNCHER_ITEMS) {
                    if (item.equals(ItemEnum.FM)) {
                        SharedPreUtil.saveInteger(CommonData.SDATA_LAUNCHER_ITEM_SORT_ + item.getId(), item.getId());
                        SharedPreUtil.saveBoolean(CommonData.SDATA_LAUNCHER_ITEM_OPEN_ + item.getId(), false);
                    } else {
                        SharedPreUtil.saveInteger(CommonData.SDATA_LAUNCHER_ITEM_SORT_ + item.getId(), item.getId());
                        SharedPreUtil.saveBoolean(CommonData.SDATA_LAUNCHER_ITEM_OPEN_ + item.getId(), true);
                    }
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

    }
}
