package com.wow.carlauncher.ex.manage;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.wow.carlauncher.common.CommonData;
import com.wow.musicapi.api.MusicApi;
import com.wow.musicapi.api.MusicApiFactory;
import com.wow.musicapi.api.MusicProvider;
import com.wow.musicapi.model.Song;

import org.xutils.x;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class MusicCoverManage {
    private static class SingletonHolder {
        @SuppressLint("StaticFieldLeak")
        private static MusicCoverManage instance = new MusicCoverManage();
    }

    public static MusicCoverManage self() {
        return MusicCoverManage.SingletonHolder.instance;
    }

    private MusicCoverManage() {
        super();
    }

    private MusicApi api;

    private MusicProvider musicProvider = MusicProvider.Xiami;


    private Md5FileNameGenerator nameGenerator = new Md5FileNameGenerator();

    public void init() {
        api = MusicApiFactory.create(musicProvider);
    }

    public void loadMusicCover(final String title, final String zuojia, final Callback callback) {
        String name = nameGenerator.generate(title + "-" + zuojia + "-" + musicProvider) + ".temp";
        final File cover = new File(Environment.getExternalStorageDirectory() + File.separator + "music_cover" + File.separator + name);
        if (cover.exists()) {
            Log.e(CommonData.TAG, "已有的封面");
            Bitmap bitmap = BitmapFactory.decodeFile(cover.getAbsolutePath());
            if (bitmap != null && bitmap.getWidth() > 0) {
                callback.loadCover(true, title, zuojia, bitmap);
            } else {
                callback.loadCover(false, title, zuojia, null);
            }
        } else {
            Log.e(CommonData.TAG, "新的封面");
            x.task().run(new Runnable() {
                @Override
                public void run() {
                    boolean success = false;
                    try {
                        final List<? extends Song> songs = api.searchMusicSync(title + " " + zuojia, 1, false);
                        if (songs.size() > 0) {
                            Bitmap bitmap = ImageLoader.getInstance().loadImageSync(songs.get(0).getPicUrl());
                            if (bitmap != null) {
                                new File(Environment.getExternalStorageDirectory() + File.separator + "music_cover" + File.separator).mkdirs();
                                cover.createNewFile();
                                callback.loadCover(true, title, zuojia, bitmap);
                                FileOutputStream out = new FileOutputStream(cover);
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
                                success = true;
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (!success) {
                        callback.loadCover(false, title, zuojia, null);
                    }
                }
            });
        }
    }

    public interface Callback {
        void loadCover(boolean success, String title, String zuojia, Bitmap cover);
    }
}
