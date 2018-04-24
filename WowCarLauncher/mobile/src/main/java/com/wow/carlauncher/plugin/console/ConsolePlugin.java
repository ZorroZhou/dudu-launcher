package com.wow.carlauncher.plugin.console;

import android.content.Context;

import com.wow.carlauncher.plugin.BasePlugin;
import com.wow.carlauncher.plugin.console.event.PConsoleEventCallState;
import com.wow.carlauncher.plugin.console.event.PConsoleEventLightState;
import com.wow.frame.util.SharedPreUtil;
import com.wow.carlauncher.plugin.console.impl.NwdConsoleImpl;
import com.wow.carlauncher.plugin.console.impl.SysConsoleImpl;

import org.greenrobot.eventbus.EventBus;

import static com.wow.carlauncher.common.CommonData.SDATA_CONSOLE_MARK;

/**
 * Created by 10124 on 2017/11/9.
 */

public class ConsolePlugin extends BasePlugin<ConsoleProtoclListener> {
    private static ConsolePlugin self;

    public static ConsolePlugin self() {
        if (self == null) {
            self = new ConsolePlugin();
        }
        return self;
    }

    private ConsolePlugin() {
    }

    public void init(Context context) {
        super.init(context);
        loadConsole();
    }

    private ConsoleProtoclListener consoleListener = new ConsoleProtoclListener() {
        @Override
        public void callState(final boolean calling) {
            EventBus.getDefault().post(new PConsoleEventCallState().setCalling(calling));
        }

        @Override
        public void lightState(boolean lightState) {
            EventBus.getDefault().post(new PConsoleEventLightState().setOpen(lightState));
        }
    };


    private ConsoleProtocl console;

    public void loadConsole() {
        if (console != null) {
            console.destroy();
        }

        ConsoleProtoclEnum consoleProtoclEnum = ConsoleProtoclEnum.getById(SharedPreUtil.getSharedPreInteger(SDATA_CONSOLE_MARK, ConsoleProtoclEnum.SYSTEM.getId()));
        switch (consoleProtoclEnum) {
            case SYSTEM: {
                console = new SysConsoleImpl(getContext(), consoleListener);
                break;
            }
            case NWD: {
                console = new NwdConsoleImpl(getContext(), consoleListener);
                break;
            }
        }
    }

    public void decVolume() {
        console.decVolume();
    }

    public void incVolume() {
        console.incVolume();
    }

    public void mute() {
        console.mute();
    }

    public void clearTask() {
        console.clearTask();
    }

    public void callAnswer() {
        console.callAnswer();
    }

    public void callHangup() {
        console.callHangup();
    }
}
