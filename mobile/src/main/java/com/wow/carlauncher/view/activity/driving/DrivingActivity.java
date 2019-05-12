package com.wow.carlauncher.view.activity.driving;

import android.view.WindowManager;
import android.widget.FrameLayout;

import com.wow.carlauncher.R;
import com.wow.carlauncher.common.util.SharedPreUtil;
import com.wow.carlauncher.view.activity.set.event.SAEventRefreshDriving;
import com.wow.carlauncher.view.base.BaseActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;

import static com.wow.carlauncher.common.CommonData.SDATA_DRIVING_VIEW;

/**
 * Created by 10124 on 2018/4/25.
 */

public class DrivingActivity extends BaseActivity {
    private boolean isFront = false;

    @BindView(R.id.content)
    FrameLayout content;

    private DrivingView nowContent;

    @Override
    public void init() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContent(R.layout.activity_driving);
        EventBus.getDefault().register(this);
    }

    @Override
    public void initView() {
        hideTitle();
        loadView();
    }

    private void loadView() {
        refreshView();

    }

    @Override
    public void onResume() {
        super.onResume();
        isFront = true;
        if (nowContent != null) {
            nowContent.setFront(true);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        isFront = false;
        if (nowContent != null) {
            nowContent.setFront(false);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void refreshView() {
        nowContent = DrivingViewEnum.createView(this, DrivingViewEnum.getById(SharedPreUtil.getInteger(SDATA_DRIVING_VIEW, DrivingViewEnum.BLACK.getId())));
        nowContent.setFront(isFront);
        content.removeAllViews();
        content.addView(nowContent, FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        nowContent.findViewById(R.id.btn_back).setOnClickListener(v -> moveTaskToBack(isTaskRoot()));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(final SAEventRefreshDriving event) {
        refreshView();
    }
}
