package com.wow.carlauncher.view.activity.driving.coolBlack;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import com.wow.carlauncher.R;
import com.wow.carlauncher.common.TaskExecutor;
import com.wow.carlauncher.ex.plugin.obd.ObdPlugin;
import com.wow.carlauncher.ex.plugin.obd.evnet.PObdEventCarInfo;
import com.wow.carlauncher.view.base.BaseView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;

/**
 * Created by 10124 on 2018/4/26.
 */

public class RevAndWaterTempView extends BaseView {
    private final static int MAX_REV = 11000;
    private final static int MAX_WATER_TEMP = 130;

    @BindView(R.id.iv_cursor)
    ImageView iv_cursor;

    @BindView(R.id.iv_water_temp)
    ImageView iv_water_temp;

    @BindView(R.id.tv_rev)
    TextView tv_rev;

    @BindView(R.id.tv_wt)
    TextView tv_wt;

    private int currentRev = 0;
    private int tagerRev = 0;
    private int revChange = 1;//转速变化的区间

    public RevAndWaterTempView(Context context) {
        super(context);

    }

    public RevAndWaterTempView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected int getContent() {
        return R.layout.content_driving_cool_rev_and_water_temp;
    }

    @Override
    protected void initView() {
        //同步一下信息
        TaskExecutor.self().post(() -> {
            onEvent(ObdPlugin.self().getCurrentPObdEventCarInfo());
        }, 500);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        iv_cursor.setPivotX(getMeasuredWidth() / 2);
        iv_cursor.setPivotY(getMeasuredHeight() / 2);//支点在图片中心

        iv_water_temp.setPivotX(getMeasuredWidth() / 2);
        iv_water_temp.setPivotY(getMeasuredHeight() / 2);//支点在图片中心
    }

    public void setWaterTemp(int waterTemp) {
        if (waterTemp > MAX_WATER_TEMP) {
            waterTemp = MAX_WATER_TEMP;
        } else if (waterTemp < 0) {
            waterTemp = 0;
        }
        iv_water_temp.setRotation(-(float) (waterTemp * 90) / (float) MAX_WATER_TEMP);

        tv_wt.setText("水温:" + waterTemp + "℃");
    }

    public void setRev(int rev) {
        tv_rev.setText(rev + "");

        if (rev > MAX_REV) {
            rev = MAX_REV;
        } else if (rev < 0) {
            rev = 0;
        }
        tagerRev = rev;
        revChange = Math.abs(tagerRev - currentRev) / 100;
        if (revChange < 1) {
            revChange = 1;
        }
        postValue();
    }

    private void postValue() {
        if (revChange + currentRev < tagerRev) {
            currentRev = currentRev + revChange;
            postDelayed(() -> {
                iv_cursor.setRotation((float) (currentRev * 270) / (float) MAX_REV);
                postValue();
            }, 1);
        } else if (revChange + currentRev > tagerRev) {
            currentRev = currentRev - revChange;
            postDelayed(() -> {
                iv_cursor.setRotation((float) (currentRev * 270) / (float) MAX_REV);
                postValue();
            }, 1);
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onEvent(final PObdEventCarInfo event) {
        post(() -> {
            if (event.getRev() != null) {
                setRev(event.getRev());
            }
            if (event.getWaterTemp() != null) {
                setWaterTemp(event.getWaterTemp());
            }
        });
    }
}
