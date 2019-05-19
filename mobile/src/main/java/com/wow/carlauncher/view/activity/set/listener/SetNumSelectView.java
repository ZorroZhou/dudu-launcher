package com.wow.carlauncher.view.activity.set.listener;

import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.wow.carlauncher.R;
import com.wow.carlauncher.common.util.CommonUtil;
import com.wow.carlauncher.view.activity.set.SetActivity;
import com.wow.carlauncher.view.activity.set.SetBaseView;

public abstract class SetNumSelectView extends SetBaseView implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {
    private String title, suffix;
    private SeekBar seek_bar;
    private Integer max, min;
    private TextView tv_min, tv_max, tv_cur;

    public SetNumSelectView(SetActivity context, String title, String suffix, Integer min, Integer max) {
        super(context);
        this.title = title;
        this.suffix = suffix;
        this.max = max;
        this.min = min;
    }

    @Override
    public String getName() {
        return title;
    }

    @Override
    protected int getContent() {
        return R.layout.content_set_num_select;
    }

    @Override
    public boolean showRight() {
        return true;
    }

    @Override
    public String rightTitle() {
        return "保存";
    }

    @Override
    public boolean rightAction() {
        int ss = seek_bar.getProgress() + min;
        if (!CommonUtil.equals(ss, getCurr())) {
            onSelect(ss, ss + suffix);
        }
        return true;
    }

    @Override
    protected void initView() {
        this.tv_min = findViewById(R.id.min);
        this.tv_max = findViewById(R.id.max);
        this.tv_cur = findViewById(R.id.cur);
        this.seek_bar = findViewById(R.id.seek_bar);
        this.seek_bar.setOnSeekBarChangeListener(this);
    }

    public abstract Integer getCurr();

    public abstract void onSelect(Integer t, String msg);

    public boolean equals(Integer t1, Integer t2) {
        return CommonUtil.equals(t1, t2);
    }

    @Override
    public void onClick(View v) {
        int savecurr = getCurr();
        //这里换算表一下,防止出现错误的值

        int chazhi = max - min;
        if (chazhi < 1) {
            chazhi = 1;
        }

        int cur = savecurr - min;
        if (cur < 0) {
            cur = 0;
        } else if (cur > chazhi) {
            cur = chazhi;
        }
        String mmin = min + suffix;
        tv_min.setText(mmin);
        String mmax = (chazhi + min) + suffix;
        tv_max.setText(mmax);
        String mcur = savecurr + suffix;
        tv_cur.setText(mcur);

        seek_bar.setMax(chazhi);
        seek_bar.setProgress(cur);
        getActivity().addSetView(this);
        System.out.println(min + " " + max + " " + chazhi + " " + savecurr);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
            String ss = (seekBar.getProgress() + min) + suffix;
            tv_cur.setText(ss);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
