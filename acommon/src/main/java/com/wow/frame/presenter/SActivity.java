package com.wow.frame.presenter;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import org.xutils.x;

/**
 * Created by 10124 on 2017/7/26.
 */

public abstract class SActivity extends AppCompatActivity {
    protected ProgressDialog progressDialog;

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initExecutor();
        init();
        x.view().inject(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        initView();
    }

    public abstract void initExecutor();

    public void init() {
    }

    public void initView() {
    }


}
