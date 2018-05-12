package com.wow.carlauncher.ex.plugin.obd;

import android.content.Context;

import com.wow.carlauncher.common.CommonData;
import com.wow.carlauncher.ex.manage.ble.BleManage;
import com.wow.carlauncher.ex.manage.time.event.MTimeSecondEvent;
import com.wow.carlauncher.ex.plugin.obd.evnet.PObdEventConnect;
import com.wow.frame.util.CommonUtil;
import com.wow.frame.util.SharedPreUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayDeque;
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

    public abstract void receiveMessage(byte[] message);

    public abstract UUID getNotifyService();

    public abstract UUID getNotifyCharacter();

    public abstract UUID getWriteService();

    public abstract UUID getWriteCharacter();

    public abstract void taskOver(ObdTask task);

    private ArrayDeque<ObdTask> taskArrayDeque;

    private ObdTask currentTask;


    public void addTask(ObdTask obdTask) {
        taskArrayDeque.add(obdTask);
        runTask();
    }

    private ObdTask getNextTask() {
        while (true) {
            if (taskArrayDeque.isEmpty()) {
                return null;
            }
            ObdTask obdTask = taskArrayDeque.pop();
            if (obdTask.getState() == ObdTask.STATE_WAIT_SEND) {
                return obdTask;
            }
        }
    }

    private void runTask() {
        if (currentTask == null && !taskArrayDeque.isEmpty()) {
            currentTask = getNextTask();
            if (currentTask != null) {
                currentTask.setState(ObdTask.STATE_WAIT_RECEIVE);
                listener.write(currentTask.getReqWarp());
            }
        }
    }

    public void destroy() {
        this.taskArrayDeque.clear();
        EventBus.getDefault().unregister(this);
    }

    protected boolean setTaskRes(String msg) {
        if (currentTask != null) {
            currentTask.writeRes(msg);
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
        for (ObdTask task : taskArrayDeque) {
            if (System.currentTimeMillis() - task.getCreateTime() > 2000) {
                task.setState(ObdTask.STATE_TIME_OUT);
            }
        }
        if (currentTask != null &&
                currentTask.getState() == ObdTask.STATE_WAIT_RECEIVE &&
                System.currentTimeMillis() - currentTask.getSendTime() > 2000) {
            setTaskRes("");
        }
    }
}
