package com.wow.carlauncher.view.activity.driving.blue1;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import com.wow.carlauncher.R;

/**
 * Created by 10124 on 2018/4/26.
 */

public class ProgressView1 extends View {
    private static final String TAG = "ProgressView1";

    private static final float paddingPercent = 0.098f;

    private float centerX = 0;
    private float centerY = 0;

    public ProgressView1(Context context) {
        super(context);
        init();

    }

    public ProgressView1(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private Paint arcPaint, shadowPaint, bgpaint;
    private float shadowRadius = 0;
    private RectF arcRect;
    private Bitmap bgBipmap;
    private Rect mSrcRect, mDestRect;
    private PaintFlagsDrawFilter paintFlagsDrawFilter;

    private void init() {
        //addContent(R.layout.content_driving_blue_speed);

        arcPaint = new Paint();
        arcPaint.setAntiAlias(true);//取消锯齿
        arcPaint.setStyle(Paint.Style.FILL);//设置画圆弧的画笔的属性为描边(空心)，个人喜欢叫它描边，叫空心有点会引起歧义
        arcPaint.setStrokeWidth(0);
        arcPaint.setColor(Color.RED);

        shadowPaint = new Paint();
        shadowPaint.setAntiAlias(true);

        bgpaint = new Paint();
        bgpaint.setAntiAlias(true);

        Resources res = getContext().getResources();
        bgBipmap = BitmapFactory.decodeResource(res, R.mipmap.dashbroad_disc_tick_mark_left_new);
        mSrcRect = new Rect(0, 0, bgBipmap.getWidth(), bgBipmap.getHeight());


        paintFlagsDrawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (centerX != 0 && centerY != 0) {
            canvas.setDrawFilter(paintFlagsDrawFilter);
            //绘制扇形
            canvas.drawArc(arcRect, 140, 260, true, arcPaint);
            //绘制阴影
            canvas.drawCircle(centerX, centerY, shadowRadius, shadowPaint);
            canvas.drawBitmap(bgBipmap, mSrcRect, mDestRect, bgpaint);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        float ncenterX = getMeasuredWidth() / 2;
        float ncenterY = getMeasuredHeight() / 2;

        if (centerX != ncenterX || centerY != ncenterY) {
            centerX = ncenterX;
            centerY = ncenterY;

            int paddingL = (int) (paddingPercent * getMeasuredWidth());
            int paddingT = (int) (paddingPercent * getMeasuredHeight());
            arcRect = new RectF(paddingL, paddingT, getMeasuredWidth() - paddingL, getMeasuredHeight() - paddingT);

            shadowRadius = getMeasuredWidth() * (1 - paddingPercent * 2) / 2;
            shadowPaint.setShader(new RadialGradient(centerX, centerY, shadowRadius, new int[]{
                    Color.BLACK, Color.BLACK, Color.TRANSPARENT}, null,
                    Shader.TileMode.REPEAT));

            mDestRect = new Rect(0, 0, getMeasuredWidth(), getMeasuredHeight());
        }
        this.invalidate();
    }
}
