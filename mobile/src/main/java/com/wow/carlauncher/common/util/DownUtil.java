package com.wow.carlauncher.common.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;

import com.wow.carlauncher.common.TaskExecutor;
import com.wow.carlauncher.ex.manage.okHttp.ProgressResponseListener;
import com.wow.carlauncher.ex.manage.toast.ToastManage;
import com.wow.carlauncher.repertory.server.CommonService;
import com.wow.carlauncher.view.dialog.ProgressDialog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class DownUtil {
    public static void loadDownloadApk(Context context, String msg, String fileName, String url) {
        String path;
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {// 优先保存到SD卡中
            path = Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + File.separator + "duduLauncher" + File.separator + "down";
        } else {// 如果SD卡不存在，就保存到本应用的目录下
            path = context.getFilesDir().getAbsolutePath()
                    + File.separator + "duduLauncher" + File.separator + "down";
        }
        path = path + File.separator + System.currentTimeMillis();
        File pathFile = new File(path);
        boolean createFileRes = true;
        if (!pathFile.exists()) {
            createFileRes = pathFile.mkdirs();
        }
        if (!createFileRes) {
            ToastManage.self().show("无法创建缓存文件夹");
            return;
        }

        String filePath = path + File.separator + fileName;

        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setTitle(msg);
        Call call = CommonService.downFile(url, new ProgressResponseListener() {
            @Override
            public void onResponseProgress(long bytesRead, long contentLength, boolean done) {
                progressDialog.setProgress((float) ((double) bytesRead / (double) contentLength));
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                ToastManage.self().show("下载失败!");
                progressDialog.dismiss();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                int len;
                byte[] buf = new byte[2048];
                ResponseBody responseBody = response.body();
                if (responseBody != null) {
                    InputStream inputStream = responseBody.byteStream();
                    /**
                     * 写入本地文件
                     */
                    File file = new File(filePath);
                    if (file.exists()) {
                        if (file.delete()) {
                            ToastManage.self().show("无法删除历史文件");
                            progressDialog.dismiss();
                            return;
                        }
                    }
                    if (!file.createNewFile()) {
                        ToastManage.self().show("无法创建文件");
                        progressDialog.dismiss();
                        return;
                    }
                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                    while ((len = inputStream.read(buf)) != -1) {
                        fileOutputStream.write(buf, 0, len);
                    }
                    fileOutputStream.flush();
                    fileOutputStream.close();
                    inputStream.close();

                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        Uri value = FileProvider.getUriForFile(context, "com.satsoftec.risense.fileprovider", new File(filePath));
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        intent.setDataAndType(value,
                                "application/vnd.android.package-archive");
                    } else {
                        intent.setDataAndType(Uri.fromFile(new File(filePath)),
                                "application/vnd.android.package-archive");
                    }
                    context.startActivity(intent);
                } else {
                    ToastManage.self().show("下载失败!");
                }
                TaskExecutor.self().autoPost(progressDialog::dismiss);
            }
        });
        progressDialog.setOnDismissListener(dialog -> {
            if (!call.isCanceled()) {
                call.cancel();
            }
        });
        TaskExecutor.self().autoPost(progressDialog::show);
    }
}
