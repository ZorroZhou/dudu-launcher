package com.wow.carlauncher.common.user;

public class LocalUser {
    private Long userId;
    private String token;
    private String nickname;
    private String userPic;
    private String email;

    public String getEmail() {
        return email;
    }

    public LocalUser setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getNickname() {
        return nickname;
    }

    public LocalUser setNickname(String nickname) {
        this.nickname = nickname;
        return this;
    }

    public String getUserPic() {
        return userPic;
    }

    public LocalUser setUserPic(String userPic) {
        this.userPic = userPic;
        return this;
    }

    public Long getUserId() {
        return userId;
    }

    public LocalUser setUserId(Long userId) {
        this.userId = userId;
        return this;
    }

    public String getToken() {
        return token;
    }

    public LocalUser setToken(String token) {
        this.token = token;
        return this;
    }
}
