package com.wow.frame.repertory.remote;

/**
 * Created by 10124 on 2017/7/27.
 */
public abstract class WebServiceInfo {
    public abstract Class<?>[] getInterFaceClass();

    public abstract String getServerUrl();

    public abstract ResultConvertor getDefaultResultConvertor();

    public abstract ResultHandle getDefaultResultHandle();

//    public void checkResult(Object res) {
//
//    }
}
