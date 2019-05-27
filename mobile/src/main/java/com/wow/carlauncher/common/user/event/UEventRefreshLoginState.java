package com.wow.carlauncher.common.user.event;

public class UEventRefreshLoginState {
    private boolean login = false;

    public boolean isLogin() {
        return login;
    }

    public UEventRefreshLoginState setLogin(boolean login) {
        this.login = login;
        return this;
    }
}
