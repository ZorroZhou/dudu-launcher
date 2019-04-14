package com.wow.carlauncher.view.activity;

import android.widget.TextView;

import com.wow.carlauncher.R;
import com.wow.carlauncher.view.base.BaseActivity;

import org.xutils.view.annotation.ViewInject;

/**
 * Created by 10124 on 2017/10/26.
 */

public class AboutActivity extends BaseActivity {
    @ViewInject(R.id.about)
    private TextView about;

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
                "1,重做首页,会比之前的好一些,同时添加昼夜支持.\n" +
                "2,增加首页导航支持的信息.\n" +
                "3,增加播放音乐的展示信息.\n" +
                "4.修复低版本的APP一直以来的天气不显示的问题\n" +
                "5,直接从git上改版了一个本地化的音乐播放器,做一个官方支持\n" +
                "下一版本的规划:\n" +
                "1,处理某些特殊车机的奇怪bug,比如dock图标无法显示\n" +
                "2,添加酷我音乐车机版本的插件支持\n" +
                "3,有条件的话,加入自动更新,以及一些其他的在线功能(PS,主要是资金紧张,没法租服务器,作者又只会用java,java没有免费空间)\n" +
                "4,尝试将巡航信息,路况信息,透出到桌面插件\n" +
                "5,继续尝试优驾插件的开发(作者就是用这个APP,挺好用的)\n" +
                "软件已经完全开源,源代码请到QQ交流群\n" +
                "QQ交流群：109799213";
        about.setText(msg);
    }
}
