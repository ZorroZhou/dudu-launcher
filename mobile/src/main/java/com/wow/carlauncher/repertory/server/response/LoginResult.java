package com.wow.carlauncher.repertory.server.response;

public class LoginResult {
    private String token;
    private Long id;

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

    @Override
    public String toString() {
        return "LoginResult{" +
                "token='" + token + '\'' +
                ", id=" + id +
                '}';
    }
}
