package com.wow.carlauncher.view.activity.set.view;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.wow.carlauncher.R;
import com.wow.carlauncher.common.CommonData;
import com.wow.carlauncher.common.view.SetView;
import com.wow.carlauncher.view.activity.AboutActivity;
import com.wow.carlauncher.view.base.BaseActivity;
import com.wow.frame.repertory.remote.WebTask;
import com.wow.frame.util.SharedPreUtil;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;

import static com.wow.carlauncher.common.CommonData.TAG;

/**
 * Created by 10124 on 2018/4/22.
 */

public class SHelpView extends FrameLayout {

    public SHelpView(@NonNull Context context) {
        super(context);
        initView();
    }

    public SHelpView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    @ViewInject(R.id.sv_about)
    private SetView sv_about;

    @ViewInject(R.id.sv_money)
    private SetView sv_money;

    @ViewInject(R.id.sv_get_newest)
    private SetView sv_get_newest;

    @ViewInject(R.id.sv_get_debug_newest)
    private SetView sv_get_debug_newest;


    private WebTask<File> downloadUpdataTask;

    protected ProgressDialog progressDialog;
    protected BaseActivity.ProgressInterruptListener progressInterruptListener;

    private BaseActivity.ProgressInterruptListener downloadUpdataTaskCancel = new BaseActivity.ProgressInterruptListener() {
        @Override
        public void onProgressInterruptListener(ProgressDialog progressDialog) {
            if (downloadUpdataTask != null) {
                downloadUpdataTask.cancelTask();
            }
        }
    };

    private void initView() {
        progressDialog = new ProgressDialog(getContext()) {
            @Override
            public void onBackPressed() {
                super.onBackPressed();
                if (progressInterruptListener != null) {
                    progressInterruptListener.onProgressInterruptListener(progressDialog);
                }
            }
        };
        progressDialog.setCanceledOnTouchOutside(false);

        LinearLayout view = (LinearLayout) View.inflate(getContext(), R.layout.content_set_help, null);
        this.addView(view, FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);

        x.view().inject(this);

        sv_get_debug_newest.setOnValueChangeListener(new SetView.OnValueChangeListener() {
            @Override
            public void onValueChange(String newValue, String oldValue) {
                Log.e(TAG, "onValueChange: " + newValue);
                if ("1".equals(newValue)) {
                    SharedPreUtil.saveSharedPreBoolean(CommonData.SDATA_ALLOW_DEBUG_APP, true);
                } else {
                    SharedPreUtil.saveSharedPreBoolean(CommonData.SDATA_ALLOW_DEBUG_APP, false);
                }
            }
        });
        sv_get_debug_newest.setChecked(SharedPreUtil.getSharedPreBoolean(CommonData.SDATA_ALLOW_DEBUG_APP, false));

        sv_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivity(new Intent(getContext(), AboutActivity.class));
            }
        });

        sv_money.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView imageView = new ImageView(getContext());
                imageView.setImageResource(R.mipmap.money);
                AlertDialog dialog = new AlertDialog.Builder(getContext()).setView(imageView).setTitle("支持我吧!").setPositiveButton("确定", null).create();
                dialog.show();
            }
        });

    }


    private Activity getActivity() {
        return (Activity) getContext();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (downloadUpdataTask != null)
            downloadUpdataTask.cancelTask();
    }


    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    /**
     * * Checks if the app has permission to write to device storage
     * *
     * * If the app does not has permission then the user will be prompted to
     * * grant permissions
     * *
     * * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
        }
    }
}
