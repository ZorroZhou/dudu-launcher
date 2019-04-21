package com.wow.carlauncher.ex.manage.musicCover;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.wow.carlauncher.R;
import com.wow.carlauncher.ex.plugin.music.event.PMusicEventInfo;
import com.wow.carlauncher.repertory.qqmusicService.QQMusicWebService;
import com.wow.carlauncher.repertory.qqmusicService.res.SearchRes;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.x;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashSet;
import java.util.Set;

import static com.wow.carlauncher.common.CommonData.TAG;

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

    public void init(Context context) {
        runkey = new HashSet<>();
        defcover = BitmapFactory.decodeResource(context.getResources(), R.mipmap.music_dlogo);
        EventBus.getDefault().register(this);
    }

    private Bitmap defcover;
    private Set<String> runkey;

    public void loadMusicCover(final String title, final String zuojia) {
        String name = nameGenerator.generate(title + "-" + zuojia) + ".temp";
        final MusicCoverRefresh musicCoverRefresh = new MusicCoverRefresh().setArtist(zuojia).setTitle(title);
        if (runkey.contains(name)) {
            return;
        }
        runkey.add(name);
        final File cover = new File(Environment.getExternalStorageDirectory() + File.separator + "music_cover" + File.separator + name);
        if (cover.exists()) {
            Log.e(TAG, "已有的封面");
            Bitmap bitmap = BitmapFactory.decodeFile(cover.getAbsolutePath());
            if (bitmap != null && bitmap.getWidth() > 0) {
                EventBus.getDefault().post(musicCoverRefresh.setCover(bitmap));
            } else {
                cover.delete();
                EventBus.getDefault().post(musicCoverRefresh.setCover(defcover));
            }
            runkey.remove(name);
        } else {
            Log.e(TAG, "新的封面");

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
                            Log.e(TAG, "加载封面:" + QQMusicWebService.picUrl(songItem.getAlbumid()));
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
                                        EventBus.getDefault().post(musicCoverRefresh.setCover(bitmap));
                                        FileOutputStream out = new FileOutputStream(cover);
                                        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
                                        success = true;
                                        Log.e(TAG, "封面下载成功");
                                    } else {
                                        Log.e(TAG, "封面下载失败");
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        runkey.remove(name);
                        if (!success) {
                            Log.e(TAG, "封面下载失败");
                            EventBus.getDefault().post(musicCoverRefresh.setCover(defcover));
                        }
                    });
                }
            });
        }
    }

    private String key = "";

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent(final PMusicEventInfo event) {
        String kk = event.getTitle() + event.getArtist();
        if (!key.equals(kk)) {
            key = kk;
            loadMusicCover(event.getTitle(), event.getArtist());
        }
    }
}
