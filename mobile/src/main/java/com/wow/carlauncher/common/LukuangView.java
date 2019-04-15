package com.wow.carlauncher.common;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class LukuangView extends View {
    public static final String TAG = "LukuangView";

    public LukuangView(Context context) {
        super(context);
        init();
    }

    public LukuangView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        lukuangs = new ArrayList<>();
    }

    private List<LukuangModel> lukuangs;

    public void setLukuangs(List<LukuangModel> lukuangs) {
        if (lukuangs == null) {
            return;
        }
        this.lukuangs.clear();
        this.total = 0;
        for (LukuangModel lukuang : lukuangs) {
            if (lukuang.status >= 0 && lukuang.status <= 4) {
                total = total + lukuang.distance;
                this.lukuangs.add(lukuang);
            }
        }
        Collections.sort(this.lukuangs, new Comparator<LukuangModel>() {
            @Override
            public int compare(LukuangModel o1, LukuangModel o2) {
                return o2.number - o1.number;
            }
        });
        invalidate();
    }

    private Paint mPaint;

    private int total;


    private int[] color = {
            Color.parseColor("#4cb6f6"),//蓝色
            Color.parseColor("#6ae128"), //绿色
            Color.parseColor("#fcfd12"), //黄色
            Color.parseColor("#ff7b69"), //浅红色
            Color.parseColor("#B22222"), //深红色
            Color.GRAY};

    @Override
    protected void onDraw(Canvas canvas) {
        if (lukuangs == null) {
            return;
        }
        int top = 0;
        for (LukuangModel lukuang : lukuangs) {
            if (lukuang.status < 0 || lukuang.status > 4) {
                lukuang.status = 5;
            }
            mPaint.setColor(color[lukuang.status]);
            int h = (int) (getHeight() * (lukuang.distance * 1f / total));
            int bottom = top + h;
            canvas.drawRect(0, top, getWidth(), bottom, mPaint);
            top = bottom;
        }
    }

    public static class LukuangModel {
        private int status;//每段柱状图信息 -1 无效, 0 无交通流(蓝色), 1 畅通（绿色）, 2 缓行（黄色）, 3 拥堵（红色）, 4 严重拥堵（深红色）, 10 行驶过的路段（灰色）
        private int number;//路况柱状图每段的编号，编号越小越靠近起点
        private int distance;// 路况柱状图每段的路程距离，单位米，所有段加起来的距离等于剩余总路程距离（每段柱状图的百分比为tmc_segment_distance除以residual_distance的值）

        public int getStatus() {
            return status;
        }

        public LukuangModel setStatus(int status) {
            this.status = status;
            return this;
        }

        public int getNumber() {
            return number;
        }

        public LukuangModel setNumber(int number) {
            this.number = number;
            return this;
        }

        public int getDistance() {
            return distance;
        }

        public LukuangModel setDistance(int distance) {
            this.distance = distance;
            return this;
        }

        @Override
        public String toString() {
            return "LukuangModel{" +
                    "status=" + status +
                    ", number=" + number +
                    ", distance=" + distance +
                    '}';
        }
    }
}
