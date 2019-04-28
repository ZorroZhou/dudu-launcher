package com.wow.carlauncher.ex.manage.baiduVoice.bean;

public class AsrEventPartial extends AsrEventParam {
    //    {
//        "results_recognition":["我"],"origin_result":{
//        "corpus_no":6684768921871811949, "err_no":0, "result":{
//            "word":["我"]},"sn":"ca82b65f-23a9-428e-b2fd-52b61b24202a_s-5"
//    },"error":0, "best_result":"我", "result_type":"partial_result"
//    }
    private String result_type;
    private String best_result;

    public String getResult_type() {
        return result_type;
    }

    public AsrEventPartial setResult_type(String result_type) {
        this.result_type = result_type;
        return this;
    }

    public String getBest_result() {
        return best_result;
    }

    public AsrEventPartial setBest_result(String best_result) {
        this.best_result = best_result;
        return this;
    }
}
//    private String errorDesc;
//
//    private int errorCode;
//
//    private String word;
//
//    public String getErrorDesc() {
//        return errorDesc;
//    }
//
//    public EventParam setErrorDesc(String errorDesc) {
//        this.errorDesc = errorDesc;
//        return this;
//    }
//
//    public int getErrorCode() {
//        return errorCode;
//    }