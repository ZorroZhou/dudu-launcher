package com.wow.carlauncher.view.activity.launcher.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wow.carlauncher.R;
import com.wow.carlauncher.common.CommonData;
import com.wow.carlauncher.common.LogEx;
import com.wow.carlauncher.common.util.SharedPreUtil;
import com.wow.carlauncher.common.util.ViewUtils;
import com.wow.carlauncher.ex.manage.appInfo.AppInfo;
import com.wow.carlauncher.ex.manage.appInfo.AppInfoManage;
import com.wow.carlauncher.view.activity.launcher.BaseThemeView;
import com.wow.carlauncher.view.activity.launcher.ItemInterval;
import com.wow.carlauncher.view.activity.launcher.LayoutEnum;
import com.wow.carlauncher.view.activity.set.event.SEventPromptShowRefresh;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.wow.carlauncher.common.CommonData.SDATA_LAUNCHER_ITEM_INTERVAL;


public class LAppsView extends BaseThemeView implements View.OnClickListener, View.OnLongClickListener {

    public LAppsView(@NonNull Context context, int columnNum, int page) {
        super(context);

        this.columnNum = columnNum;
        this.page = page;

        cellViews = new ArrayList<>();
        rows = new ArrayList<>();
        loadView();
    }

    @Override
    protected int getContent() {
        return R.layout.content_app_grid;
    }

    @BindView(R.id.ll_base)
    LinearLayout ll_base;

    private int columnNum;
    private int page;
    private List<AppInfo> appInfos;

    private List<View> cellViews;
    private List<LinearLayout> rows;

    public void loadView() {
        cellViews.clear();
        rows.clear();

        List<AppInfo> list = AppInfoManage.self().getShowAppInfos();
        int num = columnNum * 4;
        //1    0-16
        //2    16-32
        if (list.size() < num * page) {
            return;
        }
        int end = list.size();
        if (end > num * (page + 1)) {
            end = num * (page + 1);
        }
        appInfos = list.subList(num * page, end);
        int psize = getPageItemNum();
        int leftMargin = ViewUtils.dip2px(getContext(), ItemInterval.getSizeById(SharedPreUtil.getInteger(SDATA_LAUNCHER_ITEM_INTERVAL, ItemInterval.XIAO.getId())));
        LinearLayout row = null;
        for (int i = 0; i < num; i++) {
            if (i % columnNum == 0) {
                row = new LinearLayout(getContext());
                row.setOrientation(LinearLayout.HORIZONTAL);
                LinearLayout.LayoutParams rowLp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 0);
                rowLp.weight = 1;
                if (rows.size() != 0) {
                    rowLp.topMargin = ViewUtils.dip2px(getContext(), ItemInterval.getSizeById(SharedPreUtil.getInteger(SDATA_LAUNCHER_ITEM_INTERVAL, ItemInterval.XIAO.getId())));
                }
                rows.add(row);
                ll_base.addView(row, rowLp);
            }

            AppInfo model = null;
            if (i < appInfos.size()) {
                model = appInfos.get(i);
            }
            View cellView;
            if (model != null) {
                cellView = View.inflate(getContext(), R.layout.content_app_grid_item, null);
                ((TextView) cellView.findViewById(R.id.name)).setText(model.name);
                AppInfoManage.self().setIconWithSkin(cellView.findViewById(R.id.icon), model.clazz);
                cellView.setOnClickListener(this);
                cellView.setOnLongClickListener(this);
                cellView.setTag(i);
            } else {
                cellView = new View(getContext());
                cellView.setTag(-1);
            }
            LinearLayout.LayoutParams cellLp = new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT);
            cellLp.weight = 1;
            if (i % psize == 0) {
                cellLp.leftMargin = 0;
            } else {
                cellLp.leftMargin = leftMargin;
            }
            row.addView(cellView, cellLp);
            cellViews.add(cellView);
        }
        refreshItemLayout();

        LogEx.d(this, "initView: ");
    }

    private LayoutEnum layoutEnum = LayoutEnum.LAYOUT1;

    public void setLayoutEnum(LayoutEnum layoutEnum) {
        if (layoutEnum == null) {
            return;
        }
        if (!layoutEnum.equals(this.layoutEnum)) {
            this.layoutEnum = layoutEnum;
            refreshItemLayout();
        }
    }

    @Override
    public void onClick(View cell) {
        Integer index = (Integer) cell.getTag();
        if (index != null && index >= 0) {
            AppInfo info = appInfos.get(index);
            AppInfoManage.self().openApp(info.appMark + CommonData.CONSTANT_APP_PACKAGE_SEPARATE + info.clazz);
        }
    }

    @Override
    public boolean onLongClick(View cell) {
        Integer index = (Integer) cell.getTag();
        if (index != null && index >= 0) {
            final AppInfo info = appInfos.get(index);
            if (info.appMark == AppInfo.MARK_OTHER_APP) {
                final AlertDialog dialog = new AlertDialog.Builder(getContext()).setView(R.layout.dialog_menu_all_app).show();
                dialog.findViewById(R.id.run).setOnClickListener(v -> {
                    dialog.dismiss();
                    AppInfoManage.self().openApp(info.appMark + CommonData.CONSTANT_APP_PACKAGE_SEPARATE + info.clazz);
                });
                dialog.findViewById(R.id.un).setOnClickListener(v -> {
                    dialog.dismiss();
                    un(info);
                });
            }
        }
        return true;
    }

    private void un(AppInfo info) {
        Intent deleteIntent = new Intent();
        deleteIntent.setAction(Intent.ACTION_DELETE);
        deleteIntent.setData(Uri.parse("package:" + info.clazz));
        getContext().startActivity(deleteIntent);
    }

    private void refreshItemLayout() {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        int margin4 = ViewUtils.dip2px(getContext(), 4);
        int zuoyou = ViewUtils.dip2px(getContext(), ItemInterval.getSizeById(SharedPreUtil.getInteger(SDATA_LAUNCHER_ITEM_INTERVAL, ItemInterval.XIAO.getId())) - 8);
        params.setMargins(zuoyou, 0, zuoyou, margin4);
        if (layoutEnum.equals(LayoutEnum.LAYOUT1)) {
            int margin10 = ViewUtils.dip2px(getContext(), 10);
            int top = ViewUtils.dip2px(getContext(), ItemInterval.getSizeById(SharedPreUtil.getInteger(SDATA_LAUNCHER_ITEM_INTERVAL, ItemInterval.XIAO.getId())));
            int bottom = ViewUtils.dip2px(getContext(), 8);
            params.setMargins(margin10, top, margin10, bottom);
        }
        ll_base.setLayoutParams(params);
    }


    private int getPageItemNum() {
        int psize = SharedPreUtil.getInteger(CommonData.SDATA_LAUNCHER_ITEM_NUM, 3);
        if (psize != 4 && psize != 2 && psize != 5) {
            psize = 3;
        }
        return psize;
    }

}
