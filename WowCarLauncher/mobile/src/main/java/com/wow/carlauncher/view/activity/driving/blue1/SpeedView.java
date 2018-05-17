package com.wow.carlauncher.view.activity.driving.blue1;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
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

public class SpeedView extends BaseEBusView {
    private final static int MAX_REV = 11000;
    private final static int MAX_WATER_TEMP = 130;

    private boolean show = false;

    private int currentRev = 0;
    private int tagerRev = 0;
    private int revChange = 1;//转速变化的区间

    private float centerX = 0;
    private float centerY = 0;

    public SpeedView(Context context) {
        super(context);
        init();

    }

    public SpeedView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private Paint mPaint;

    private void init() {
        addContent(R.layout.content_driving_blue_speed);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        show = true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        centerX = getMeasuredWidth() / 2;
        centerY = getMeasuredHeight() / 2;
//        iv_cursor.setPivotX(getMeasuredWidth() / 2);
//        iv_cursor.setPivotY(getMeasuredHeight() / 2);//支点在图片中心
//
//        iv_water_temp.setPivotX(getMeasuredWidth() / 2);
//        iv_water_temp.setPivotY(getMeasuredHeight() / 2);//支点在图片中心
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        show = false;
    }

//    public void setWaterTemp(int waterTemp) {
//        if (show) {
//            if (waterTemp > MAX_WATER_TEMP) {
//                waterTemp = MAX_WATER_TEMP;
//            } else if (waterTemp < 0) {
//                waterTemp = 0;
//            }
//            iv_water_temp.setRotation(-(float) (waterTemp * 90) / (float) MAX_WATER_TEMP);
//
//            tv_wt.setText("水温:" + waterTemp + "℃");
//        }
//    }
//
//    public void setRev(int rev) {
//        if (show) {
//            tv_rev.setText(rev + "");
//
//            if (rev > MAX_REV) {
//                rev = MAX_REV;
//            } else if (rev < 0) {
//                rev = 0;
//            }
//            tagerRev = rev;
//            revChange = Math.abs(tagerRev - currentRev) / 100;
//            if (revChange < 1) {
//                revChange = 1;
//            }
//            postValue();
//        }
//    }
//
//    private void postValue() {
//        if (revChange + currentRev < tagerRev) {
//            currentRev = currentRev + revChange;
//            postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    iv_cursor.setRotation((float) (currentRev * 270) / (float) MAX_REV);
//                    postValue();
//                }
//            }, 1);
//        } else if (revChange + currentRev > tagerRev) {
//            currentRev = currentRev - revChange;
//            postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    iv_cursor.setRotation((float) (currentRev * 270) / (float) MAX_REV);
//                    postValue();
//                }
//            }, 1);
//        }
//    }
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onEventMainThread(PObdEventCarInfo event) {
//        if (event.getRev() != null) {
//            setRev(event.getRev());
//        }
//        if (event.getWaterTemp() != null) {
//            setWaterTemp(event.getWaterTemp());
//        }
//    }
}
