package com.wow.carlauncher.common.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.RectF;
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
        paintFlagsDrawFilter = new PaintFlagsDrawFilter(0, Paint.FILTER_BITMAP_FLAG | Paint.ANTI_ALIAS_FLAG);
    }

    public void setCircular(boolean circular) {
        this.circular = circular;
        invalidate();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (width != getWidth() || height != getHeight()) {
            width = getWidth();
            height = getHeight();
            rtRectF = new RectF(width - rightTopRadius * 2, 0, width, rightTopRadius * 2);
            rbRectF = new RectF(width - rightBottomRadius * 2, height - rightBottomRadius * 2, width, height);
            lbRectF = new RectF(0, height - leftBottomRadius * 2, leftBottomRadius * 2, height);
            ltRectF = new RectF(0, 0, leftTopRadius * 2, leftTopRadius * 2);
        }
    }

    private PaintFlagsDrawFilter paintFlagsDrawFilter;
    private Path path;
    private Path pathCircle;
    private RectF rtRectF, rbRectF, ltRectF, lbRectF;

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        canvas.setDrawFilter(paintFlagsDrawFilter);
        if (circular) {
            pathCircle.reset();
            pathCircle.addCircle(width / 2.0f, height / 2.0f, width / 2, Path.Direction.CCW);
            canvas.clipPath(pathCircle);
        } else {
            path.reset();
            //四个角：右上，右下，左下，左上
            path.moveTo(leftTopRadius, 0);
            path.lineTo(width - rightTopRadius - leftTopRadius, 0);
            path.arcTo(rtRectF, 270, 90);
            path.lineTo(width, height - rightBottomRadius);
            path.arcTo(rbRectF, 0, 90);
            path.lineTo(leftBottomRadius, height);
            path.arcTo(lbRectF, 90, 90);

            path.lineTo(0, leftTopRadius);
            path.arcTo(ltRectF, 180, 90);

            canvas.clipPath(path);
        }
        super.onDraw(canvas);
        canvas.restore();
    }
}