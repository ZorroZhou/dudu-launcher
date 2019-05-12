package com.wow.carlauncher.ex.manage.speed;

public class SMEventReceiveSpeed {
    private SMReceiveSpeedFrom from;
    private int speed;

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

    public enum SMReceiveSpeedFrom {
        GPS, AMAP
    }
}
