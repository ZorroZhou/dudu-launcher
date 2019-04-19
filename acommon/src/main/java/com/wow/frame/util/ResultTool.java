package com.wow.carlauncher.common.util;

public class ResultTool {
    public static void checkResult(boolean expression, String msg) {
        if (!expression) {
            throw new ResultException(msg);
        }
    }

    public static class ResultException extends RuntimeException {
        private String msg;

        public ResultException(String msg) {
            super();
            this.msg = msg;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }
}
