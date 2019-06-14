package com.wow.carlauncher.ex.manage.speed;

public class SMEventSendSpeed {
    private boolean use;
    private int speed;
    private int cameraSpeed;

    public int getSpeed() {
        return speed;
    }

    public SMEventSendSpeed setSpeed(int speed) {
        this.speed = speed;
        return this;
    }

    public boolean isUse() {
        return use;
    }

    public SMEventSendSpeed setUse(boolean use) {
        this.use = use;
        return this;
    }

    public int getCameraSpeed() {
        return cameraSpeed;
    }

    public SMEventSendSpeed setCameraSpeed(int cameraSpeed) {
        this.cameraSpeed = cameraSpeed;
        return this;
    }
}
