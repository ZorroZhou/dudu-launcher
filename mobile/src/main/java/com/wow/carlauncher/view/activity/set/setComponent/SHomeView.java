package com.wow.carlauncher.view.activity.set.setComponent;

import android.annotation.SuppressLint;
import android.support.v7.app.AlertDialog;
import android.view.WindowManager;

import com.wow.carlauncher.R;
import com.wow.carlauncher.common.CommonData;
import com.wow.carlauncher.common.TaskExecutor;
import com.wow.carlauncher.common.util.SharedPreUtil;
import com.wow.carlauncher.common.util.ViewUtils;
import com.wow.carlauncher.common.view.SetView;
import com.wow.carlauncher.ex.manage.baiduVoice.BaiduVoiceAssistant;
import com.wow.carlauncher.ex.manage.toast.ToastManage;
import com.wow.carlauncher.view.activity.launcher.ItemEnum;
import com.wow.carlauncher.view.activity.launcher.ItemInterval;
import com.wow.carlauncher.view.activity.launcher.ItemModel;
import com.wow.carlauncher.view.activity.launcher.ItemTransformer;
import com.wow.carlauncher.view.activity.launcher.LayoutEnum;
import com.wow.carlauncher.view.activity.launcher.event.LItemRefreshEvent;
import com.wow.carlauncher.view.activity.launcher.event.LLayoutRefreshEvent;
import com.wow.carlauncher.view.activity.launcher.event.LPageTransformerChangeEvent;
import com.wow.carlauncher.view.activity.set.LauncherItemAdapter;
import com.wow.carlauncher.view.activity.set.SetActivity;
import com.wow.carlauncher.view.activity.set.SetBaseView;
import com.wow.carlauncher.view.activity.set.event.SEventPromptShowRefresh;
import com.wow.carlauncher.view.activity.set.event.SEventRequestLauncherRecreate;
import com.wow.carlauncher.view.activity.set.commonView.SetNumSelectView;
import com.wow.carlauncher.view.activity.set.commonView.SetSingleSelectView;
import com.wow.carlauncher.view.activity.set.commonView.SetSwitchOnClickListener;

import org.greenrobot.eventbus.EventBus;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import butterknife.BindView;

import static com.wow.carlauncher.common.CommonData.SDATA_LAUNCHER_ITEM_INTERVAL;
import static com.wow.carlauncher.common.CommonData.SDATA_LAUNCHER_ITEM_TRAN;
import static com.wow.carlauncher.common.CommonData.SDATA_LAUNCHER_LAYOUT;

/**
 * Created by 10124 on 2018/4/22.
 */
@SuppressLint("ViewConstructor")
public class SHomeView extends SetBaseView {

    public SHomeView(SetActivity activity) {
        super(activity);
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

    @BindView(R.id.sv_item_interval)
    SetView sv_item_interval;

    @Override
    public String getName() {
        return "首页设置";
    }

    protected void initView() {

        sv_item_interval.setSummary(ItemInterval.getById(SharedPreUtil.getInteger(SDATA_LAUNCHER_ITEM_INTERVAL, ItemInterval.XIAO.getId())).getName());
        sv_item_interval.setOnClickListener(new SetSingleSelectView<ItemInterval>(getActivity(), "请选择首页卡片间隔") {
            @Override
            public Collection<ItemInterval> getAll() {
                return Arrays.asList(CommonData.ITEM_INTERVALS);
            }

            @Override
            public ItemInterval getCurr() {
                return ItemInterval.getById(SharedPreUtil.getInteger(SDATA_LAUNCHER_ITEM_INTERVAL, ItemInterval.XIAO.getId()));
            }

            @Override
            public boolean onSelect(ItemInterval setEnum) {
                SharedPreUtil.saveInteger(SDATA_LAUNCHER_ITEM_INTERVAL, setEnum.getId());
                sv_item_interval.setSummary(setEnum.getName());
                new AlertDialog.Builder(getContext()).setTitle("确认").setNegativeButton("下次重启", null).setPositiveButton("立即生效", (dialog, which) -> {
                    EventBus.getDefault().post(new SEventRequestLauncherRecreate());
                }).setMessage("是否立即生效,立即生效首页将会重新加载").show();
                return true;
            }
        });

        sv_prompt_show.setOnValueChangeListener(new SetSwitchOnClickListener(CommonData.SDATA_LAUNCHER_PROMPT_SHOW) {
            @Override
            public void newValue(boolean value) {
                EventBus.getDefault().post(new SEventPromptShowRefresh());
            }
        });
        sv_prompt_show.setChecked(SharedPreUtil.getBoolean(CommonData.SDATA_LAUNCHER_PROMPT_SHOW, true));


        sv_home_layout.setSummary(LayoutEnum.getById(SharedPreUtil.getInteger(SDATA_LAUNCHER_LAYOUT, LayoutEnum.AUTO.getId())).getName());
        sv_home_layout.setOnClickListener(new SetSingleSelectView<LayoutEnum>(getActivity(), "请选择首页的布局") {
            @Override
            public Collection<LayoutEnum> getAll() {
                return Arrays.asList(CommonData.LAUNCHER_LAYOUTS);
            }

            @Override
            public LayoutEnum getCurr() {
                return LayoutEnum.getById(SharedPreUtil.getInteger(SDATA_LAUNCHER_LAYOUT, LayoutEnum.AUTO.getId()));
            }

            @Override
            public boolean onSelect(LayoutEnum setEnum) {
                SharedPreUtil.saveInteger(SDATA_LAUNCHER_LAYOUT, setEnum.getId());
                sv_home_layout.setSummary(setEnum.getName());
                EventBus.getDefault().post(new LLayoutRefreshEvent());
                return true;
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
                    EventBus.getDefault().post(new SEventRequestLauncherRecreate());
                }).setMessage("是否立即生效,立即生效首页将会重新加载").show();
            }
        });
        sv_home_full.setChecked(SharedPreUtil.getBoolean(CommonData.SDATA_HOME_FULL, true));

        sv_item_tran.setSummary(ItemTransformer.getById(SharedPreUtil.getInteger(SDATA_LAUNCHER_ITEM_TRAN, ItemTransformer.None.getId())).getName());
        sv_item_tran.setOnClickListener(new SetSingleSelectView<ItemTransformer>(getActivity(), "请选择首页切换动画") {
            @Override
            public Collection<ItemTransformer> getAll() {
                return Arrays.asList(CommonData.LAUNCHER_ITEMS_TRANS);
            }

            @Override
            public ItemTransformer getCurr() {
                return ItemTransformer.getById(SharedPreUtil.getInteger(SDATA_LAUNCHER_ITEM_TRAN, ItemTransformer.None.getId()));
            }

            @Override
            public boolean onSelect(ItemTransformer setEnum) {
                SharedPreUtil.saveInteger(SDATA_LAUNCHER_ITEM_TRAN, setEnum.getId());
                sv_item_tran.setSummary(setEnum.getName());
                EventBus.getDefault().post(new LPageTransformerChangeEvent());
                return true;
            }
        });

        sv_launcher_item_num.setSummary(SharedPreUtil.getInteger(CommonData.SDATA_LAUNCHER_ITEM_NUM, 3) + "个");
        sv_launcher_item_num.setOnClickListener(new SetNumSelectView(getActivity(), "请选择首页的插件数量", "个", 2, 5) {
            @Override
            public Integer getCurr() {
                return SharedPreUtil.getInteger(CommonData.SDATA_LAUNCHER_ITEM_NUM, 3);
            }

            @Override
            public void onSelect(Integer t, String ss) {
                SharedPreUtil.saveInteger(CommonData.SDATA_LAUNCHER_ITEM_NUM, t);
                sv_launcher_item_num.setSummary(ss);
                EventBus.getDefault().post(new LItemRefreshEvent());
            }
        });

        sv_launcher_item_sort_re.setOnClickListener(v -> {
            new AlertDialog.Builder(getContext()).setTitle("警告!").setNegativeButton("取消", null).setPositiveButton("确定", (dialog2, which2) -> {
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

    }
}
