package com.wow.carlauncher.common.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Path;
import android.os.Build;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.View;

import com.wow.carlauncher.R;

public class CustomRoundAngleImageView extends AppCompatImageView {
    private float width, height;
    private int defaultRadius = 0;
    private int radius;
    private int leftTopRadius;
    private int rightTopRadius;
    private int rightBottomRadius;
    private int leftBottomRadius;
    private boolean circular;

    public CustomRoundAngleImageView(Context context) {
        this(context, null);
        init(context, null);
    }

    public CustomRoundAngleImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        init(context, attrs);
    }

    public CustomRoundAngleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @SuppressLint("CustomViewStyleable")
    private void init(Context context, AttributeSet attrs) {
        if (Build.VERSION.SDK_INT < 18) {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        // 读取配置
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.Custom_Round_Image_View);
        radius = array.getDimensionPixelOffset(R.styleable.Custom_Round_Image_View_radius, defaultRadius);
        leftTopRadius = array.getDimensionPixelOffset(R.styleable.Custom_Round_Image_View_left_top_radius, defaultRadius);
        rightTopRadius = array.getDimensionPixelOffset(R.styleable.Custom_Round_Image_View_right_top_radius, defaultRadius);
        rightBottomRadius = array.getDimensionPixelOffset(R.styleable.Custom_Round_Image_View_right_bottom_radius, defaultRadius);
        leftBottomRadius = array.getDimensionPixelOffset(R.styleable.Custom_Round_Image_View_left_bottom_radius, defaultRadius);

        //如果四个角的值没有设置，那么就使用通用的radius的值。
        if (defaultRadius == leftTopRadius) {
            leftTopRadius = radius;
        }
        if (defaultRadius == rightTopRadius) {
            rightTopRadius = radius;
        }
        if (defaultRadius == rightBottomRadius) {
            rightBottomRadius = radius;
        }
        if (defaultRadius == leftBottomRadius) {
            leftBottomRadius = radius;
        }
        array.recycle();
        circular = false;
        pathCircle = new Path();
        path = new Path();
    }

    public void setCircular(boolean circular) {
        this.circular = circular;
        invalidate();
    }

    public void setRightTopRadius(int rightTopRadius) {
        this.rightTopRadius = rightTopRadius;
        invalidate();
    }

    public void setLeftTopRadius(int leftTopRadius) {
        this.leftTopRadius = leftTopRadius;
        invalidate();
    }

    public void setRightBottomRadius(int rightBottomRadius) {
        this.rightBottomRadius = rightBottomRadius;
        invalidate();
    }

    public void setLeftBottomRadius(int leftBottomRadius) {
        this.leftBottomRadius = leftBottomRadius;
        invalidate();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        width = getWidth();
        height = getHeight();
    }

    private Path path;
    private Path pathCircle;

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        if (circular) {
            pathCircle.reset();
            pathCircle.addCircle(width / 2.0f, height / 2.0f, width / 2, Path.Direction.CCW);
            canvas.clipPath(pathCircle);
        } else {
            path.reset();
            //四个角：右上，右下，左下，左上
            path.moveTo(leftTopRadius, 0);
            path.lineTo(width - rightTopRadius, 0);
            path.quadTo(width, 0, width, rightTopRadius);

            path.lineTo(width, height - rightBottomRadius);
            path.quadTo(width, height, width - rightBottomRadius, height);

            path.lineTo(leftBottomRadius, height);
            path.quadTo(0, height, 0, height - leftBottomRadius);

            path.lineTo(0, leftTopRadius);
            path.quadTo(0, 0, leftTopRadius, 0);

            canvas.clipPath(path);
        }
        super.onDraw(canvas);
        canvas.restore();
    }
}