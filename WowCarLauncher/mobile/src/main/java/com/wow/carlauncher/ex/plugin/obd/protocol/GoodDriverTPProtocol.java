package com.wow.carlauncher.ex.plugin.obd.protocol;

import android.content.Context;
import android.util.Log;

import com.wow.carlauncher.ex.plugin.obd.ObdProtocol;
import com.wow.carlauncher.ex.plugin.obd.ObdProtocolListener;
import com.wow.carlauncher.ex.plugin.obd.protocol.gd.CloseEchoTask;
import com.wow.carlauncher.ex.plugin.obd.protocol.gd.CloseLineFeedTask;
import com.wow.carlauncher.ex.plugin.obd.protocol.gd.CloseSpaceTask;
import com.wow.carlauncher.ex.plugin.obd.protocol.gd.CloseTitleTask;
import com.wow.carlauncher.ex.plugin.obd.protocol.gd.GetOilConTask;
import com.wow.carlauncher.ex.plugin.obd.protocol.gd.GetRevTask;
import com.wow.carlauncher.ex.plugin.obd.protocol.gd.GetSpeedTask;
import com.wow.carlauncher.ex.plugin.obd.protocol.gd.GetTpTask;
import com.wow.carlauncher.ex.plugin.obd.protocol.gd.GetWaterTempTask;
import com.wow.carlauncher.ex.plugin.obd.protocol.gd.ProtocolAutoTask;
import com.wow.carlauncher.ex.plugin.obd.ObdTask;

import java.util.UUID;

import static com.wow.carlauncher.common.CommonData.TAG;
import static com.wow.carlauncher.ex.plugin.obd.protocol.gd.GetTpTask.LB;
import static com.wow.carlauncher.ex.plugin.obd.protocol.gd.GetTpTask.LF;
import static com.wow.carlauncher.ex.plugin.obd.protocol.gd.GetTpTask.RB;
import static com.wow.carlauncher.ex.plugin.obd.protocol.gd.GetTpTask.RF;

/**
 * Created by 10124 on 2018/4/19.
 */

public class GoodDriverTPProtocol extends ObdProtocol {
    private boolean running = false;
    private StringBuffer resMessageTemp;

    public GoodDriverTPProtocol(Context context, String address, final ObdProtocolListener listener) {
        super(context, address, listener);

        //单独用来处理粘包的,太扯淡了
        this.resMessageTemp = new StringBuffer();
    }


    private long mark = 0L;
    private boolean reset = false;
    private int infoMark = 10;

    private boolean cmdCloseEcho = false;
    private boolean cmdCloseLineFeed = false;
    private boolean cmdCloseSpace = false;
    private boolean cmdCloseTitle = false;
    private boolean cmdProtocolAuto = false;

    @Override
    public void run() {
        resMessageTemp.setLength(0);
        cmdCloseEcho = false;
        cmdCloseLineFeed = false;
        cmdCloseSpace = false;
        cmdCloseTitle = false;
        cmdProtocolAuto = false;

        new Thread(new Runnable() {
            @Override
            public void run() {
                running = true;
                while (running) {
                    if (listener.isConnect()) {
                        baseCheckTask();

                        if (mark % infoMark == 0) {
                            addTask(new GetSpeedTask());
                            addTask(new GetRevTask());
                        }

                        if (mark % 20 == 0) {
                            addTask(new GetWaterTempTask());
                            addTask(new GetOilConTask());
                        }

                        if (mark % 60 == 0) {
                            addTask(new GetTpTask(LF));
                            addTask(new GetTpTask(RF));
                            addTask(new GetTpTask(LB));
                            addTask(new GetTpTask(RB));
                        }
                        mark++;
                    } else {
                        running = false;
                    }
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        Log.d(TAG, "!!!!!!!!!!!!!!!run: 开始运行");
    }

    private void baseCheckTask() {
        if (!cmdCloseEcho) {
            addTask(new CloseEchoTask());
        }

        if (!cmdCloseLineFeed) {
            addTask(new CloseLineFeedTask());
        }

        if (!cmdCloseSpace) {
            addTask(new CloseSpaceTask());
        }

        if (!cmdCloseTitle) {
            addTask(new CloseTitleTask());
        }

        if (!cmdProtocolAuto) {
            addTask(new ProtocolAutoTask());
        }
    }

    @Override
    public void stop() {
        running = false;
    }

    @Override
    public synchronized void receiveMessage(byte[] message) {
        resMessageTemp.append(new String(message));
        Log.d(TAG, "receiveMessage: " + resMessageTemp);
        if (resMessageTemp.indexOf(">") > -1) {
            //拿出来消息进行回掉
            setTaskRes(resMessageTemp.substring(0, resMessageTemp.indexOf(">")));
            //从缓存拿走消息
            resMessageTemp.delete(0, resMessageTemp.indexOf(">") + 1);
        }
    }

    @Override
    public void taskOver(ObdTask task) {
        if (task.isSuccess()) {
            if (task instanceof CloseSpaceTask) {
                cmdCloseSpace = true;
            } else if (task instanceof CloseLineFeedTask) {
                cmdCloseLineFeed = true;
            } else if (task instanceof CloseEchoTask) {
                cmdCloseEcho = true;
            } else if (task instanceof CloseTitleTask) {
                cmdCloseTitle = true;
            } else if (task instanceof ProtocolAutoTask) {
                cmdProtocolAuto = true;
            } else if (task.haveData()) {
                if (task instanceof GetSpeedTask) {
                    GetSpeedTask t = (GetSpeedTask) task;
                    listener.carRunningInfo(t.getSpeed(), null, null, null);
                } else if (task instanceof GetRevTask) {
                    GetRevTask t = (GetRevTask) task;
                    if (t.getRev() > 500) {
                        infoMark = 1;
                    } else {
                        infoMark = 10;
                    }
                    listener.carRunningInfo(null, t.getRev(), null, null);
                } else if (task instanceof GetWaterTempTask) {
                    GetWaterTempTask t = (GetWaterTempTask) task;
                    listener.carRunningInfo(null, null, t.getTemp(), null);
                } else if (task instanceof GetOilConTask) {
                    GetOilConTask t = (GetOilConTask) task;
                    listener.carRunningInfo(null, null, null, t.getOil());
                } else if (task instanceof GetTpTask) {
                    GetTpTask t = (GetTpTask) task;
                    if (t.getMark() == LF) {
                        listener.carTirePressureInfo(t.getTp(), t.getTemp(), null, null, null, null, null, null);
                    } else if (t.getMark() == RF) {
                        listener.carTirePressureInfo(null, null, t.getTp(), t.getTemp(), null, null, null, null);
                    } else if (t.getMark() == LB) {
                        listener.carTirePressureInfo(null, null, null, null, t.getTp(), t.getTemp(), null, null);
                    } else if (t.getMark() == RB) {
                        Log.d(TAG, "taskOver: !!!!!!!!!!!!!!!!!!" + t.getTp());
                        listener.carTirePressureInfo(null, null, null, null, null, null, t.getTp(), t.getTemp());
                    }
                }
            }
        }
    }

    public boolean supportTp() {
        return true;
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
