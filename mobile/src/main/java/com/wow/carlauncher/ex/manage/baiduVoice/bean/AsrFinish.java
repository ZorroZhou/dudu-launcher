package com.wow.carlauncher.ex.manage.baiduVoice.bean;

public class AsrFinish extends AsrParam {
    private String result_type;
    private String best_result;


    public String getResult_type() {
        return result_type;
    }

    public AsrFinish setResult_type(String result_type) {
        this.result_type = result_type;
        return this;
    }

    public String getBest_result() {
        return best_result;
    }

    public AsrFinish setBest_result(String best_result) {
        this.best_result = best_result;
        return this;
    }
}



