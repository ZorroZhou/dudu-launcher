package com.wow.carlauncher.activity;

import android.widget.TextView;

import com.wow.carlauncher.R;
import com.wow.carlauncher.common.BaseActivity;

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
        setTitle("系统设置");
        about.setText("新功能:\n" +
                "1,增加了首页item背景设置,dock背景设置.\n" +
                "2,增加了悬浮窗是否忽略状态栏高度的设置,以及悬浮框大小调节.\n" +
                "3,对launcher的生命周期做了处理,防止出现一些不可预知的问题\n" +
                "4,处理了天气的bug,同时去掉定位,改为直接选择城市\n" +
                "下一版本的规划:\n" +
                "1,处理某些特殊车机的奇怪bug,比如dock图标无法显示\n" +
                "2,添加酷我音乐车机版本的插件支持\n" +
                "3,有条件的话,加入自动更新,以及一些其他的在线功能(PS,主要是资金紧张,没法租服务器,作者又只会用java,java没有免费空间)\n" +
                "4,尝试将巡航信息,路况信息,透出到桌面插件\n" +
                "5,继续尝试优驾插件的开发(作者就是用这个APP,挺好用的)\n" +
                "对于开源的问题,作者现阶段还没有这个想法,等过一阵APP版本稳定了再考虑\n" +
                "技术支持：QQ860913526.\n" +
                "QQ交流群：109799213.\n" +
                "如有问题请反馈至群或者qq邮箱");
    }
}
