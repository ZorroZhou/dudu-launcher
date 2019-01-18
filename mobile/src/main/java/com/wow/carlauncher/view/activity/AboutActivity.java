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
        about.setText("新功能:\n" +
                "1,增加Poweramp的插件支持.\n" +
                "2,修复了qq音乐车机版插件的bug.\n" +
                "3,减少了一些会导致卡的代码\n" +
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
