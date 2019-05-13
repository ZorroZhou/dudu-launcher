package com.wow.carlauncher.view.dialog;

import android.content.Context;
import android.widget.ProgressBar;

import com.wow.carlauncher.R;
import com.wow.carlauncher.common.TaskExecutor;
import com.wow.carlauncher.view.base.BaseDialog;

public class ProgressDialog extends BaseDialog {
    private ProgressBar progressBar;

    public ProgressDialog(Context context) {
        super(context);
        setGravityCenter();
        setContent(R.layout.dialog_progress);

        progressBar = findViewById(R.id.progressBar);
    }

    public void setProgress(float p) {
        TaskExecutor.self().autoPost(() -> {
            progressBar.setProgress((int) (p * 100));
            progressBar.setMax(100);
        });

    }
}
