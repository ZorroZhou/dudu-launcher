package com.wow.carlauncher.view.dialog;

import android.content.Context;
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
    private EditText editText;

    public InputDialog(Context context) {
        super(context);
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_input, new LinearLayout(context), false);
        setView(contentView);

        editText = contentView.findViewById(R.id.et_input);
        setOnShowListenerEx(dialogInterface -> {
            if (inputString != null) {
                editText.setText(inputString);
            }
        });
    }

    public EditText getEditText() {
        return editText;
    }

    public String getInputString() {
        return editText.getText().toString();
    }

    private String inputString;

    public void setInputString(String input) {
        this.inputString = input;
    }


}
