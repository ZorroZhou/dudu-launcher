package com.wow.carlauncher.ex.manage;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.wow.carlauncher.common.CommonData;
import com.wow.carlauncher.repertory.qqmusicService.QQMusicWebService;
import com.wow.carlauncher.repertory.qqmusicService.res.SearchRes;

import org.xutils.x;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashSet;
import java.util.Set;

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


    private Md5FileNameGenerator nameGenerator = new Md5FileNameGenerator();

    public void init() {
        runkey = new HashSet<>();
    }

    private Set<String> runkey;

    public void loadMusicCover(final String title, final String zuojia, final Callback callback) {
        String name = nameGenerator.generate(title + "-" + zuojia) + ".temp";
        if (runkey.contains(name)) {
            return;
        }
        runkey.add(name);
        final File cover = new File(Environment.getExternalStorageDirectory() + File.separator + "music_cover" + File.separator + name);
        if (cover.exists()) {
            Log.e(CommonData.TAG, "已有的封面");
            Bitmap bitmap = BitmapFactory.decodeFile(cover.getAbsolutePath());
            if (bitmap != null && bitmap.getWidth() > 0) {
                callback.loadCover(true, title, zuojia, bitmap);
                Log.e(CommonData.TAG, "封面载入成功");
            } else {
                cover.delete();
                callback.loadCover(false, title, zuojia, null);
                Log.e(CommonData.TAG, "封面载入失败");
            }
            runkey.remove(name);
        } else {
            Log.e(CommonData.TAG, "新的封面");

            QQMusicWebService.searchMusic(title + " " + zuojia, 1, new QQMusicWebService.CommonCallback<SearchRes>() {
                @Override
                public void callback(final SearchRes res) {
                    x.task().run(() -> {
                        boolean success = false;
                        if (res != null &&
                                res.getCode() == 0 &&
                                res.getData() != null &&
                                res.getData().getSong() != null &&
                                res.getData().getSong().getList() != null &&
                                res.getData().getSong().getList().size() > 0) {
                            SearchRes.SongItem songItem = res.getData().getSong().getList().get(0);
                            Log.e(CommonData.TAG, "加载封面:" + QQMusicWebService.picUrl(songItem.getAlbumid()));
                            Bitmap bitmap = ImageLoader.getInstance().loadImageSync(QQMusicWebService.picUrl(songItem.getAlbumid()));
                            if (bitmap != null && bitmap.getWidth() > 0) {
                                try {
                                    File dir = new File(Environment.getExternalStorageDirectory() + File.separator + "music_cover" + File.separator);
                                    boolean direxist = true;
                                    if (!dir.exists()) {
                                        direxist = dir.mkdirs();
                                    }
                                    cover.delete();
                                    if (direxist && cover.createNewFile()) {
                                        callback.loadCover(true, title, zuojia, bitmap);
                                        FileOutputStream out = new FileOutputStream(cover);
                                        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
                                        success = true;
                                        Log.e(CommonData.TAG, "封面下载成功");
                                    } else {
                                        Log.e(CommonData.TAG, "封面下载失败");
                                    }
                                } catch (Exception e) {

                                }
                            }
                        }
                        if (!success) {
                            Log.e(CommonData.TAG, "封面下载失败");
                            callback.loadCover(false, title, zuojia, null);
                        }
                        runkey.remove(name);
                    });
                }
            });
        }
    }

    public interface Callback {
        void loadCover(boolean success, String title, String zuojia, Bitmap cover);
    }
}
