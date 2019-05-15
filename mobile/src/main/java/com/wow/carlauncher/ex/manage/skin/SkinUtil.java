package com.wow.carlauncher.ex.manage.skin;

import android.view.Gravity;
import android.view.View;

import com.wow.carlauncher.R;

public class SkinUtil {
    public static int analysisItemTitleAlign(String align) {
        switch (align) {
            case SkinConstant.ItemTitleAlign
                    .left: {
                return Gravity.CENTER_VERTICAL | Gravity.LEFT;
            }
            case SkinConstant.ItemTitleAlign
                    .right: {
                return Gravity.CENTER_VERTICAL | Gravity.RIGHT;
            }
            default: {
                return Gravity.CENTER;
            }
        }
    }

    public static boolean analysisMusicCoverType(String type) {
        switch (type) {
            case SkinConstant.MusicCoverType
                    .rect: {
                return false;
            }
            default: {
                return true;
            }
        }
    }

    public static int analysisVisibility(String type) {
        switch (type) {
            case SkinConstant.Visibility.hide: {
                return View.GONE;
            }
            default: {
                return View.VISIBLE;
            }
        }
    }


    public static int analysisWeatherLayout(String layout) {
        switch (layout) {
            case SkinConstant.WeatherLayout.layout2: {
                return R.layout.content_l_weather_layout2;
            }
            default: {
                return R.layout.content_l_weather_layout1;
            }
        }
    }

    public static boolean analysisUseWallpaper(String type) {
        switch (type) {
            case SkinConstant.UseWallpaper
                    .use: {
                return true;
            }
            default: {
                return false;
            }
        }
    }

}
