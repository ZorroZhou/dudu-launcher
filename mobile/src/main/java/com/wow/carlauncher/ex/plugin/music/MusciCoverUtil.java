package com.wow.carlauncher.ex.plugin.music;

import android.graphics.Bitmap;

import com.wow.carlauncher.ex.manage.ImageManage;
import com.wow.carlauncher.repertory.db.entiy.CoverTemp;
import com.wow.carlauncher.repertory.db.manage.DatabaseManage;
import com.wow.carlauncher.repertory.web.qqmusic.QQMusicWebService;
import com.wow.carlauncher.repertory.web.qqmusic.res.SearchRes;

import org.xutils.x;

public class MusciCoverUtil {
    public static boolean run = false;

    public static void loadCover(String title, String artist, MusicPlugin musicPlugin) {
        if (run) {
            return;
        }
        run = true;
        x.task().run(() -> {
            String key = title;
            if (artist != null) {
                key = key + artist;
            }
            String fkey = key;
            CoverTemp temp = DatabaseManage.getBean(CoverTemp.class, " key='" + fkey + "'");
            if (temp == null) {
                QQMusicWebService.searchMusic(key, 1, new QQMusicWebService.CommonCallback<SearchRes>() {
                    @Override
                    public void callback(final SearchRes res) {
                        if (res != null &&
                                res.getCode() == 0 &&
                                res.getData() != null &&
                                res.getData().getSong() != null &&
                                res.getData().getSong().getList() != null &&
                                res.getData().getSong().getList().size() > 0) {
                            SearchRes.SongItem songItem = res.getData().getSong().getList().get(0);
                            String url = QQMusicWebService.picUrl(songItem.getAlbumid());
                            musicPlugin.refreshCover(url);
                            DatabaseManage.insert(new CoverTemp().setKey(fkey).setUrl(url));
                        }
                        run = false;
                    }
                });
            } else {
                Bitmap cover = ImageManage.self().loadImageSync(temp.getUrl());
                if (cover == null || cover.getWidth() < 10) {
                    musicPlugin.refreshCover(null);
                    DatabaseManage.delete(CoverTemp.class, " key='" + fkey + "'");
                } else {
                    musicPlugin.refreshCover(temp.getUrl());
                }
                run = false;
            }
        });
    }
}
