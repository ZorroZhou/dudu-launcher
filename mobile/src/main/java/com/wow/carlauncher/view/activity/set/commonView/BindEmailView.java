package com.wow.carlauncher.view.activity.set.commonView;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.wow.carlauncher.R;
import com.wow.carlauncher.common.AppContext;
import com.wow.carlauncher.common.TaskExecutor;
import com.wow.carlauncher.common.user.event.UEventRefreshLoginState;
import com.wow.carlauncher.common.util.CommonUtil;
import com.wow.carlauncher.ex.manage.toast.ToastManage;
import com.wow.carlauncher.repertory.server.UserService;
import com.wow.carlauncher.view.activity.set.SetActivity;
import com.wow.carlauncher.view.activity.set.SetBaseView;

import org.greenrobot.eventbus.EventBus;

import java.util.concurrent.ScheduledFuture;

import butterknife.BindView;

public class BindEmailView extends SetBaseView implements View.OnClickListener {
    public BindEmailView(SetActivity activity) {
        super(activity);
    }

    @BindView(R.id.et_email)
    EditText et_email;

    @BindView(R.id.et_pass)
    EditText et_pass;

    @BindView(R.id.et_code)
    EditText et_code;

    @BindView(R.id.tv_send)
    TextView tv_send;

    @BindView(R.id.btn_bind)
    TextView btn_bind;


    private int time = 0;

    private ScheduledFuture scheduledFuture;

    @Override
    protected void initView() {
        tv_send.setOnClickListener(this);
        btn_bind.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_bind) {
            String code = et_code.getText().toString();
            String pass = et_pass.getText().toString();
            UserService.bindMail(code, pass, (code1, msg, o) -> {
                if (code1 == 0) {
                    AppContext.self().getLocalUser().setEmail(o);
                    EventBus.getDefault().post(new UEventRefreshLoginState().setLogin(true));
                    TaskExecutor.self().autoPost(() -> getActivity().backSetView());
                } else {
                    ToastManage.self().show(msg);
                }
            });
        } else if (v.getId() == R.id.tv_send) {
            getActivity().showLoading("请求中...");
            String email = et_email.getText().toString();
            if (CommonUtil.isNull(email)) {
                ToastManage.self().show("请输入邮箱");
                return;
            }
            UserService.sendMailCode(email, (code, msg, o) -> {
                getActivity().hideLoading();
                if (code != 0) {
                    ToastManage.self().show(msg);
                } else {
                    time = 60;

                    TaskExecutor.self().autoPost(() -> {
                        et_email.setEnabled(false);
                        tv_send.setText(String.valueOf(time + "秒后重试"));
                    });
                    scheduledFuture = TaskExecutor.self().repeatRun(() -> {
                        time--;
                        TaskExecutor.self().autoPost(() -> {
                            if (time == 0) {
                                tv_send.setText(String.valueOf("发送验证码"));
                                et_email.setEnabled(true);
                                scheduledFuture.cancel(true);
                                scheduledFuture = null;
                            } else {
                                tv_send.setText(String.valueOf(time + "秒后重试"));
                            }
                        });
                    }, 1000, 1000);
                }
            });
        } else {
            getActivity().addSetView(this);
        }

    }

    @Override
    public String getName() {
        return "绑定邮箱";
    }

    @Override
    protected int getContent() {
        return R.layout.content_set_bind_email;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (scheduledFuture != null) {
            scheduledFuture.cancel(true);
        }
    }
}
