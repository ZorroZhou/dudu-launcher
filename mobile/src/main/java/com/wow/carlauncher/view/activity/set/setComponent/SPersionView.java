package com.wow.carlauncher.view.activity.set.setComponent;

import android.annotation.SuppressLint;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wow.carlauncher.R;
import com.wow.carlauncher.common.AppContext;
import com.wow.carlauncher.common.CommonData;
import com.wow.carlauncher.common.TaskExecutor;
import com.wow.carlauncher.common.user.event.UEventRefreshLoginState;
import com.wow.carlauncher.common.util.CommonUtil;
import com.wow.carlauncher.common.util.SharedPreUtil;
import com.wow.carlauncher.common.view.SetView;
import com.wow.carlauncher.ex.manage.ImageManage;
import com.wow.carlauncher.ex.manage.toast.ToastManage;
import com.wow.carlauncher.repertory.server.UserService;
import com.wow.carlauncher.view.activity.set.SetActivity;
import com.wow.carlauncher.view.activity.set.SetBaseView;
import com.wow.carlauncher.view.activity.set.commonView.BindEmailView;
import com.wow.carlauncher.view.activity.set.commonView.SetSwitchOnClickListener;
import com.wow.carlauncher.view.activity.set.event.SEventRequestLogin;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;

/**
 * Created by 10124 on 2018/4/22.
 */
@SuppressLint("ViewConstructor")
public class SPersionView extends SetBaseView {
    public SPersionView(SetActivity activity) {
        super(activity);
    }

    @Override
    protected int getContent() {
        return R.layout.content_set_persion;
    }

    @Override
    public String getName() {
        return "个人中心";
    }

    @BindView(R.id.sv_logout)
    SetView sv_logout;

    @BindView(R.id.sv_bind)
    SetView sv_bind;

    @BindView(R.id.sv_unbind)
    SetView sv_unbind;

    @BindView(R.id.sv_allow_report_location)
    SetView sv_allow_report_location;

    @BindView(R.id.ll_user)
    LinearLayout ll_user;

    @BindView(R.id.tv_nickname)
    TextView tv_nickname;

    @BindView(R.id.iv_user_pic)
    ImageView iv_user_pic;


    protected void initView() {
        View.OnClickListener onClickListener = v -> {
            switch (v.getId()) {
                case R.id.ll_user:
                    if (AppContext.self().getLocalUser() == null) {
                        EventBus.getDefault().post(new SEventRequestLogin());
                        return;
                    }
                    break;
                case R.id.sv_logout:
                    AppContext.self().logout();
                    break;
                case R.id.sv_unbind:
                    new AlertDialog.Builder(getContext()).setTitle("警告!").setNegativeButton("取消", null).setPositiveButton("确定", (dialog2, which2) -> {
                        UserService.unbindMail((code, msg, o) -> {
                            if (code == 0) {
                                AppContext.self().getLocalUser().setEmail("");
                                TaskExecutor.self().autoPost(() -> onEvent(new UEventRefreshLoginState().setLogin(AppContext.self().getLocalUser() != null)));
                            } else {
                                ToastManage.self().show(msg);
                            }
                        });
                    }).setMessage("是否确认解绑,解绑后无法登陆web端").show();
                    break;
            }
        };

        ll_user.setOnClickListener(onClickListener);
        sv_logout.setOnClickListener(onClickListener);
        sv_bind.setOnClickListener(new BindEmailView(getActivity()));
        sv_unbind.setOnClickListener(onClickListener);
        onEvent(new UEventRefreshLoginState().setLogin(AppContext.self().getLocalUser() != null));

        sv_allow_report_location.setOnValueChangeListener(new SetSwitchOnClickListener(CommonData.SDATA_ALLOW_REPORT_LOCATION));
        sv_allow_report_location.setChecked(SharedPreUtil.getBoolean(CommonData.SDATA_ALLOW_REPORT_LOCATION, true));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(UEventRefreshLoginState event) {
        if (event.isLogin()) {
            tv_nickname.setText(AppContext.self().getLocalUser().getNickname());
            ImageManage.self().loadImage(AppContext.self().getLocalUser().getUserPic(), iv_user_pic);
            if (CommonUtil.isNull(AppContext.self().getLocalUser().getEmail())) {
                sv_bind.setVisibility(VISIBLE);
                sv_unbind.setVisibility(GONE);
            } else {
                sv_unbind.setVisibility(VISIBLE);
                sv_bind.setVisibility(GONE);
                sv_unbind.setSummary(AppContext.self().getLocalUser().getEmail());
            }
        } else {
            sv_bind.setVisibility(GONE);
            sv_unbind.setVisibility(GONE);
            tv_nickname.setText("点击登录");
            iv_user_pic.setImageResource(R.drawable.theme_music_dcover);
        }
    }
}
