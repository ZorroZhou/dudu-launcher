package com.wow.carlauncher.view.activity.launcher.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wow.carlauncher.R;
import com.wow.carlauncher.ex.manage.ThemeManage;
import com.wow.carlauncher.ex.plugin.obd.ObdPlugin;
import com.wow.carlauncher.ex.plugin.obd.evnet.PObdEventCarTp;
import com.wow.carlauncher.ex.plugin.obd.evnet.PObdEventConnect;
import com.wow.carlauncher.view.base.BaseEXView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by 10124 on 2018/4/20.
 */

public class LTaiyaView extends BaseEXView {

    public LTaiyaView(@NonNull Context context) {
        super(context);
    }

    public LTaiyaView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected int getContent() {
        return R.layout.content_l_taiya;
    }

    @Override
    public void onThemeChanged(ThemeManage manage) {
        Context context = getContext();
        rl_base.setBackgroundResource(manage.getCurrentThemeRes(context, R.drawable.n_l_item1_bg));
        tv_text1.setTextColor(manage.getCurrentThemeColor(context, R.color.l_text1));

        manage.setViewsBackround(this, new int[]{
                R.id.tv_lt,
                R.id.tv_rt,
                R.id.tv_lb,
                R.id.tv_rb
        }, R.drawable.n_cell_bg);


        manage.setTextViewsColor(this, new int[]{
                R.id.tv_lt,
                R.id.tv_rt,
                R.id.tv_lb,
                R.id.tv_rb
        }, R.color.l_text2);

        //iv_img.setImageResource(manage.getCurrentThemeRes(context, R.mipmap.n_car_b));
    }

    @ViewInject(R.id.tv_text1)
    private TextView tv_text1;

    @ViewInject(R.id.tv_lt)
    private TextView tv_lt;

    @ViewInject(R.id.tv_rt)
    private TextView tv_rt;

    @ViewInject(R.id.tv_lb)
    private TextView tv_lb;

    @ViewInject(R.id.tv_rb)
    private TextView tv_rb;

    @ViewInject(R.id.rl_base)
    private View rl_base;

    @ViewInject(R.id.iv_img)
    private ImageView iv_img;

    @Override
    protected void initView() {
        onEvent(ObdPlugin.self().getCurrentPObdEventCarTp());
    }

    @SuppressLint("SetTextI18n")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(final PObdEventConnect event) {
//        boolean show = false;
//        if (event.isConnected()) {
//            if (ObdPlugin.self().supportTp()) {
//                show = true;
//            } else {
//                tv_msg.setText("OBD设备不支持胎压");
//            }
//        } else {
//            tv_msg.setText("没有连接OBD");
//        }
//        ll_ty.setVisibility(show ? VISIBLE : GONE);
//        ll_msg.setVisibility(show ? GONE : VISIBLE);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(final PObdEventCarTp event) {
        if (tv_lt != null && event.getlFTirePressure() != null) {
            tv_lt.setText(getContext().getString(R.string.launcher_tp, "左前", event.getlFTirePressure(), event.getlFTemp()));
        }

        if (tv_lb != null && event.getlBTirePressure() != null) {
            tv_lb.setText(getContext().getString(R.string.launcher_tp, "左后", event.getlBTirePressure(), event.getlBTemp()));
        }

        if (tv_rt != null && event.getrFTirePressure() != null) {
            tv_rt.setText(getContext().getString(R.string.launcher_tp, "右前", event.getrFTirePressure(), event.getrFTemp()));
        }

        if (tv_rb != null && event.getrBTirePressure() != null) {
            tv_rb.setText(getContext().getString(R.string.launcher_tp, "右后", event.getrBTirePressure(), event.getrBTemp()));
        }
    }
}
