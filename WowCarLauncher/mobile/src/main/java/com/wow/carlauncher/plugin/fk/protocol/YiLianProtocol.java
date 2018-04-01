package com.wow.carlauncher.plugin.fk.protocol;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.widget.Toast;

import com.google.common.primitives.Shorts;
import com.wow.carlauncher.common.console.ConsoleManage;
import com.wow.carlauncher.plugin.SimulateDoubleClickUtil;
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

    public static final String ACTION_BT_BEGIN_CALL_ONLINE = "com.bt.ACTION_BT_BEGIN_CALL_ONLINE";
    public static final String ACTION_BT_END_CALL = "com.bt.ACTION_BT_END_CALL";
    public static final String ACTION_BT_INCOMING_CALL = "com.bt.ACTION_BT_INCOMING_CALL";
    public static final String ACTION_BT_OUTGOING_CALL = "com.bt.ACTION_BT_OUTGOING_CALL";

    private int moshi = 1;

    //标记是否是在打电话
    private boolean isCalling = false;

    private SimulateDoubleClickUtil<Short> doubleClick;

    public YiLianProtocol(String address, Context context) {
        super(address, context);
        doubleClick = new SimulateDoubleClickUtil<>();


        IntentFilter localIntentFilter = new IntentFilter();
        localIntentFilter.addAction("com.bt.ACTION_BT_END_CALL");
        localIntentFilter.addAction("com.bt.ACTION_BT_BEGIN_CALL_ONLINE");
        localIntentFilter.addAction("com.bt.ACTION_BT_INCOMING_CALL");
        localIntentFilter.addAction("com.bt.ACTION_BT_OUTGOING_CALL");

        context.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (ACTION_BT_BEGIN_CALL_ONLINE.equals(intent.getAction())) {
                    isCalling = true;
                } else if (ACTION_BT_INCOMING_CALL.equals(intent.getAction())) {
                    isCalling = true;
                } else if (ACTION_BT_OUTGOING_CALL.equals(intent.getAction())) {
                    isCalling = true;
                } else if (ACTION_BT_END_CALL.equals(intent.getAction())) {
                    isCalling = false;
                }
            }
        }, localIntentFilter);
    }

    @Override
    public void receiveMessage(byte[] message) {
        if (message != null && message.length == 2) {
            short cmd = Shorts.fromByteArray(message);
            if (isCalling) {
                switch (cmd) {
                    case BTN_LEFT_TOP_CLICK:
                        ConsoleManage.self().decVolume();
                        break;
                    case BTN_RIGHT_TOP_CLICK:
                        ConsoleManage.self().incVolume();
                        break;
                    case BTN_LEFT_BOTTOM_CLICK:
                        ConsoleManage.self().callAnswer();
                        break;
                    case BTN_RIGHT_BOTTOM_CLICK:
                        ConsoleManage.self().callHangup();
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
                        ConsoleManage.self().callAnswer();
                        break;
                    case BTN_RIGHT_BOTTOM_LONG_CLICK:
                        ConsoleManage.self().callHangup();
                        break;
                    case BTN_CENTER_LONG_CLICK:
                        isCalling = false;
                        break;
                }
            } else {
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
                            setBtnRightBottomClick();
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
                            setBtnRightBottomClick();
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
    }

    private void setBtnRightBottomClick() {
        doubleClick.action(BTN_RIGHT_BOTTOM_CLICK, new Runnable() {
            @Override
            public void run() {
                modelSwitch(false);
            }
        }, new Runnable() {
            @Override
            public void run() {
                modelSwitch(true);
            }
        });
    }


    private int oldmodel = -1;

    private void modelSwitch(boolean dclick) {
        if (dclick) {
            if (moshi == 2 || moshi == 1) {
                oldmodel = moshi;
                moshi = 3;
            } else {
                moshi = oldmodel;
            }
        } else {
            if (moshi == 3) {
                moshi = oldmodel;
            } else {
                if (moshi == 2) {
                    moshi = 1;
                } else {
                    moshi = 2;
                }
            }
        }
        Toast.makeText(context, "切换到模式:" + moshi, Toast.LENGTH_SHORT).show();
    }

    public UUID getService() {
        return UUID.fromString("0000474d-0000-1000-8000-00805f9b34fb");
    }

    public UUID getCharacter() {
        return UUID.fromString("00004b59-0000-1000-8000-00805f9b34fb");
    }
}
