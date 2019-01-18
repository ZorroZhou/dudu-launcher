package com.wow.carlauncher.view.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.wow.carlauncher.R;
import com.wow.carlauncher.view.base.BaseDialog2;

/**
 * Created by 10124 on 2017/11/5.
 */

public class InputDialog extends BaseDialog2 {

    public InputDialog(Context context) {
        super(context);
        setView(LayoutInflater.from(getContext()).inflate(R.layout.dialog_input, new LinearLayout(context), false));
        setOnShowListenerEx(new OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                if (inputString != null) {
                    ((EditText) findViewById(R.id.et_input)).setText(inputString);
                }
            }
        });
    }


    public String getInputString() {
        return ((EditText) findViewById(R.id.et_input)).getText().toString();
    }

    private String inputString;

    public void setInputString(String input) {
        this.inputString = input;
    }
}
