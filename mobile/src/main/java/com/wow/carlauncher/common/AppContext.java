package com.wow.carlauncher.common;

import android.app.Application;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.wow.carlauncher.CarLauncherApplication;
import com.wow.carlauncher.ex.manage.MusicCoverManage;
import com.wow.carlauncher.ex.manage.ThemeManage;
import com.wow.carlauncher.ex.manage.appInfo.AppInfoManage;
import com.wow.carlauncher.ex.manage.ble.BleManage;
import com.wow.carlauncher.ex.manage.location.LocationManage;
import com.wow.carlauncher.ex.manage.time.TimeManage;
import com.wow.carlauncher.ex.manage.toast.ToastManage;
import com.wow.carlauncher.ex.plugin.amapcar.AMapCarPlugin;
import com.wow.carlauncher.ex.plugin.console.ConsolePlugin;
import com.wow.carlauncher.ex.plugin.fk.FangkongPlugin;
import com.wow.carlauncher.ex.plugin.music.MusicPlugin;
import com.wow.carlauncher.ex.plugin.obd.ObdPlugin;
import com.wow.carlauncher.repertory.db.AppDbInfo;
import com.wow.carlauncher.view.consoleWindow.ConsoleWin;
import com.wow.carlauncher.view.popupWindow.PopupWin;
import com.wow.frame.SFrame;
import com.wow.frame.declare.SAppDeclare;
import com.wow.frame.declare.SDatabaseDeclare;
import com.wow.frame.repertory.dbTool.DatabaseInfo;
import com.wow.frame.util.CommonUtil;
import com.wow.frame.util.SharedPreUtil;

import org.xutils.x;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.wow.carlauncher.common.CommonData.TAG;


/**
 * Created by liuyixian on 2017/9/20.
 */

public class AppContext implements SAppDeclare, SDatabaseDeclare {
    private static AppContext self;

    public synchronized static AppContext self() {
        if (self == null) {
            self = new AppContext();
        }
        return self;
    }

    private CarLauncherApplication application;

//    private BluetoothClient bluetoothClient;
//
//    public BluetoothClient getBluetoothClient() {
//        return bluetoothClient;
//    }

    private AppContext() {

    }

    public void init(CarLauncherApplication app) {
        this.application = app;
        SFrame.init(this);
        //图片加载工具
        initImage();
        //通知工具
        ToastManage.self().init(app);
        //时间管理器
        TimeManage.self().init(app);
        //蓝牙客户端
        BleManage.self().init(app);
        //定位管理器
        LocationManage.self().init(app);
        //封面加载器
        MusicCoverManage.self().init();
        //主题管理器
        ThemeManage.self().init(app);
        //app信息管理器
        AppInfoManage.self().init(app);
        //插件相关
        //高德地图插件
        AMapCarPlugin.self().init(app);
        //音乐插件
        MusicPlugin.self().init(app);
        //方控插件
        FangkongPlugin.self().init(app);
        //OBD插件
        ObdPlugin.self().init(app);
        //车机控制插件
        ConsolePlugin.self().init(app);
        //悬浮窗
        //弹出敞口
        PopupWin.self().init(app);
        //控制窗口
        ConsoleWin.self().init(app);

        int size = SharedPreUtil.getSharedPreInteger(CommonData.SDATA_POPUP_SIZE, 1);
        PopupWin.self().setRank(size + 1);
        handerException();

        x.task().run(new Runnable() {
            @Override
            public void run() {
                if (SharedPreUtil.getSharedPreBoolean(CommonData.SDATA_APP_AUTO_OPEN_USE, false)) {
                    Log.e(TAG, "开始唤醒其他APP");
                    if (CommonUtil.isNotNull(SharedPreUtil.getSharedPreString(CommonData.SDATA_APP_AUTO_OPEN1))) {
                        Log.e(TAG, "SDATA_APP_AUTO_OPEN1 " + SharedPreUtil.getSharedPreString(CommonData.SDATA_APP_AUTO_OPEN1));
                        AppInfoManage.self().openApp(SharedPreUtil.getSharedPreString(CommonData.SDATA_APP_AUTO_OPEN1));
                    }
                    if (CommonUtil.isNotNull(SharedPreUtil.getSharedPreString(CommonData.SDATA_APP_AUTO_OPEN2))) {
                        Log.e(TAG, "SDATA_APP_AUTO_OPEN2 " + SharedPreUtil.getSharedPreString(CommonData.SDATA_APP_AUTO_OPEN2));
                        AppInfoManage.self().openApp(SharedPreUtil.getSharedPreString(CommonData.SDATA_APP_AUTO_OPEN2));
                    }
                    if (CommonUtil.isNotNull(SharedPreUtil.getSharedPreString(CommonData.SDATA_APP_AUTO_OPEN3))) {
                        Log.e(TAG, "SDATA_APP_AUTO_OPEN3 " + SharedPreUtil.getSharedPreString(CommonData.SDATA_APP_AUTO_OPEN3));
                        AppInfoManage.self().openApp(SharedPreUtil.getSharedPreString(CommonData.SDATA_APP_AUTO_OPEN3));
                    }
                    if (CommonUtil.isNotNull(SharedPreUtil.getSharedPreString(CommonData.SDATA_APP_AUTO_OPEN4))) {
                        Log.e(TAG, "SDATA_APP_AUTO_OPEN4 " + SharedPreUtil.getSharedPreString(CommonData.SDATA_APP_AUTO_OPEN4));
                        AppInfoManage.self().openApp(SharedPreUtil.getSharedPreString(CommonData.SDATA_APP_AUTO_OPEN4));
                    }
                    Log.e(TAG, "延迟返回:" + SharedPreUtil.getSharedPreInteger(CommonData.SDATA_APP_AUTO_OPEN_BACK, 5) + "秒");
                    x.task().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Log.e(TAG, "back to desktop");
                            Intent home = new Intent(Intent.ACTION_MAIN);
                            home.addCategory(Intent.CATEGORY_HOME);
                            home.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            application.startActivity(home);
                        }
                    }, SharedPreUtil.getSharedPreInteger(CommonData.SDATA_APP_AUTO_OPEN_BACK, 5) * 1000);
                } else {
                    Log.e(TAG, "不唤醒其他APP");
                }
            }
        });
    }

    @Override
    public Application getApplication() {
        return this.application;
    }

    @Override
    public DatabaseInfo getDatabaseInfo() {
        return new AppDbInfo();
    }

    private void handerException() {
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                try {
                    String path;

                    if (Environment.getExternalStorageState().equals(
                            Environment.MEDIA_MOUNTED)) {// 优先保存到SD卡中
                        path = Environment.getExternalStorageDirectory()
                                .getAbsolutePath() + File.separator + "carLauncherError";
                    } else {// 如果SD卡不存在，就保存到本应用的目录下
                        path = getApplication().getFilesDir().getAbsolutePath()
                                + File.separator + "error";
                    }

                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault());
                    String date = format.format(new Date(System.currentTimeMillis()));

                    File file = new File(path, "log_"
                            + date + ".log");
                    if (!file.exists() && file.mkdirs() && file.createNewFile()) {
                        Log.e(TAG, "创建文件");
                    } else {
                        return;
                    }

                    PrintWriter pw = new PrintWriter(new FileWriter(file));
                    e.printStackTrace(pw);
                    pw.close();
                    System.exit(0);
                } catch (Exception ee) {
                    e.printStackTrace();
                }
            }
        });
    }


    private void initImage() {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this.application)
                .memoryCacheExtraOptions(480, 800)
                // 缓存在内存的图片的宽和高度
                // default = device screen dimensions
                .diskCacheExtraOptions(480, 800, null)
                .threadPoolSize(3)
                // 线程池内加载的数量
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .tasksProcessingOrder(QueueProcessingType.FIFO)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))//你可以通过自己的内存缓存实现
                .memoryCacheSize(3 * 1024 * 1024)// 缓存到内存的最大数据
                .memoryCacheSizePercentage(13)
                .diskCacheSize(50 * 1024 * 1024)// //缓存到文件的最大数据
                .diskCacheFileCount(99999)// 文件数量
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())//将保存的时候的URI名称用MD5 加密
                .imageDownloader(new BaseImageDownloader(this.application)) // default
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
                .writeDebugLogs()// Remove for release app
                .build();
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);// 初始化
    }
}
