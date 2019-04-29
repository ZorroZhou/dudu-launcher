package com.wow.carlauncher.view.activity.launcher.view;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.wow.carlauncher.R;
import com.wow.carlauncher.common.util.ViewUtils;
import com.wow.carlauncher.ex.manage.ThemeManage;
import com.wow.carlauncher.ex.plugin.obd.ObdPlugin;
import com.wow.carlauncher.ex.plugin.obd.evnet.PObdEventCarTp;
import com.wow.carlauncher.ex.plugin.obd.evnet.PObdEventConnect;
import com.wow.carlauncher.view.base.BaseEXView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static com.wow.carlauncher.common.CommonData.TAG;
import static com.wow.carlauncher.ex.manage.ThemeManage.Theme.BLACK;
import static com.wow.carlauncher.ex.manage.ThemeManage.Theme.WHITE;
import static com.wow.carlauncher.view.activity.launcher.view.LShadowView.SizeEnum.FIVE;

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
    public void changedTheme(ThemeManage manage) {
        rl_base.setBackgroundResource(manage.getCurrentThemeRes(R.drawable.n_l_item1_bg));
        tv_title.setTextColor(manage.getCurrentThemeColor(R.color.l_title));
        tv_msg.setTextColor(manage.getCurrentThemeColor(R.color.l_msg));
        iv_error.setImageResource(manage.getCurrentThemeRes(R.mipmap.ic_obderror));

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

        fl_lt_root.removeAllViews();
        if (fl_lt.getParent() != null) {
            ((ViewGroup) fl_lt.getParent()).removeView(fl_lt);
        }

        fl_rt_root.removeAllViews();
        if (fl_rt.getParent() != null) {
            ((ViewGroup) fl_rt.getParent()).removeView(fl_rt);
        }

        fl_lb_root.removeAllViews();
        if (fl_lb.getParent() != null) {
            ((ViewGroup) fl_lb.getParent()).removeView(fl_lb);
        }

        fl_rb_root.removeAllViews();
        if (fl_rb.getParent() != null) {
            ((ViewGroup) fl_rb.getParent()).removeView(fl_rb);
        }
        if (currentTheme == WHITE || currentTheme == BLACK) {
            tv_title.setGravity(Gravity.CENTER);
            fl_rt_root.addView(LShadowView.getShadowView(getContext(), fl_rt, FIVE), MATCH_PARENT, MATCH_PARENT);
            fl_rb_root.addView(LShadowView.getShadowView(getContext(), fl_rb, FIVE), MATCH_PARENT, MATCH_PARENT);
            fl_lb_root.addView(LShadowView.getShadowView(getContext(), fl_lb, FIVE), MATCH_PARENT, MATCH_PARENT);
            fl_lt_root.addView(LShadowView.getShadowView(getContext(), fl_lt, FIVE), MATCH_PARENT, MATCH_PARENT);
        } else {
            tv_title.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);

            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT);
            int margin = ViewUtils.dip2px(getContext(), 5);
            params.setMargins(margin, margin, margin, margin);

            fl_lb_root.addView(fl_lb, params);
            fl_rt_root.addView(fl_rt, params);
            fl_rb_root.addView(fl_rb, params);
            fl_lt_root.addView(fl_lt, params);
        }

        Log.e(TAG + getClass().getSimpleName(), "changedTheme: ");
    }

    @Event(value = {R.id.rl_base})
    private void clickEvent(View view) {
        switch (view.getId()) {
            case R.id.rl_base: {
                if (ObdPlugin.self().notConnect()) {
                    new AlertDialog.Builder(getContext()).setTitle("警告!")
                            .setNegativeButton("取消", null)
                            .setPositiveButton("确定", (dialog2, which2) -> {
                                BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                                mBluetoothAdapter.disable();
                                x.task().postDelayed(mBluetoothAdapter::enable, 300);
                            })
                            .setMessage("是否确认重启蓝牙").show();
                }
                break;
            }
        }
    }


    @ViewInject(R.id.fl_rb_root)
    private FrameLayout fl_rb_root;

    @ViewInject(R.id.fl_rb)
    private FrameLayout fl_rb;

    @ViewInject(R.id.fl_lb_root)
    private FrameLayout fl_lb_root;

    @ViewInject(R.id.fl_lb)
    private FrameLayout fl_lb;

    @ViewInject(R.id.fl_rt_root)
    private FrameLayout fl_rt_root;

    @ViewInject(R.id.fl_rt)
    private FrameLayout fl_rt;

    @ViewInject(R.id.fl_lt_root)
    private FrameLayout fl_lt_root;

    @ViewInject(R.id.fl_lt)
    private FrameLayout fl_lt;

    @ViewInject(R.id.tv_title)
    private TextView tv_title;

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

    @ViewInject(R.id.iv_error)
    private ImageView iv_error;

    @ViewInject(R.id.tv_msg)
    private TextView tv_msg;

    @ViewInject(R.id.ll_ty)
    private View ll_ty;

    @ViewInject(R.id.ll_msg)
    private View ll_msg;

    @Override
    protected void initView() {
        onEvent(ObdPlugin.self().getCurrentPObdEventCarTp());
        Log.e(TAG + getClass().getSimpleName(), "initView: ");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(final PObdEventConnect event) {
        boolean show = false;
        if (event.isConnected()) {
            if (ObdPlugin.self().supportTp()) {
                show = true;
            } else {
                tv_msg.setText(R.string.obd_not_tp);
            }
        } else {
            tv_msg.setText(R.string.obd_not_connect);
        }
        ll_ty.setVisibility(show ? VISIBLE : GONE);
        ll_msg.setVisibility(show ? GONE : VISIBLE);
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
