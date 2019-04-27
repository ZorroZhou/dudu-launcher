package com.wow.carlauncher.repertory.web.mobile.packet.user;

public class LoginRes {
    private String email;
    private String nickName;
    private String summary;

    public String getEmail() {
        return email;
    }

    public LoginRes setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getNickName() {
        return nickName;
    }

    public LoginRes setNickName(String nickName) {
        this.nickName = nickName;
        return this;
    }

    public String getSummary() {
        return summary;
    }

    public LoginRes setSummary(String summary) {
        this.summary = summary;
        return this;
    }

    @Override
    public String toString() {
        return "LoginRes{" +
                "email='" + email + '\'' +
                ", nickName='" + nickName + '\'' +
                ", summary='" + summary + '\'' +
                '}';
    }
}
