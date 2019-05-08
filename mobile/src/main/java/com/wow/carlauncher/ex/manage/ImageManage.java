package com.wow.carlauncher.ex.manage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.wow.carlauncher.common.LogEx;

public class ImageManage {
    private static class SingletonHolder {
        @SuppressLint("StaticFieldLeak")
        private static ImageManage instance = new ImageManage();
    }

    public static ImageManage self() {
        return ImageManage.SingletonHolder.instance;
    }

    private ImageManage() {
        super();
    }


    private DisplayImageOptions DEFAULT_DISPLAY_OPTION;

    public void init(Context context) {
        DEFAULT_DISPLAY_OPTION = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();


        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .memoryCacheExtraOptions(1000, 1000)
                // 缓存在内存的图片的宽和高度
                // default = device screen dimensions
                .diskCacheExtraOptions(1000, 1000, null)
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
                .imageDownloader(new BaseImageDownloader(context)) // default
                .defaultDisplayImageOptions(DEFAULT_DISPLAY_OPTION) // default
                .writeDebugLogs()// Remove for release app
                .build();
        ImageLoader.getInstance().init(config);// 初始化

        LogEx.e(this, "init ");
    }

    public DisplayImageOptions buildDisplayOption(int resId) {
        return new DisplayImageOptions.Builder()
                .cloneFrom(DEFAULT_DISPLAY_OPTION)
                .showImageForEmptyUri(resId)
                .showImageOnFail(resId).build();
    }

    public void loadImage(String url, ImageView imageView) {
        ImageLoader.getInstance().displayImage(url, imageView, DEFAULT_DISPLAY_OPTION);
    }

    public void loadImage(String url, ImageView imageView, int def) {
        ImageLoader.getInstance().displayImage(url, imageView, buildDisplayOption(def));
    }

    public Bitmap loadImageSync(String url) {
        return ImageLoader.getInstance().loadImageSync(url, DEFAULT_DISPLAY_OPTION);
    }
}
