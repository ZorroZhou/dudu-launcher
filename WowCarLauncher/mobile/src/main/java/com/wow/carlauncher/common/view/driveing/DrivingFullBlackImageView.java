package com.wow.carlauncher.common.view.driveing;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.comit.gooddriver.app.R.styleable;

public class DrivingFullBlackImageView
        extends ImageView {
    private static final int EXTRA_PADDING = 1;
    private final float MAX_DEGREES;
    private final float MAX_VALUE;
    private final float MIN_DEGREES;
    private final float MIN_VALUE;
    private float mDegrees = 0.0F;
    private Paint mPaint;
    private RectF mRectF;

    public DrivingFullBlackImageView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
        TypedArray localTypedArray = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.gooddriver);
        init(localTypedArray.getColor(R.styleable.gooddriver_paintColor, 0));
        localTypedArray.recycle();
        paramContext = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.CustomRotateImageView);
        this.MIN_VALUE = paramContext.getFloat(R.styleable.CustomRotateImageView_min_value, 0.0F);
        this.MAX_VALUE = paramContext.getFloat(R.styleable.CustomRotateImageView_max_value, 100.0F);
        this.MIN_DEGREES = paramContext.getFloat(R.styleable.CustomRotateImageView_min_degrees, 0.0F);
        this.MAX_DEGREES = paramContext.getFloat(R.styleable.CustomRotateImageView_max_degrees, 360.0F);
        this.mDegrees = this.MIN_DEGREES;
        float f = paramContext.getFloat(R.styleable.CustomRotateImageView_current_value, this.MIN_VALUE);
        paramContext.recycle();
        setValue(f);
    }

    public DrivingFullBlackImageView(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
        super(paramContext, paramAttributeSet, paramInt);
        TypedArray localTypedArray = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.gooddriver);
        init(localTypedArray.getColor(R.styleable.gooddriver_paintColor, 0));
        localTypedArray.recycle();
        paramContext = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.CustomRotateImageView);
        this.MIN_VALUE = paramContext.getFloat(R.styleable.CustomRotateImageView_min_value, 0.0F);
        this.MAX_VALUE = paramContext.getFloat(R.styleable.CustomRotateImageView_max_value, 100.0F);
        this.MIN_DEGREES = paramContext.getFloat(R.styleable.CustomRotateImageView_min_degrees, 0.0F);
        this.MAX_DEGREES = paramContext.getFloat(R.styleable.CustomRotateImageView_max_degrees, 360.0F);
        this.mDegrees = this.MIN_DEGREES;
        float f = paramContext.getFloat(R.styleable.CustomRotateImageView_current_value, this.MIN_VALUE);
        paramContext.recycle();
        setValue(f);
    }

    private float formatValue(float paramFloat) {
        float f1 = this.MAX_VALUE;
        float f2 = this.MIN_VALUE;
        if (this.MAX_VALUE < this.MIN_VALUE) {
            f1 = this.MIN_VALUE;
            f2 = this.MAX_VALUE;
        }
        if (paramFloat > f1) {
        }
        do {
            return f1;
            f1 = paramFloat;
        } while (paramFloat >= f2);
        return f2;
    }

    private float getDegrees(float paramFloat) {
        return (this.MAX_DEGREES - this.MIN_DEGREES) * (paramFloat - this.MIN_VALUE) / (this.MAX_VALUE - this.MIN_VALUE);
    }

    private void init(int paramInt) {
        this.mPaint = new Paint();
        this.mPaint.setFilterBitmap(true);
        this.mPaint.setAntiAlias(true);
        this.mPaint.setColor(paramInt);
        this.mPaint.setStyle(Paint.Style.FILL);
    }

    public float getMinValue() {
        return this.MIN_VALUE;
    }

    protected void onDraw(Canvas paramCanvas) {
        paramCanvas.drawArc(this.mRectF, this.MIN_DEGREES, this.mDegrees, true, this.mPaint);
        super.onDraw(paramCanvas);
    }

    protected void onSizeChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        super.onSizeChanged(paramInt1, paramInt2, paramInt3, paramInt4);
        if (this.mRectF == null) {
            this.mRectF = new RectF(0.0F, 0.0F, 0.0F, 0.0F);
        }
        if (paramInt1 >= paramInt2) {
            this.mRectF.set((paramInt1 - paramInt2) / 2.0F + 1.0F, 1.0F, paramInt1 - (paramInt1 - paramInt2) / 2.0F - 1.0F, paramInt2 - 1);
            return;
        }
        this.mRectF.set(1.0F, (paramInt2 - paramInt1) / 2.0F + 1.0F, paramInt1 - 1, paramInt2 - (paramInt2 - paramInt1) / 2.0F - 1.0F);
    }

    public void setColor(int paramInt) {
        if (this.mPaint.getColor() != paramInt) {
            this.mPaint.setColor(paramInt);
            invalidate();
        }
    }

    public void setValue(float paramFloat) {
        paramFloat = getDegrees(formatValue(paramFloat));
        if (paramFloat != this.mDegrees) {
            this.mDegrees = paramFloat;
            invalidate();
        }
    }
}