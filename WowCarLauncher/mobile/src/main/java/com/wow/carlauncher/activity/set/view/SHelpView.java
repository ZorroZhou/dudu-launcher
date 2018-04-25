package com.wow.carlauncher.activity.set.view;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.wow.carAssistant.packet.response.common.GetAppUpdateRes;
import com.wow.carlauncher.R;
import com.wow.carlauncher.activity.AboutActivity;
import com.wow.carlauncher.common.base.BaseActivity;
import com.wow.carlauncher.common.base.BaseDialog;
import com.wow.carlauncher.common.CommonData;
import com.wow.carlauncher.common.ex.ToastManage;
import com.wow.carlauncher.common.view.SetView;
import com.wow.carlauncher.webservice.service.CommonService;
import com.wow.frame.repertory.remote.WebServiceManage;
import com.wow.frame.repertory.remote.WebTask;
import com.wow.frame.repertory.remote.callback.SCallBack;
import com.wow.frame.repertory.remote.callback.SProgressCallback;
import com.wow.frame.util.AndroidUtil;
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

        sv_get_newest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyStoragePermissions(getActivity());
                WebServiceManage.getService(CommonService.class).checkUpdate(SharedPreUtil.getSharedPreBoolean(CommonData.SDATA_ALLOW_DEBUG_APP, false)).setCallback(new SCallBack<GetAppUpdateRes>() {
                    @Override
                    public void callback(boolean isok, String msg, final GetAppUpdateRes res) {
                        if (isok) {
                            final String version = AndroidUtil.getAppVersionCode(getContext());
                            if (TextUtils.isEmpty(version)) {
                                ToastManage.self().show("获取当前版本号失败");
                                return;
                            }
                            if (res.getVersionCode() == null) {
                                ToastManage.self().show("没有新版本发布");
                                return;
                            }
                            boolean havenew;
                            Log.e(TAG, "callback!!!!!!!!!!!!!!!: " + SharedPreUtil.getSharedPreBoolean(CommonData.SDATA_ALLOW_DEBUG_APP, false));
                            Log.e(TAG, "callback!!!!!!!!!!!!!!!: " + (res.getVersionCode() >= Integer.parseInt(version)));
                            if (SharedPreUtil.getSharedPreBoolean(CommonData.SDATA_ALLOW_DEBUG_APP, false)) {
                                havenew = res.getVersionCode() >= Integer.parseInt(version);
                            } else {
                                havenew = res.getVersionCode() > Integer.parseInt(version);
                            }

                            if (havenew) {
                                BaseDialog baseDialog = new BaseDialog(getContext());
                                baseDialog.setGravityCenter();
                                baseDialog.setTitle("版本更新");
                                baseDialog.setMessage("最新版本为" + res.getVersionName() + ",是否更新?\n" + res.getNewMessage());
                                baseDialog.setBtn1("下载", new BaseDialog.OnBtnClickListener() {
                                    @Override
                                    public boolean onClick(BaseDialog dialog) {
                                        dialog.dismiss();
                                        final String savePath = Environment.getExternalStorageDirectory() + "/" + res.getVersionName() + ".apk";
                                        downloadUpdataTask = WebServiceManage.getService(CommonService.class).getAppUpdateFile(savePath, SharedPreUtil.getSharedPreBoolean(CommonData.SDATA_ALLOW_DEBUG_APP, false));
                                        showLoading("开始下载!", downloadUpdataTaskCancel);
                                        downloadUpdataTask.setCallback(new SProgressCallback<File>() {
                                            @Override
                                            public void onProgress(float progress) {
                                                if (progress > 0 && progress < 1)
                                                    downloadResult(progress, savePath, null);
                                            }

                                            @Override
                                            public void callback(boolean isok, String msg, File res) {
                                                if (isok) {
                                                    downloadResult(1f, savePath, savePath);
                                                } else {
                                                    downloadResult(-1f, savePath, msg);
                                                }
                                            }
                                        });
                                        return true;
                                    }
                                });
                                baseDialog.setBtn2("取消", null);
                                baseDialog.show();
                            } else {
                                ToastManage.self().show("已是最新版本");
                            }

                        } else {
                            ToastManage.self().show("错误:" + msg);
                        }
                    }
                });
            }
        });
    }

    public void downloadResult(float progress, String path, String msg) {
        if (progress == -1) {
            ToastManage.self().show(msg);
            return;
        }
        if (progress >= 0 && progress < 1) {
            showLoading("已经下载:" + (int) (progress * 100) + "%", downloadUpdataTaskCancel);
        }
        if (progress == 1) {
            ToastManage.self().show("下载成功");
            downloadUpdataTask = null;
            hideLoading();
            Intent intent = new Intent();
            //执行动作
            intent.setAction(Intent.ACTION_VIEW);
            //执行的数据类型
            Uri data = Uri.fromFile(new File(path + ".tmp"));
            intent.setDataAndType(data, "application/vnd.android.package-archive");
            getActivity().startActivity(intent);
        }
    }


    public void showLoading(final String msg, @Nullable final BaseActivity.ProgressInterruptListener progressInterruptListener) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                x.task().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (showLoading) {
                            return;
                        }
                        if (progressDialog != null && !getActivity().isFinishing() && !showLoading) {
                            progressDialog.setMessage(msg);
                            progressDialog.show();
                            showLoading = true;
                        }
                    }
                }, 100);
            }
        });
    }


    public synchronized void hideLoading() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                x.task().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (!showLoading) {
                            return;
                        }
                        if (progressDialog != null && showLoading) {
                            progressDialog.hide();
                            showLoading = false;
                        }
                    }
                }, 100);
            }
        });
    }

    private Boolean showLoading = false;

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
