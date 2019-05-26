package com.wow.carlauncher.view.activity.set.listener;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.wow.carlauncher.R;
import com.wow.carlauncher.common.TaskExecutor;
import com.wow.carlauncher.common.util.CommonUtil;
import com.wow.carlauncher.common.view.PullToRefreshView;
import com.wow.carlauncher.ex.manage.ImageManage;
import com.wow.carlauncher.ex.manage.toast.ToastManage;
import com.wow.carlauncher.repertory.server.CommonCallback;
import com.wow.carlauncher.repertory.server.UserThemeService;
import com.wow.carlauncher.repertory.server.response.ThemePageResponse;
import com.wow.carlauncher.view.activity.set.SetActivity;
import com.wow.carlauncher.view.activity.set.SetBaseView;
import com.wow.carlauncher.view.base.BaseAdapterEx;

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
        loadPage();
        getActivity().addSetView(this);
    }

    private int totalPage = -1;
    private int currrentPage = 1;

    private void loadPage() {
        getActivity().showLoading("数据加载中");
        UserThemeService.getPage(currrentPage, 10, (code, msg, themePageResponse) -> {
            TaskExecutor.self().autoPost(new Runnable() {
                @Override
                public void run() {
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
                }
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
        return true;
    }

    @Override
    public String rightTitle() {
        return "保存";
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
                ((TextView) convertView.findViewById(R.id.name)).setText(model.getThemeName());
                if (CommonUtil.isNotNull(model.getNickName())) {
                    ((TextView) convertView.findViewById(R.id.uname)).setText(model.getNickName());
                    ImageManage.self().loadImage(model.getUserPic(), convertView.findViewById(R.id.upic));
                } else {
                    ((TextView) convertView.findViewById(R.id.uname)).setText("系统管理员");
                    ((ImageView) convertView.findViewById(R.id.upic)).setImageResource(R.mipmap.ic_launcher);
                }
                convertView.setTag(model);
            }
            return convertView;
        }
    }
}
