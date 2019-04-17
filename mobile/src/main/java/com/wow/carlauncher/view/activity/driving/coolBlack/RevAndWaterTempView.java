package com.wow.carlauncher.view.activity.driving.coolBlack;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import com.wow.carlauncher.R;
import com.wow.carlauncher.ex.plugin.obd.ObdPlugin;
import com.wow.carlauncher.ex.plugin.obd.evnet.PObdEventCarInfo;
import com.wow.carlauncher.view.base.BaseEBusView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by 10124 on 2018/4/26.
 */

public class RevAndWaterTempView extends BaseEBusView {
    private final static int MAX_REV = 11000;
    private final static int MAX_WATER_TEMP = 130;

    @ViewInject(R.id.iv_cursor)
    private ImageView iv_cursor;

    @ViewInject(R.id.iv_water_temp)
    private ImageView iv_water_temp;

    @ViewInject(R.id.tv_rev)
    private TextView tv_rev;

    @ViewInject(R.id.tv_wt)
    private TextView tv_wt;


    private boolean show = false;

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
    protected void initView(Bundle savedInstanceState) {
        onEventMainThread(ObdPlugin.self().getCurrentPObdEventCarInfo());
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        show = true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        iv_cursor.setPivotX(getMeasuredWidth() / 2);
        iv_cursor.setPivotY(getMeasuredHeight() / 2);//支点在图片中心

        iv_water_temp.setPivotX(getMeasuredWidth() / 2);
        iv_water_temp.setPivotY(getMeasuredHeight() / 2);//支点在图片中心
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        show = false;
    }

    public void setWaterTemp(int waterTemp) {
        if (show) {
            if (waterTemp > MAX_WATER_TEMP) {
                waterTemp = MAX_WATER_TEMP;
            } else if (waterTemp < 0) {
                waterTemp = 0;
            }
            iv_water_temp.setRotation(-(float) (waterTemp * 90) / (float) MAX_WATER_TEMP);

            tv_wt.setText("水温:" + waterTemp + "℃");
        }
    }

    public void setRev(int rev) {
        if (show) {
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
    }

    private void postValue() {
        if (revChange + currentRev < tagerRev) {
            currentRev = currentRev + revChange;
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    iv_cursor.setRotation((float) (currentRev * 270) / (float) MAX_REV);
                    postValue();
                }
            }, 1);
        } else if (revChange + currentRev > tagerRev) {
            currentRev = currentRev - revChange;
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    iv_cursor.setRotation((float) (currentRev * 270) / (float) MAX_REV);
                    postValue();
                }
            }, 1);
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onEventMainThread(final PObdEventCarInfo event) {
        post(new Runnable() {
            @Override
            public void run() {
                if (event.getRev() != null) {
                    setRev(event.getRev());
                }
                if (event.getWaterTemp() != null) {
                    setWaterTemp(event.getWaterTemp());
                }
            }
        });
    }
}
