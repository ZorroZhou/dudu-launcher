package com.wow.carlauncher.repertory.server.response;

public class LoginResult {
    private String token;
    private String email;
    private Long id;
    private String nickName;

    public Long getId() {
        return id;
    }

    public LoginResult setId(Long id) {
        this.id = id;
        return this;
    }

    public String getToken() {
        return token;
    }

    public LoginResult setToken(String token) {
        this.token = token;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public LoginResult setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getNickName() {
        return nickName;
    }

    public LoginResult setNickName(String nickName) {
        this.nickName = nickName;
        return this;
    }

    @Override
    public String toString() {
        return "LoginResult{" +
                "token='" + token + '\'' +
                ", id=" + id +
                '}';
    }
}
