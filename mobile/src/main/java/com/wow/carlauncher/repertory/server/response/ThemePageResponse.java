package com.wow.carlauncher.repertory.server.response;

public class ThemePageResponse extends PageResponse<ThemePageResponse.UserThemeDto> {


    public static class UserThemeDto {
        private String nickName;
        private String userPic;
        private String themeName;
        private String themePic;
        private String url;
        private Integer version;

        public String getNickName() {
            return nickName;
        }

        public UserThemeDto setNickName(String nickName) {
            this.nickName = nickName;
            return this;
        }

        public String getUserPic() {
            return userPic;
        }

        public UserThemeDto setUserPic(String userPic) {
            this.userPic = userPic;
            return this;
        }

        public String getThemeName() {
            return themeName;
        }

        public UserThemeDto setThemeName(String themeName) {
            this.themeName = themeName;
            return this;
        }

        public String getThemePic() {
            return themePic;
        }

        public UserThemeDto setThemePic(String themePic) {
            this.themePic = themePic;
            return this;
        }

        public String getUrl() {
            return url;
        }

        public UserThemeDto setUrl(String url) {
            this.url = url;
            return this;
        }

        public Integer getVersion() {
            return version;
        }

        public UserThemeDto setVersion(Integer version) {
            this.version = version;
            return this;
        }
    }
}
