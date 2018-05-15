package com.wow.carlauncher.ex.plugin.obd;

import android.content.Context;
import android.util.Log;

import com.wow.carlauncher.common.CommonData;
import com.wow.carlauncher.ex.manage.ble.BleManage;
import com.wow.carlauncher.ex.manage.time.event.MTimeSecondEvent;
import com.wow.carlauncher.ex.manage.toast.ToastManage;
import com.wow.carlauncher.ex.plugin.obd.evnet.PObdEventConnect;
import com.wow.frame.util.CommonUtil;
import com.wow.frame.util.DateUtil;
import com.wow.frame.util.SharedPreUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayDeque;
import java.util.Date;
import java.util.UUID;

import static com.inuker.bluetooth.library.Constants.STATUS_DEVICE_CONNECTED;

/**
 * Created by 10124 on 2018/3/29.
 */

public abstract class ObdProtocol {
    protected Context context;
    protected ObdProtocolListener listener;

    private String address;

    public String getAddress() {
        return address;
    }

    public ObdProtocol(Context context, String address, ObdProtocolListener listener) {
        this.address = address;
        this.context = context;
        this.listener = listener;

        this.taskArrayDeque = new ArrayDeque<>();
        EventBus.getDefault().register(this);
    }

    //是否支持胎压
    public abstract boolean supportTp();

    public abstract void run();

    public abstract void stop();

    public abstract void warpTimeOut();

    public abstract void receiveMessage(byte[] message);

    public abstract UUID getNotifyService();

    public abstract UUID getNotifyCharacter();

    public abstract UUID getWriteService();

    public abstract UUID getWriteCharacter();

    public abstract void taskOver(ObdTask task);

    private ArrayDeque<ObdTask> taskArrayDeque;

    private ObdTask currentTask;


    public void addTask(ObdTask obdTask) {
        ObdTask oldTask = null;
        for (ObdTask task : taskArrayDeque) {
            if (task.getReqMessage().equals(obdTask.getReqMessage())) {
                oldTask = task;
                break;
            }
        }
        if (oldTask != null) {
            Log.d("task send over3", "remove old task " + oldTask);
            taskArrayDeque.remove(oldTask);
        }
        taskArrayDeque.add(obdTask);
        runTask();
    }

    private void runTask() {
        if (currentTask == null && !taskArrayDeque.isEmpty()) {
            currentTask = taskArrayDeque.pop();
            currentTask.setSendTime(System.currentTimeMillis());
            listener.write(currentTask.getReqWarp());
        }
    }

    public void destroy() {
        this.taskArrayDeque.clear();
        EventBus.getDefault().unregister(this);
    }

    protected boolean setTaskRes(String msg) {
        if (currentTask != null) {
            currentTask.writeRes(msg);

            Log.d("task send over2", "sendTime:" + DateUtil.dateToString(new Date(currentTask.getSendTime()), "hh:ss:ss:SSSS") +
                    "  overTime:" + DateUtil.dateToString(new Date(System.currentTimeMillis()), "hh:ss:ss:SSSS"));
            Log.d("task send over3", "msg:" + msg);
            taskOver(currentTask);
            currentTask = null;
            runTask();
            return true;
        } else {
            return false;
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onEventAsync(final MTimeSecondEvent event) {
        if (currentTask != null && System.currentTimeMillis() - currentTask.getSendTime() > 2000) {
            ToastManage.self().show("传输包超时");
            setTaskRes("");
        }
    }
}
