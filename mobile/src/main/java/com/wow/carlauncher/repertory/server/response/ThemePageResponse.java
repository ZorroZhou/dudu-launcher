package com.wow.carlauncher.repertory.server.response;

public class ThemePageResponse extends PageResponse<ThemePageResponse.UserThemeDto> {


    public static class UserThemeDto {
        private String nickName;
        private String userPic;
        private String themeName;
        private String themePic;
        private Long id;
        private Integer version;
        private String apkPackage;
        private String versionName;

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

        public Long getId() {
            return id;
        }

        public UserThemeDto setId(Long id) {
            this.id = id;
            return this;
        }

        public Integer getVersion() {
            return version;
        }

        public UserThemeDto setVersion(Integer version) {
            this.version = version;
            return this;
        }

        public String getApkPackage() {
            return apkPackage;
        }

        public UserThemeDto setApkPackage(String apkPackage) {
            this.apkPackage = apkPackage;
            return this;
        }

        public String getVersionName() {
            return versionName;
        }

        public UserThemeDto setVersionName(String versionName) {
            this.versionName = versionName;
            return this;
        }
    }
}
