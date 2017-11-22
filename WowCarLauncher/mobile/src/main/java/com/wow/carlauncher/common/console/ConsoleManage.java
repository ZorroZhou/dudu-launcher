package com.wow.carlauncher.common.console;

import android.content.Context;

import com.wow.frame.util.SharedPreUtil;
import com.wow.carlauncher.common.console.impl.NwdConsoleImpl;
import com.wow.carlauncher.common.console.impl.SysConsoleImpl;

import static com.wow.carlauncher.common.CommonData.SDATA_CONSOLE_MARK;

/**
 * Created by 10124 on 2017/11/9.
 */

public class ConsoleManage extends IConsole {
    private static ConsoleManage self;

    public static ConsoleManage self() {
        if (self == null) {
            self = new ConsoleManage();
        }
        return self;
    }

    private ConsoleManage() {
    }

    public void init(Context context) {
        this.context = context;
        int mark = SharedPreUtil.getSharedPreInteger(SDATA_CONSOLE_MARK, SysConsoleImpl.MARK);
        switch (mark) {
            case SysConsoleImpl.MARK: {
                console = new SysConsoleImpl(context);
                break;
            }
            case NwdConsoleImpl.MARK: {
                console = new NwdConsoleImpl(context);
                break;
            }
            default: {
                console = new SysConsoleImpl(context);
            }
        }
    }

    private IConsole console;

    public void setConsole(IConsole console) {
        if (console instanceof SysConsoleImpl) {
            SharedPreUtil.saveSharedPreInteger(SDATA_CONSOLE_MARK, SysConsoleImpl.MARK);
        } else if (console instanceof NwdConsoleImpl) {
            SharedPreUtil.saveSharedPreInteger(SDATA_CONSOLE_MARK, NwdConsoleImpl.MARK);
        } else {
            return;
        }
        this.console = console;
    }

    @Override
    public void decVolume() {
        console.decVolume();
    }

    @Override
    public void incVolume() {
        console.incVolume();
    }

    @Override
    public void mute() {
        console.mute();
    }

    @Override
    public void clearTask() {
        console.clearTask();
    }
}
