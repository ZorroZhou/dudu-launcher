package com.wow.carlauncher.view.activity;

import android.widget.TextView;

import com.wow.carlauncher.R;
import com.wow.carlauncher.view.base.BaseActivity;

import butterknife.BindView;

/**
 * Created by 10124 on 2017/10/26.
 */

public class AboutActivity extends BaseActivity {
    @BindView(R.id.about)
    TextView about;

    @Override
    public void init() {
        setContent(R.layout.activity_about);
    }

    @Override
    public void initView() {
        setTitle("关于我们");
        String msg = "首先感谢大家的支持,我想说的是,诈尸更新了!\n" +
                "作者时间有限,尽量满足大家的要求\n" +
                "新功能:\n" +
                "1,三个版本主题支持.\n" +
                "2,首页导航更多信息展示.\n" +
                "3,QQ音乐封面和进度,其他音乐的封面支持.\n" +
                "4.bug修复\n" +
                "软件已经完全开源,源代码请到QQ交流群\n" +
                "任何修改版,请保留以上信息,同时在下方添加修改说明!\n" +
                "QQ交流群：109799213\n" +
                "作者QQ:1012434956";
        about.setText(msg);
    }
}
