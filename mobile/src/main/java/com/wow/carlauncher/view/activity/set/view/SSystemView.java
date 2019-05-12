package com.wow.carlauncher.view.activity.set.view;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;

import com.wow.carlauncher.R;
import com.wow.carlauncher.common.CommonData;
import com.wow.carlauncher.common.LogEx;
import com.wow.carlauncher.common.TaskExecutor;
import com.wow.carlauncher.common.util.AppUtil;
import com.wow.carlauncher.common.util.SharedPreUtil;
import com.wow.carlauncher.common.view.SetView;
import com.wow.carlauncher.ex.manage.appInfo.AppInfoManage;
import com.wow.carlauncher.ex.manage.okHttp.ProgressResponseListener;
import com.wow.carlauncher.ex.manage.toast.ToastManage;
import com.wow.carlauncher.repertory.server.CommonService;
import com.wow.carlauncher.view.activity.AboutActivity;
import com.wow.carlauncher.view.activity.launcher.event.LDockLabelShowChangeEvent;
import com.wow.carlauncher.view.activity.set.SetActivity;
import com.wow.carlauncher.view.activity.set.SetBaseView;
import com.wow.carlauncher.view.activity.set.listener.SetAppMultipleSelectOnClickListener;
import com.wow.carlauncher.view.activity.set.listener.SetAppSingleSelectOnClickListener;
import com.wow.carlauncher.view.activity.set.listener.SetSwitchOnClickListener;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Response;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by 10124 on 2018/4/22.
 */

@SuppressLint("ViewConstructor")
public class SSystemView extends SetBaseView {
    public SSystemView(SetActivity activity) {
        super(activity);
    }

    @Override
    protected int getContent() {
        return R.layout.content_set_system;
    }

    @BindView(R.id.sv_sys_anquan)
    SetView sv_sys_anquan;

    @BindView(R.id.sv_sys_overlay)
    SetView sv_sys_overlay;

    @BindView(R.id.sv_sys_sdk)
    SetView sv_sys_sdk;

    @BindView(R.id.sv_about)
    SetView sv_about;

    @BindView(R.id.sv_apps_hides)
    SetView sv_apps_hides;

    @BindView(R.id.sv_launcher_show_dock_label)
    SetView sv_launcher_show_dock_label;

    @BindView(R.id.sv_key_listener)
    SetView sv_key_listener;

    @BindView(R.id.sv_open_log)
    SetView sv_open_log;

    @BindView(R.id.sv_update)
    SetView sv_update;

    private boolean showKey;
    private BroadcastReceiver nwdKeyTestReceiver = new BroadcastReceiver() {
        public void onReceive(Context paramContext, Intent paramIntent) {
            if ("com.nwd.action.ACTION_KEY_VALUE".equals(paramIntent.getAction())) {
                int key = paramIntent.getByteExtra("extra_key_value", (byte) 0);
                TaskExecutor.self().autoPost(new Runnable() {
                    @Override
                    public void run() {
                        ToastManage.self().show("NWD按键测试触发:" + key);
                    }
                });
            }
        }
    };

    @Override
    protected void initView() {
        sv_update.setOnClickListener(v -> {
            getActivity().showLoading("请求中");
            CommonService.getUpdate(2, (success, msg, appUpdate) -> {
                getActivity().hideLoading();
                if (success) {
                    if (AppUtil.getLocalVersion(getContext()) < appUpdate.getVersion()) {
                        TaskExecutor.self().autoPost(() -> new AlertDialog.Builder(getContext()).setTitle("发现新版本")
                                .setNegativeButton("忽略", null)
                                .setPositiveButton("下载新版本", (dialog12, which) -> {
                                    loadDownloadApk(appUpdate.getUrl(), appUpdate.getVersion());
                                }).setMessage(appUpdate.getAbout()).show());
                    } else {
                        ToastManage.self().show("没有新版本");
                    }
                }
            });
        });

        sv_open_log.setOnValueChangeListener(new SetSwitchOnClickListener(CommonData.SDATA_LOG_OPEN) {
            @Override
            public void newValue(boolean value) {
                SharedPreUtil.saveBoolean(CommonData.SDATA_LOG_OPEN, value);
                LogEx.setSaveFile(value);
            }
        });
        sv_open_log.setChecked(SharedPreUtil.getBoolean(CommonData.SDATA_LOG_OPEN, true));

        sv_key_listener.setOnClickListener(v -> {
            showKey = true;
            IntentFilter intentFilter2 = new IntentFilter();
            intentFilter2.addAction("com.nwd.action.ACTION_KEY_VALUE");
            getContext().registerReceiver(nwdKeyTestReceiver, intentFilter2);

            new AlertDialog.Builder(getContext()).setTitle("开启").setOnDismissListener(dialog -> {
                showKey = false;
                getContext().unregisterReceiver(nwdKeyTestReceiver);
            }).setPositiveButton("关闭", null).show();
        });

        sv_apps_hides.setOnClickListener(new SetAppMultipleSelectOnClickListener(getContext()) {
            @Override
            public String getCurr() {
                return SharedPreUtil.getString(CommonData.SDATA_HIDE_APPS);
            }

            @Override
            public void onSelect(String t) {
                SharedPreUtil.saveString(CommonData.SDATA_HIDE_APPS, t);
                AppInfoManage.self().refreshShowApp();
            }
        });

        sv_launcher_show_dock_label.setOnValueChangeListener(new SetSwitchOnClickListener(CommonData.SDATA_LAUNCHER_DOCK_LABEL_SHOW) {
            @Override
            public void newValue(boolean value) {
                EventBus.getDefault().post(new LDockLabelShowChangeEvent(value));
            }
        });
        sv_launcher_show_dock_label.setChecked(SharedPreUtil.getBoolean(CommonData.SDATA_LAUNCHER_DOCK_LABEL_SHOW, true));

        sv_sys_overlay.setOnClickListener(new SetAppSingleSelectOnClickListener(getContext()) {
            @Override
            public String getCurr() {
                return null;
            }

            @Override
            public void onSelect(String t) {
                if (Build.VERSION.SDK_INT >= 23) {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                            Uri.parse("package:" + t));
                    getActivity().startActivity(intent);
                } else {
                    ToastManage.self().show("这个功能是安卓6.0以上才有的");
                }
            }
        });

        sv_sys_anquan.setOnClickListener(v -> {
            Intent intent = new Intent(Settings.ACTION_SECURITY_SETTINGS);
            getActivity().startActivity(intent);
        });

        sv_sys_sdk.setOnClickListener(v -> ToastManage.self().show("当前SDK版本是" + Build.VERSION.SDK_INT));

        sv_about.setOnClickListener(v -> getActivity().startActivity(new Intent(getContext(), AboutActivity.class)));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (showKey) {
            ToastManage.self().show("系统按键触发 KEY_CODE:" + keyCode);
        }
        return super.onKeyDown(keyCode, event);
    }

    public void loadDownloadApk(String url, int version) {
        String[] fileName = url.split("/");
        final String filePath = Environment.getExternalStorageDirectory() + "/ddlauncher-V" + version + ".apk";
        initNotification();
        CommonService.downFile(url, new ProgressResponseListener() {
            @Override
            public void onResponseProgress(long bytesRead, long contentLength, boolean done) {
                System.out.println(bytesRead + "  " + contentLength + "  " + done);
                downloadResult(bytesRead / contentLength, filePath);
            }

            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("!!onFailure!");
            }

            @Override
            public void onResponse(Call call, @NonNull Response response) throws IOException {
//                response.body().byteStream()

                int len;
                byte[] buf = new byte[2048];
                InputStream inputStream = response.body().byteStream();
                /**
                 * 写入本地文件
                 */
                File file = new File(filePath);
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                while ((len = inputStream.read(buf)) != -1) {
                    fileOutputStream.write(buf, 0, len);
                }
                fileOutputStream.flush();
                fileOutputStream.close();
                inputStream.close();

                notificationManager.cancel(1);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Uri value = FileProvider.getUriForFile(getActivity(), "com.satsoftec.risense.fileprovider", new File(filePath));
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intent.setDataAndType(value,
                            "application/vnd.android.package-archive");
                } else {
                    intent.setDataAndType(Uri.fromFile(new File(filePath)),
                            "application/vnd.android.package-archive");
                }
                getActivity().startActivity(intent);
            }
        });
    }

    private Notification notification;
    private NotificationManager notificationManager;
    private NotificationCompat.Builder builder;

    private void initNotification() {
        String id = "my_channel_01";
        String name = "我是渠道名字";
        notificationManager = (NotificationManager) getActivity().getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(id, name, NotificationManager.IMPORTANCE_LOW);
            notificationManager.createNotificationChannel(mChannel);
            builder = new NotificationCompat.Builder(getActivity());
            builder.setContentText("下载进度:0%");
            builder.setContentTitle("正在更新...");
            //创建通知时指定channelID
            builder.setChannelId(id);
            builder.setSmallIcon(R.mipmap.ic_launcher);
            builder.setProgress(100, 0, false);
            notification = builder.build();

        } else {
            builder = new NotificationCompat.Builder(getActivity())
                    .setContentTitle("正在更新...")
                    .setContentText("下载进度:0%")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setProgress(100, 0, false)
                    .setOngoing(true);
            notification = builder.build();
        }

    }

    public void downloadResult(float progress, String path) {
        if (progress == -1) {
            return;
        }

        if (0 < progress && progress < 1) {
            builder.setProgress(100, (int) (progress * 100), false);
            builder.setContentText("下载进度:" + (int) (progress * 100) + "%");
            notification = builder.build();
            notificationManager.notify(1, notification);
        }
    }
}
