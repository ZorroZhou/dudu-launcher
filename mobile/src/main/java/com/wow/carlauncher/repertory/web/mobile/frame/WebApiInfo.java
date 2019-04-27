package com.wow.carlauncher.repertory.web.mobile.frame;

/**
 * Created by 10124 on 2017/7/27.
 */
public interface WebApiInfo {
    Class<?>[] getInterFaceClass();

    String getServerUrl();

    ResultHandle getDefaultResultHandle();
}
