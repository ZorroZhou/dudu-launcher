package com.wow.frame.presenter;

import com.wow.frame.executer.BaseExecuter;

/**
 * Created by 10124 on 2017/7/26.
 */
public interface ProgressPresenter<T extends BaseExecuter> extends BasePresenter<T> {
    //用来展示进度条
    void showProgress();

    //用来隐藏进度条
    void hideProgress();

    //用来关闭进度条弹窗（只是隐藏进度条弹窗，finish activity时会报错）
    void dismissProgress();
}
