package com.wow.carlauncher.view.activity;

import android.app.Activity;
import android.app.LauncherActivity;
import android.content.Intent;
import android.os.Bundle;

import com.wow.carlauncher.common.TaskExecutor;
import com.wow.carlauncher.common.util.AppUtil;

public class WelcomeActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TaskExecutor.self().post(() -> {
            if (AppUtil.isDefaultLauncher(getApplicationContext())) {
                Intent home = new Intent(Intent.ACTION_MAIN);
                home.addCategory(Intent.CATEGORY_HOME);
                home.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                startActivity(home);
            } else {
                Intent home = new Intent(getApplicationContext(), LauncherActivity.class);
                startActivity(home);
            }
        }, 1000);
        System.out.println("!!!!!!!!!!!");
    }
}
