package com.wow.carlauncher.view.activity.launcher.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.meetsl.scardview.SCardView;
import com.wow.carlauncher.R;
import com.wow.carlauncher.view.base.BaseView;

import org.xutils.view.annotation.ViewInject;

public class LPageView extends BaseView {
    private int num;

    public LPageView(@NonNull Context context, int num) {
        super(context);
        this.num = num;
        if (this.num != 3) {
            this.num = 4;
        }
    }

    @Override
    protected void initView() {
        if (num == 3) {
            sv_4.setVisibility(GONE);
        }
    }

    @Override
    protected int getContent() {
        return R.layout.content_l_page;
    }

    public void setItem(View[] item) {
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        if (item.length > 0 && item[0] != null) {
            sv_1.addView(item[0], lp);
        } else {
            sv_1.setVisibility(INVISIBLE);
        }
        if (item.length > 1 && item[1] != null) {
            sv_2.addView(item[1], lp);
        } else {
            sv_2.setVisibility(INVISIBLE);
        }
        if (item.length > 2 && item[2] != null) {
            sv_3.addView(item[2], lp);
        } else {
            sv_3.setVisibility(INVISIBLE);
        }
        if (num == 3) {
            sv_4.setVisibility(GONE);
        } else {
            if (item.length > 3 && item[3] != null) {
                sv_4.addView(item[3], lp);
            } else {
                sv_4.setVisibility(INVISIBLE);
            }
        }
    }

    @ViewInject(R.id.sv_1)
    private SCardView sv_1;

    @ViewInject(R.id.sv_2)
    private SCardView sv_2;

    @ViewInject(R.id.sv_3)
    private SCardView sv_3;

    @ViewInject(R.id.sv_4)
    private SCardView sv_4;
}
