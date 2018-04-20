package com.wow.carlauncher.plugin.console;

import android.content.Context;

import com.wow.carlauncher.plugin.BasePlugin;
import com.wow.carlauncher.plugin.fk.FangkongProtocolEnum;
import com.wow.frame.util.SharedPreUtil;
import com.wow.carlauncher.plugin.console.impl.NwdConsoleImpl;
import com.wow.carlauncher.plugin.console.impl.SysConsoleImpl;

import static com.wow.carlauncher.common.CommonData.SDATA_CONSOLE_MARK;
import static com.wow.carlauncher.common.CommonData.SDATA_FANGKONG_CONTROLLER;

/**
 * Created by 10124 on 2017/11/9.
 */

public class ConsolePlugin extends BasePlugin<ConsoleListener> {
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
        this.context = context;
        loadConsole();
    }

    private ConsoleListener consoleListener = new ConsoleListener() {
        @Override
        public void callState(final boolean calling) {
            runListener(new ListenerRuner<ConsoleListener>() {
                @Override
                public void run(ConsoleListener consoleListener) {
                    consoleListener.callState(calling);
                }
            });
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
