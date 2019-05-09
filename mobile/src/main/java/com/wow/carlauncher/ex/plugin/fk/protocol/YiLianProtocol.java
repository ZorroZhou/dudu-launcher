package com.wow.carlauncher.ex.plugin.fk.protocol;

import android.content.Context;
import android.util.Log;

import com.wow.carlauncher.base.Shorts;
import com.wow.carlauncher.common.LogEx;
import com.wow.carlauncher.ex.plugin.fk.FKSimulateDoubleClick;
import com.wow.carlauncher.ex.plugin.fk.FangkongProtocol;
import com.wow.carlauncher.ex.plugin.fk.FangkongProtocolListener;

import java.util.UUID;

import static com.wow.carlauncher.common.CommonData.TAG;

/**
 * Created by 10124 on 2018/3/28.
 */

public class YiLianProtocol extends FangkongProtocol {
    public static final short LEFT_TOP_CLICK = -24316;//左上
    public static final short RIGHT_TOP_CLICK = -24319;//右上
    public static final short LEFT_BOTTOM_CLICK = -24312;//左下
    public static final short RIGHT_BOTTOM_CLICK = -24318;//右下
    public static final short CENTER_CLICK = -24304;//中间

    public static final short LEFT_TOP_DCLICK = -24316;//左上
    public static final short RIGHT_TOP_DCLICK = -24319;//右上
    public static final short LEFT_BOTTOM_DCLICK = -24312;//左下
    public static final short RIGHT_BOTTOM_DCLICK = -24318;//右下
    public static final short CENTER_DCLICK = -24304;//中间


    public static final short LEFT_TOP_LONG_CLICK = -23804;
    public static final short RIGHT_TOP_LONG_CLICK = -23807;
    public static final short LEFT_BOTTOM_LONG_CLICK = -23800;
    public static final short RIGHT_BOTTOM_LONG_CLICK = -23806;
    public static final short CENTER_LONG_CLICK = -23792;


    private FKSimulateDoubleClick<Short> doubleClick;

    public YiLianProtocol(String address, Context context, FangkongProtocolListener listener) {
        super(address, context, listener);
        doubleClick = new FKSimulateDoubleClick<>();

        Log.d(TAG, "yilian protocol init");
    }

    @Override
    public void receiveMessage(byte[] message) {
        if (message != null && message.length == 2) {
            short cmd = Shorts.fromByteArray(message);
            LogEx.d(this, "cmd:" + cmd);
            switch (cmd) {
                case LEFT_TOP_CLICK:
                    if (simulatedDClick) {
                        doubleClick.action(LEFT_TOP_CLICK,
                                () -> listener.onAction(LEFT_TOP_CLICK),
                                () -> listener.onAction(LEFT_TOP_DCLICK));
                    } else {
                        listener.onAction(LEFT_TOP_CLICK);
                    }
                    break;
                case RIGHT_TOP_CLICK:
                    if (simulatedDClick) {
                        doubleClick.action(RIGHT_TOP_CLICK,
                                () -> listener.onAction(RIGHT_TOP_CLICK),
                                () -> listener.onAction(RIGHT_TOP_DCLICK));
                    } else {
                        listener.onAction(RIGHT_TOP_CLICK);
                    }
                    break;
                case LEFT_BOTTOM_CLICK:
                    if (simulatedDClick) {
                        doubleClick.action(LEFT_BOTTOM_CLICK,
                                () -> listener.onAction(LEFT_BOTTOM_CLICK),
                                () -> listener.onAction(LEFT_BOTTOM_DCLICK));
                    } else {
                        listener.onAction(LEFT_BOTTOM_CLICK);
                    }
                    break;
                case RIGHT_BOTTOM_CLICK:
                    if (simulatedDClick) {
                        doubleClick.action(RIGHT_BOTTOM_CLICK,
                                () -> listener.onAction(RIGHT_BOTTOM_CLICK),
                                () -> listener.onAction(RIGHT_BOTTOM_DCLICK));
                    } else {
                        listener.onAction(RIGHT_BOTTOM_CLICK);
                    }
                    break;
                case CENTER_CLICK:
                    if (simulatedDClick) {
                        doubleClick.action(CENTER_CLICK,
                                () -> listener.onAction(CENTER_CLICK),
                                () -> listener.onAction(CENTER_DCLICK));
                    } else {
                        listener.onAction(CENTER_CLICK);
                    }
                    break;
                case LEFT_TOP_LONG_CLICK:
                    listener.onAction(LEFT_TOP_LONG_CLICK);
                    break;
                case RIGHT_TOP_LONG_CLICK:
                    listener.onAction(RIGHT_TOP_LONG_CLICK);
                    break;
                case LEFT_BOTTOM_LONG_CLICK:
                    listener.onAction(LEFT_BOTTOM_LONG_CLICK);
                    break;
                case RIGHT_BOTTOM_LONG_CLICK:
                    listener.onAction(RIGHT_BOTTOM_LONG_CLICK);
                    break;
                case CENTER_LONG_CLICK:
                    listener.onAction(CENTER_LONG_CLICK);
                    break;
            }
        }

    }

    public UUID getService() {
        return UUID.fromString("0000474d-0000-1000-8000-00805f9b34fb");
    }

    public UUID getCharacter() {
        return UUID.fromString("00004b59-0000-1000-8000-00805f9b34fb");
    }

    @Override
    public void destroy() {
        Log.d(TAG, "yilian protocol destroy");
    }
}
