package com.wow.carlauncher.plugin.obd.protocol;

import android.content.Context;

import com.wow.carlauncher.plugin.obd.ObdPluginListener;
import com.wow.carlauncher.plugin.obd.ObdProtocol;
import com.wow.carlauncher.plugin.obd.protocol.gd.GetRevTask;
import com.wow.carlauncher.plugin.obd.protocol.gd.GetSpeedTask;
import com.wow.carlauncher.plugin.obd.protocol.gd.GetTpTask;
import com.wow.carlauncher.plugin.obd.task.ObdTask;
import com.wow.carlauncher.plugin.obd.task.ObdTaskManage;

import java.util.UUID;

import static com.wow.carlauncher.plugin.obd.protocol.gd.GetTpTask.LB;
import static com.wow.carlauncher.plugin.obd.protocol.gd.GetTpTask.LF;
import static com.wow.carlauncher.plugin.obd.protocol.gd.GetTpTask.RB;
import static com.wow.carlauncher.plugin.obd.protocol.gd.GetTpTask.RF;

/**
 * Created by 10124 on 2018/4/19.
 */

public class GoodDriverTPProtocol extends ObdProtocol {
    private boolean running = false;

    public GoodDriverTPProtocol(Context context, String address, final ObdPluginListener listener) {
        super(context, address, listener);

        obdTaskManage.init(new ObdTaskManage.TaskCallBack() {
            @Override
            public void write(byte[] msg) {
                GoodDriverTPProtocol.this.write(msg);
            }

            @Override
            public void taskOver(ObdTask task) {
                if (task.isSuccess()) {
                    if (task instanceof GetSpeedTask) {
                        GetSpeedTask t = (GetSpeedTask) task;
                        listener.carRunningInfo(t.getSpeed(), null, null, null, null);
                    } else if (task instanceof GetRevTask) {
                        GetRevTask t = (GetRevTask) task;
                        listener.carRunningInfo(null, t.getRev(), null, null, null);
                    } else if (task instanceof GetTpTask) {
                        GetTpTask t = (GetTpTask) task;
                        if (t.getMark() == LF) {
                            listener.carTirePressureInfo(t.getTp(), t.getTemp(), null, null, null, null, null, null);
                        } else if (t.getMark() == RF) {
                            listener.carTirePressureInfo(null, null, t.getTp(), t.getTemp(), null, null, null, null);
                        } else if (t.getMark() == LB) {
                            listener.carTirePressureInfo(null, null, null, null, t.getTp(), t.getTemp(), null, null);
                        } else if (t.getMark() == RB) {
                            listener.carTirePressureInfo(null, null, null, null, null, null, t.getTp(), t.getTemp());
                        }
                    }
                }
            }
        });
    }

    private long mark = 0L;

    @Override
    public void run() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (running) {
                    if (mark % 2 == 0) {
                        obdTaskManage.addTask(new GetSpeedTask());
                        obdTaskManage.addTask(new GetRevTask());
                    }

                    if (mark % 5 == 0) {
                        obdTaskManage.addTask(new GetTpTask(LF));
                        obdTaskManage.addTask(new GetTpTask(RF));
                        obdTaskManage.addTask(new GetTpTask(LB));
                        obdTaskManage.addTask(new GetTpTask(RB));
                    }
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    @Override
    public void stop() {
        running = false;
        super.stop();
    }

    @Override
    public void receiveMessage(byte[] message) {
        obdTaskManage.writeRes(message);
    }

    public void write(String message) {
        byte[] msg = (message + '\r').getBytes();
        super.write(msg);
    }

    @Override
    public UUID getNotifyService() {
        return UUID.fromString("0000FFE0-0000-1000-8000-00805F9B34FB");
    }

    @Override
    public UUID getNotifyCharacter() {
        return UUID.fromString("0000FFE1-0000-1000-8000-00805F9B34FB");
    }

    @Override
    public UUID getWriteService() {
        return UUID.fromString("0000FFE0-0000-1000-8000-00805F9B34FB");
    }

    @Override
    public UUID getWriteCharacter() {
        return UUID.fromString("0000FFE1-0000-1000-8000-00805F9B34FB");
    }
}
