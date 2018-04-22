package com.wow.carlauncher.plugin.obd;

import android.content.Context;

import java.util.ArrayDeque;
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

    private void runTask() {
        if (currentTask == null && !taskArrayDeque.isEmpty()) {
            currentTask = taskArrayDeque.pop();
            listener.write(currentTask.getReqWarp());
        }
    }

    public void destroy() {
        this.taskArrayDeque.clear();
    }

    protected boolean writeRes(byte[] msg) {
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
}
