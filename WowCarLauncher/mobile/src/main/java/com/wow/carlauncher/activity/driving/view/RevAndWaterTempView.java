package com.wow.carlauncher.activity.driving.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.wow.carlauncher.R;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import static com.wow.carlauncher.common.CommonData.TAG;

/**
 * Created by 10124 on 2018/4/26.
 */

public class RevAndWaterTempView extends RelativeLayout {
    private final static int MAX_REV = 11000;

    @ViewInject(R.id.iv_cursor)
    private ImageView iv_cursor;

    private boolean show = false;

    private int currentValue = 0;
    private int tagerValue = 0;
    private int revChangeValue = 1;//转速变化的区间

    public RevAndWaterTempView(Context context) {
        super(context);
        initView();
    }

    public RevAndWaterTempView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }


    private void initView() {
        View amapView = View.inflate(getContext(), R.layout.content_rev_and_water_temp, null);
        this.addView(amapView, FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        x.view().inject(this);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        show = true;

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        iv_cursor.setPivotX(iv_cursor.getWidth() / 2);
        iv_cursor.setPivotY(iv_cursor.getHeight() / 2);//支点在图片中心
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        show = false;
    }

    public void setRev(int rev) {
        if (show) {
            if (rev > MAX_REV) {
                rev = MAX_REV;
            } else if (rev < 0) {
                rev = 0;
            }
            tagerValue = rev;
            revChangeValue = Math.abs(tagerValue - currentValue) / 100;
            if (revChangeValue < 1) {
                revChangeValue = 1;
            }
            postValue();
        }
    }

    private void postValue() {
        if (revChangeValue + currentValue < tagerValue) {
            currentValue = currentValue + revChangeValue;
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    iv_cursor.setRotation((float) (currentValue * 270) / (float) MAX_REV);
                    postValue();
                }
            }, 1);
        } else if (revChangeValue + currentValue > tagerValue) {
            currentValue = currentValue - revChangeValue;
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    iv_cursor.setRotation((float) (currentValue * 270) / (float) MAX_REV);
                    postValue();
                }
            }, 1);
        }
    }
}
