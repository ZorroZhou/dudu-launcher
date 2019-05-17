package com.wow.carlauncher.ex.plugin.console;

import android.annotation.SuppressLint;
import android.content.Context;

import com.wow.carlauncher.common.LogEx;
import com.wow.carlauncher.common.util.SharedPreUtil;
import com.wow.carlauncher.ex.ContextEx;
import com.wow.carlauncher.ex.plugin.console.event.PConsoleEventCallState;
import com.wow.carlauncher.ex.plugin.console.event.PConsoleEventLightState;
import com.wow.carlauncher.ex.plugin.console.impl.NwdConsoleImpl;
import com.wow.carlauncher.ex.plugin.console.impl.SysConsoleImpl;

import static com.wow.carlauncher.common.CommonData.SDATA_CONSOLE_MARK;

/**
 * Created by 10124 on 2017/11/9.
 */

public class ConsolePlugin extends ContextEx {
    private static class SingletonHolder {
        @SuppressLint("StaticFieldLeak")
        private static ConsolePlugin instance = new ConsolePlugin();
    }

    public static ConsolePlugin self() {
        return ConsolePlugin.SingletonHolder.instance;
    }

    private ConsolePlugin() {
    }

    public void init(Context context) {
        long t1 = System.currentTimeMillis();
        setContext(context);
        loadConsole();
        LogEx.d(this, "init time:" + (System.currentTimeMillis() - t1));
    }

    private ConsoleProtoclListener consoleListener = new ConsoleProtoclListener() {
        @Override
        public void callState(final boolean calling) {
            postEvent(new PConsoleEventCallState().setCalling(calling));
        }

        @Override
        public void lightState(boolean lightState) {
            postEvent(new PConsoleEventLightState().setOpen(lightState));
        }
    };


    private ConsoleProtocl console;

    public void loadConsole() {
        if (console != null) {
            console.destroy();
        }

        ConsoleProtoclEnum consoleProtoclEnum = ConsoleProtoclEnum.getById(SharedPreUtil.getInteger(SDATA_CONSOLE_MARK, ConsoleProtoclEnum.SYSTEM.getId()));
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

        LogEx.d(this, "loadConsole:" + console);
    }

    public void decVolume() {
        LogEx.d(this, "decVolume");
        console.decVolume();
    }

    public void incVolume() {
        LogEx.d(this, "incVolume");
        console.incVolume();
    }

    public void mute() {
        LogEx.d(this, "mute");
        console.mute();
    }

    public void clearTask() {
        LogEx.d(this, "clearTask");
        console.clearTask();
    }

    public void callAnswer() {
        LogEx.d(this, "callAnswer");
        console.callAnswer();
    }

    public void callHangup() {
        LogEx.d(this, "callHangup");
        console.callHangup();
    }
}
