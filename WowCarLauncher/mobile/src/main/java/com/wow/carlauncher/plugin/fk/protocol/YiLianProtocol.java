package com.wow.carlauncher.plugin.fk.protocol;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.google.common.primitives.Shorts;
import com.wow.carlauncher.activity.LockActivity;
import com.wow.carlauncher.common.console.ConsoleManage;
import com.wow.carlauncher.plugin.fk.FangkongProtocol;
import com.wow.carlauncher.plugin.music.MusicPlugin;

import java.util.UUID;

/**
 * Created by 10124 on 2018/3/28.
 */

public class YiLianProtocol extends FangkongProtocol {
    private static final short BTN_LEFT_TOP_CLICK = -24316;//左上
    private static final short BTN_RIGHT_TOP_CLICK = -24319;//右上
    private static final short BTN_LEFT_BOTTOM_CLICK = -24312;//左下
    private static final short BTN_RIGHT_BOTTOM_CLICK = -24318;//右下
    private static final short BTN_CENTER_CLICK = -24304;//中间
    private static final short BTN_LEFT_TOP_LONG_CLICK = -23804;
    private static final short BTN_RIGHT_TOP_LONG_CLICK = -23807;
    private static final short BTN_LEFT_BOTTOM_LONG_CLICK = -23800;
    private static final short BTN_RIGHT_BOTTOM_LONG_CLICK = -23806;
    private static final short BTN_CENTER_LONG_CLICK = -23792;

    private int moshi = 1;

    private boolean lock = false;

    public YiLianProtocol(String address, Context context) {
        super(address, context);
    }

    @Override
    public void receiveMessage(byte[] message) {
        if (message != null && message.length == 2) {
            short cmd = Shorts.fromByteArray(message);
            if (moshi == 1) {
                switch (cmd) {
                    case BTN_LEFT_TOP_CLICK:
                        ConsoleManage.self().decVolume();
                        break;
                    case BTN_RIGHT_TOP_CLICK:
                        ConsoleManage.self().incVolume();
                        break;
                    case BTN_LEFT_BOTTOM_CLICK:
                        ConsoleManage.self().mute();
                        break;
                    case BTN_RIGHT_BOTTOM_CLICK:
                        moshi = 2;
                        Toast.makeText(context, "切换到模式2", Toast.LENGTH_LONG).show();
                        break;
                    case BTN_CENTER_CLICK:
                        Intent home = new Intent(Intent.ACTION_MAIN);
                        home.addCategory(Intent.CATEGORY_HOME);
                        home.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(home);
                        break;
                    case BTN_LEFT_TOP_LONG_CLICK:
                        MusicPlugin.self().pre();
                        break;
                    case BTN_RIGHT_TOP_LONG_CLICK:
                        MusicPlugin.self().next();
                        break;
                    case BTN_LEFT_BOTTOM_LONG_CLICK:
                        Intent intent = new Intent(context, LockActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                        break;
                    case BTN_RIGHT_BOTTOM_LONG_CLICK:
                        ConsoleManage.self().callHangup();
                        break;
                    case BTN_CENTER_LONG_CLICK:
                        break;
                }
            } else {
                switch (cmd) {
                    case BTN_LEFT_TOP_CLICK:
                        MusicPlugin.self().pre();
                        break;
                    case BTN_RIGHT_TOP_CLICK:
                        MusicPlugin.self().next();
                        break;
                    case BTN_LEFT_BOTTOM_CLICK:
                        ConsoleManage.self().mute();
                        break;
                    case BTN_RIGHT_BOTTOM_CLICK:
                        moshi = 1;
                        Toast.makeText(context, "切换到模式1", Toast.LENGTH_LONG).show();
                        break;
                    case BTN_CENTER_CLICK:
                        Intent home = new Intent(Intent.ACTION_MAIN);
                        home.addCategory(Intent.CATEGORY_HOME);
                        home.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(home);
                        break;
                    case BTN_LEFT_TOP_LONG_CLICK:
                        break;
                    case BTN_RIGHT_TOP_LONG_CLICK:
                        break;
                    case BTN_LEFT_BOTTOM_LONG_CLICK:
                        break;
                    case BTN_RIGHT_BOTTOM_LONG_CLICK:
                        break;
                    case BTN_CENTER_LONG_CLICK:
                        break;
                }
            }
        }
    }

    public UUID getService() {
        return UUID.fromString("0000474d-0000-1000-8000-00805f9b34fb");
    }

    public UUID getCharacter() {
        return UUID.fromString("00004b59-0000-1000-8000-00805f9b34fb");
    }
}
