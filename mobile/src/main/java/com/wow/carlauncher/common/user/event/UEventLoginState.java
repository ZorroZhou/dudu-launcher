package com.wow.carlauncher.common.user.event;

public class UEventLoginState {
    private boolean login = false;

    public boolean isLogin() {
        return login;
    }

    public UEventLoginState setLogin(boolean login) {
        this.login = login;
        return this;
    }
}
