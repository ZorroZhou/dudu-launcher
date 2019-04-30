package com.wow.carlauncher.ex.plugin.obd;

import android.content.Context;
import android.util.Log;

import com.wow.carlauncher.ex.manage.time.event.MTimeSecondEvent;
import com.wow.carlauncher.ex.manage.toast.ToastManage;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
        //出现了线程问题,暂时加上try catch
        try {
            ObdTask oldTask = null;
            List<ObdTask> tempList = new ArrayList<>(taskArrayDeque);
            for (ObdTask task : tempList) {
                if (task.getReqMessage().equals(obdTask.getReqMessage())) {
                    oldTask = task;
                    break;
                }
            }
            if (oldTask != null) {
                taskArrayDeque.remove(oldTask);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        taskArrayDeque.add(obdTask);
        runTask();
    }

    private void runTask() {
        if (currentTask == null && !taskArrayDeque.isEmpty()) {
            try {
                currentTask = taskArrayDeque.pop();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (currentTask != null) {
                currentTask.setSendTime(System.currentTimeMillis());
                listener.write(currentTask.getReqWarp());
            } else {
                runTask();
            }
        }
    }

    public void destroy() {
        this.taskArrayDeque.clear();
        EventBus.getDefault().unregister(this);
    }

    protected void setTaskRes(String msg) {
        if (currentTask != null) {
            currentTask.writeRes(msg);
            taskOver(currentTask);
            currentTask = null;
            runTask();
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent(final MTimeSecondEvent event) {
        if (currentTask != null && System.currentTimeMillis() - currentTask.getSendTime() > 5000) {
            ToastManage.self().show("传输包超时");
            currentTask = null;
            this.taskArrayDeque.clear();
            warpTimeOut();
        }
    }
}
