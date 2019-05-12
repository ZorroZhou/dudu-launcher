package com.wow.carlauncher.ex.manage.speed;

public class SMEventSendSpeed {
    private boolean use;

    private int speed;

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
}
