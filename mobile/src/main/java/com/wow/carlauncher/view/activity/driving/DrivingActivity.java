package com.wow.carlauncher.view.activity.driving;

import android.view.WindowManager;
import android.widget.FrameLayout;

import com.wow.carlauncher.R;
import com.wow.carlauncher.common.util.SharedPreUtil;
import com.wow.carlauncher.view.base.BaseActivity;
import com.wow.carlauncher.view.base.BaseView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;

import static com.wow.carlauncher.common.CommonData.SDATA_DRIVING_VIEW;

/**
 * Created by 10124 on 2018/4/25.
 */

public class DrivingActivity extends BaseActivity {

    @BindView(R.id.content)
    FrameLayout content;

    private BaseView nowContent;

    @Override
    public void init() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContent(R.layout.activity_driving);
    }

    @Override
    public void initView() {
        hideTitle();
        nowContent = DrivingViewEnum.createView(this, DrivingViewEnum.getById(SharedPreUtil.getInteger(SDATA_DRIVING_VIEW, DrivingViewEnum.BLACK.getId())));
        content.removeAllViews();
        content.addView(nowContent, FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        nowContent.findViewById(R.id.btn_back).setOnClickListener(v -> finish());
    }
}
