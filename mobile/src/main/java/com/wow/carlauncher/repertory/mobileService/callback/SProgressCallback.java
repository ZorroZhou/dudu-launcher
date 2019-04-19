package com.wow.carlauncher.repertory.mobileService.callback;

/**
 * Created by Che on 2016/12/6 0006.
 */
public interface SProgressCallback<T> extends SCallBack<T> {
    void onProgress(float progress);
}
