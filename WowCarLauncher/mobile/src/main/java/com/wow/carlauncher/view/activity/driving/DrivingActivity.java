package com.wow.carlauncher.view.activity.driving;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.wow.carlauncher.R;
import com.wow.carlauncher.view.activity.driving.coolBlack.CoolBlackView;
import com.wow.carlauncher.view.base.BaseActivity;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by 10124 on 2018/4/25.
 */

public class DrivingActivity extends BaseActivity {
    public static boolean isDriving = false;


    @ViewInject(R.id.content)
    private FrameLayout content;

    @Override
    public void init() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContent(R.layout.activity_driving);
    }

    @Override
    public void initView() {
        hideTitle();
        View nowContent = new CoolBlackView(this);
        content.addView(nowContent, FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
    }

    @Event(value = {R.id.iv_back2})
    private void clickEvent(View view) {
        switch (view.getId()) {
            case R.id.iv_back2: {
                new AlertDialog.Builder(this)
                        .setTitle("提示")
                        .setMessage("确定推出驾驶界面吗?")
                        .setPositiveButton("退出", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
                break;
            }
        }
    }
}
