package com.wow.carlauncher.view.base;

import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wow.carlauncher.R;


/**
 * Created by liu on 2017/1/5.
 */
public class BaseDialog extends Dialog {
    protected Context context;
    private RelativeLayout rl_content;
    private LinearLayout ll_dialog_background;

    public BaseDialog(Context context) {
        super(context, R.style.PopupDialog);
        this.context = context;
        init();
    }

    private void init() {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();

        LayoutInflater inflater = LayoutInflater.from(context);
        View base = inflater.inflate(R.layout.dialog_base, new LinearLayout(context), false);
        //获得屏幕宽度
        setContentView(base);

        Window dialogWindow = this.getWindow();
        WindowManager.LayoutParams layoutParams = dialogWindow.getAttributes();
        layoutParams.width = (int) (displayMetrics.widthPixels * 0.8);
        layoutParams.gravity = Gravity.BOTTOM;
        dialogWindow.setAttributes(layoutParams);


        ImageView tv_cancel = (ImageView) findViewById(R.id.iv_cancel);
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        rl_content = (RelativeLayout) findViewById(R.id.content);
        ll_dialog_background = (LinearLayout) findViewById(R.id.ll_dialog_background);
    }

    //隐藏底部按钮,不显示
    public BaseDialog hideBottom() {
        findViewById(R.id.dialog_bottom_ll).setVisibility(View.GONE);
        return this;
    }

    //设置标题方法
    public BaseDialog setTitle(String title) {
        TextView tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText(title);
        return this;
    }

    //隐藏标题方法
    public BaseDialog hideTitle() {
        TextView tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setVisibility(View.GONE);
        return this;
    }

    //隐藏标题方法
    public BaseDialog hideHeadLine() {
        RelativeLayout headline = (RelativeLayout) findViewById(R.id.rl_headline);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) headline.getLayoutParams();
        layoutParams.setMargins(0, 0, 0, 0);
        headline.setLayoutParams(layoutParams);
        headline.setVisibility(View.INVISIBLE);
        return this;
    }

    // 设置内容布局
    public BaseDialog setContent(int r) {
        View view = LayoutInflater.from(context).inflate(r, null);
        rl_content.setVisibility(View.VISIBLE);
        rl_content.addView(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        return this;
    }

    // 设置内容布局
    public BaseDialog setMessage(String message) {
        TextView tv_message = (TextView) findViewById(R.id.tv_message);
        tv_message.setVisibility(View.VISIBLE);
        tv_message.setText(message);
        return this;
    }

    //获得内容布局
    public RelativeLayout getContent() {
        return ((RelativeLayout) this.findViewById(R.id.content));
    }

    // 确认按钮 监听
    public BaseDialog setBtn1(String text, final OnBtnClickListener onClickListener) {
        Button btn1 = (Button) findViewById(R.id.btn1);
        btn1.setVisibility(View.VISIBLE);
        btn1.setText(text);
        if (onClickListener != null) {
            btn1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onClickListener.onClick(BaseDialog.this)) {
                        dismiss();
                    }
                }
            });
        } else {
            btn1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        }
        return this;
    }

    public BaseDialog setBtn2(String text, final OnBtnClickListener onClickListener) {
        Button btn2 = (Button) findViewById(R.id.btn2);
        btn2.setVisibility(View.VISIBLE);
        btn2.setText(text);
        if (onClickListener != null) {
            btn2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onClickListener.onClick(BaseDialog.this)) {
                        dismiss();
                    }
                }
            });
        } else {
            btn2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        }
        return this;
    }

    //取消按钮监听
    public BaseDialog setBtn3(String text, final OnBtnClickListener onClickListener) {
        Button btn3 = (Button) findViewById(R.id.btn3);
        btn3.setVisibility(View.VISIBLE);
        btn3.setText(text);
        if (onClickListener != null) {
            btn3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onClickListener.onClick(BaseDialog.this)) {
                        dismiss();
                    }
                }
            });
        } else {
            btn3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        }
        return this;
    }


    public interface OnBtnClickListener {
        boolean onClick(BaseDialog dialog);
    }

    public BaseDialog setBackground(int i) {
        ll_dialog_background.setBackgroundResource(i);
        return this;
    }

    public BaseDialog setGravityCenter() {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        Window dialogWindow = this.getWindow();
        WindowManager.LayoutParams layoutParams = dialogWindow.getAttributes();
        layoutParams.width = (int) (displayMetrics.widthPixels * 0.8);
        layoutParams.gravity = Gravity.CENTER;
        dialogWindow.setAttributes(layoutParams);
        return this;
    }
}

