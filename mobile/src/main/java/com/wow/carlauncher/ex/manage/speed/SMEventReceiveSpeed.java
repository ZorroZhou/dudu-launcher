package com.wow.carlauncher.ex.manage.speed;

public class SMEventReceiveSpeed {
    private SMReceiveSpeedFrom from;
    private int speed;
    private int cameraSpeed;//电子眼限速度，对应的值为int类型，无限速则为0，单位：公里/小时

    public int getSpeed() {
        return speed;
    }

    public SMEventReceiveSpeed setSpeed(int speed) {
        this.speed = speed;
        return this;
    }

    public SMReceiveSpeedFrom getFrom() {
        return from;
    }

    public SMEventReceiveSpeed setFrom(SMReceiveSpeedFrom from) {
        this.from = from;
        return this;
    }

    public int getCameraSpeed() {
        return cameraSpeed;
    }

    public SMEventReceiveSpeed setCameraSpeed(int cameraSpeed) {
        this.cameraSpeed = cameraSpeed;
        return this;
    }

    public enum SMReceiveSpeedFrom {
        GPS, AMAP, OBD
    }
}
