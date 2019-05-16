package com.wow.carlauncher.repertory.server.response;

public class LoginResult extends BaseResult<LoginResult.LoginInfo> {
    public static class LoginInfo {
        private String token;
        private Long id;

        public Long getId() {
            return id;
        }

        public LoginInfo setId(Long id) {
            this.id = id;
            return this;
        }

        public String getToken() {
            return token;
        }

        public LoginInfo setToken(String token) {
            this.token = token;
            return this;
        }

        @Override
        public String toString() {
            return "LoginInfo{" +
                    "token='" + token + '\'' +
                    ", id=" + id +
                    '}';
        }
    }

}
