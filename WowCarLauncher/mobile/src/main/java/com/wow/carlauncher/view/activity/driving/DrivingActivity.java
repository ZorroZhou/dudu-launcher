package com.wow.carlauncher.view.activity.driving;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.wow.carlauncher.R;
import com.wow.carlauncher.view.activity.driving.coolBlack.CoolBlackView;
import com.wow.carlauncher.view.base.BaseActivity;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import static com.wow.carlauncher.common.CommonData.TAG;

/**
 * Created by 10124 on 2018/4/25.
 */

public class DrivingActivity extends BaseActivity {

    @ViewInject(R.id.content)
    private FrameLayout content;

    @Override
    public void init() {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
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
                        .setTitle("带确定键的提示框")
                        .setMessage("确定吗")
                        .setPositiveButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                            }
                        })
                        .setNegativeButton("否", null)
                        .show();
                break;
            }
        }
    }
}
