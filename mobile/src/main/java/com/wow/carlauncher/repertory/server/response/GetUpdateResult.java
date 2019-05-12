package com.wow.carlauncher.repertory.server.response;

public class GetUpdateResult extends BaseResult<GetUpdateResult.AppUpdate> {
    public static class AppUpdate {
        private Integer version;
        private String about;
        private String url;

        public Integer getVersion() {
            return version;
        }

        public AppUpdate setVersion(Integer version) {
            this.version = version;
            return this;
        }

        public String getAbout() {
            return about;
        }

        public AppUpdate setAbout(String about) {
            this.about = about;
            return this;
        }

        public String getUrl() {
            return url;
        }

        public AppUpdate setUrl(String url) {
            this.url = url;
            return this;
        }

        @Override
        public String toString() {
            return "AppUpdate{" +
                    "version=" + version +
                    ", about='" + about + '\'' +
                    ", url='" + url + '\'' +
                    '}';
        }
    }

}
