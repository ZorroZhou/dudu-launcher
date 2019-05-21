package com.wow.carlauncher.view.activity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
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
        PackageManager packageManager = getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        try {
            PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(), 0);
            String msg = "首先感谢大家的支持,作者时间有限,尽量满足大家的要求\n" +
                    "软件已经完全开源,源代码请到QQ交流群\n" +
                    "任何修改版,请保留以上信息,同时在下方添加修改说明!\n" +
                    "QQ交流群：109799213\n" +
                    "作者QQ:1012434956\n" +
                    "当前版本:" + packInfo.versionName;
            about.setText(msg);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
}
