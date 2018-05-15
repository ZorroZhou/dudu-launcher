package com.wow.carlauncher.view.base;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;

/**
 * Created by liu on 2017/1/5.
 */
public class BaseDialog2 extends AlertDialog {
    public BaseDialog2(Context context) {
        super(context);
        setCanceledOnTouchOutside(true);
        setCancelable(true);
        setButton(BUTTON_POSITIVE, "确定", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        setButton(BUTTON_NEGATIVE, "取消", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dismiss();
            }
        });

        setOnShowListener(new OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                if (onShowListener != null) {
                    onShowListener.onShow(dialogInterface);
                }
                getButton(BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (okclickListener != null && okclickListener.onClick(BaseDialog2.this)) {
                            dismiss();
                        }
                    }
                });
            }
        });
    }

    private OnShowListener onShowListener;

    public void setOnShowListenerEx(@Nullable OnShowListener listener) {
        this.onShowListener = listener;
    }

    private OnBtnClickListener okclickListener;

    public BaseDialog2 setOkclickListener(OnBtnClickListener okclickListener) {
        this.okclickListener = okclickListener;
        return this;
    }

    public interface OnBtnClickListener {
        boolean onClick(BaseDialog2 dialog);
    }
}

