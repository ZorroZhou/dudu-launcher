package com.wow.carlauncher.view.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.wow.carlauncher.R;
import com.wow.carlauncher.view.base.BaseDialog;

/**
 * Created by 10124 on 2017/11/5.
 */

public class InputDialog extends AlertDialog {
    public InputDialog(Context context) {
        super(context);
        View base = LayoutInflater.from(getContext()).inflate(R.layout.dialog_input, new LinearLayout(context), false);
        setView(base);
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
    }

    public String getInputString() {
        return ((EditText) findViewById(R.id.et_input)).getText().toString();
    }

    public void setInputString(String input) {
        ((EditText) findViewById(R.id.et_input)).setText(input);
    }
}
