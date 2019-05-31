package com.wow.carlauncher.view.activity.set.commonView;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.wow.carlauncher.R;
import com.wow.carlauncher.common.AppContext;
import com.wow.carlauncher.common.TaskExecutor;
import com.wow.carlauncher.common.util.CommonUtil;
import com.wow.carlauncher.common.util.DownUtil;
import com.wow.carlauncher.common.view.PullToRefreshView;
import com.wow.carlauncher.ex.manage.ImageManage;
import com.wow.carlauncher.ex.manage.toast.ToastManage;
import com.wow.carlauncher.repertory.db.entiy.SkinInfo;
import com.wow.carlauncher.repertory.db.manage.DatabaseManage;
import com.wow.carlauncher.repertory.server.UserThemeService;
import com.wow.carlauncher.repertory.server.response.ThemePageResponse;
import com.wow.carlauncher.view.activity.set.SetActivity;
import com.wow.carlauncher.view.activity.set.SetBaseView;
import com.wow.carlauncher.view.activity.set.event.SEventRequestLogin;
import com.wow.carlauncher.view.base.BaseAdapterEx;

import org.greenrobot.eventbus.EventBus;

public class SetOnlineSkinSelectView extends SetBaseView implements View.OnClickListener {
    private SelectAdapter selectAdapter;
    private PullToRefreshView refresh_view;

    public SetOnlineSkinSelectView(SetActivity context) {
        super(context);
    }

    @Override
    protected void initView() {
        ListView list = findViewById(R.id.list);
        refresh_view = findViewById(R.id.refresh_view);
        this.selectAdapter = new SelectAdapter(getActivity());
        list.setAdapter(selectAdapter);
        list.setOnItemClickListener((parent, view, position, id) -> {
            new AlertDialog.Builder(getContext()).setTitle("是否下载新皮肤")
                    .setNegativeButton("取消", null)
                    .setPositiveButton("下载", (dialog12, which) -> {
                        ThemePageResponse.UserThemeDto model = selectAdapter.getItem(position);
                        UserThemeService.getUrl(model.getId(), (code, msg, s) -> TaskExecutor.self().autoPost(() -> DownUtil.loadDownloadApk(getActivity()
                                , "正在下载主题"
                                , "/temp" + model.getVersion() + ".apk"
                                , s)));
                    }).show();
        });

        refresh_view.setOnHeaderRefreshListener(view -> {
            currrentPage = 1;
            loadPage();
        });

        refresh_view.setOnFooterRefreshListener(view -> {
            if (currrentPage < totalPage) {
                currrentPage++;
                loadPage();
            } else {
                refresh_view.onFooterRefreshComplete();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (AppContext.self().getLocalUser() == null) {
            ToastManage.self().show("请先登陆再使用此功能!");
            EventBus.getDefault().post(new SEventRequestLogin());
            return;
        }
        loadPage();
        getActivity().addSetView(this);
    }

    private int totalPage = -1;
    private int currrentPage = 1;

    private void loadPage() {
        getActivity().showLoading("数据加载中");
        UserThemeService.getPage(currrentPage, 10, (code, msg, themePageResponse) -> {
            TaskExecutor.self().autoPost(() -> {
                if (code == 0) {
                    long xx = themePageResponse.getTotal() % 10;
                    if (xx == 0) {
                        totalPage = (int) (themePageResponse.getTotal() / 10);
                    } else {
                        totalPage = (int) (themePageResponse.getTotal() / 10) + 1;
                    }
                    if (currrentPage == 1) {
                        selectAdapter.clear();
                    }
                    selectAdapter.addItems(themePageResponse.getRows());
                } else {
                    ToastManage.self().show(msg);
                }
                refresh_view.onHeaderRefreshComplete();
                refresh_view.onFooterRefreshComplete();
                getActivity().hideLoading();
            });
        });
    }

    @Override
    public String getName() {
        return "在线皮肤列表";
    }

    @Override
    protected int getContent() {
        return R.layout.content_set_skin_online;
    }

    @Override
    public boolean showRight() {
        return false;
    }

    public static class SelectAdapter extends BaseAdapterEx<ThemePageResponse.UserThemeDto> {

        public SelectAdapter(Context context) {
            super(context);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            if (convertView == null) {
                convertView = inflate.inflate(R.layout.item_set_skin_select, viewGroup, false);
            }
            ThemePageResponse.UserThemeDto model = items.get(position);
            if (!model.equals(convertView.getTag())) {
                ImageManage.self().loadImage(model.getThemePic(), convertView.findViewById(R.id.pic));
                SkinInfo skinInfo = DatabaseManage.getBean(SkinInfo.class, " mark='" + model.getApkPackage() + "'");
                String nameMsg = model.getThemeName();
                convertView.findViewById(R.id.tv_down).setVisibility(VISIBLE);
                if (skinInfo != null && skinInfo.getVersion() != null) {
                    if (skinInfo.getVersion() < model.getVersion()) {
                        nameMsg = nameMsg + "(有更新)";
                    } else {
                        nameMsg = nameMsg + "(最新版)";
                        convertView.findViewById(R.id.tv_down).setVisibility(GONE);
                    }
                }
                ((TextView) convertView.findViewById(R.id.name)).setText(nameMsg);
                if (CommonUtil.isNotNull(model.getNickName())) {
                    ((TextView) convertView.findViewById(R.id.uname)).setText(model.getNickName());
                    ImageManage.self().loadImage(model.getUserPic(), convertView.findViewById(R.id.upic));
                } else {
                    ((TextView) convertView.findViewById(R.id.uname)).setText("系统管理员");
                    ((ImageView) convertView.findViewById(R.id.upic)).setImageResource(R.mipmap.ic_launcher);
                }

                ((TextView) convertView.findViewById(R.id.xiazai_num)).setText(String.valueOf(model.getDownTime() + "次下载"));
                convertView.setTag(model);
            }
            return convertView;
        }
    }
}
