package com.wow.carlauncher.plugin.obd.task;

import java.util.ArrayDeque;

/**
 * Created by 10124 on 2018/4/19.
 */

public class ObdTaskManage {
    private ArrayDeque<ObdTask> taskArrayDeque;

    private ObdTask currentTask;

    private TaskCallBack taskCallBack;


    public ObdTaskManage() {

    }

    public void init(TaskCallBack taskCallBack) {
        this.taskArrayDeque = new ArrayDeque<ObdTask>();
        this.taskCallBack = taskCallBack;
    }

    public void addTask(ObdTask obdTask) {
        taskArrayDeque.add(obdTask);
        runTask();
    }

    private void runTask() {
        if (currentTask == null && !taskArrayDeque.isEmpty()) {
            currentTask = taskArrayDeque.pop();
            taskCallBack.write(currentTask);
        }
    }

    public void destroy() {
        this.taskArrayDeque.clear();
    }

    public boolean writeRes(byte[] msg) {
        if (currentTask != null) {
            currentTask.writeRes(msg);
            taskCallBack.taskOver(currentTask);
            currentTask = null;
            runTask();
            return true;
        } else {
            return false;
        }
    }

    public interface TaskCallBack {
        void write(ObdTask task);

        void taskOver(ObdTask task);
    }
}
