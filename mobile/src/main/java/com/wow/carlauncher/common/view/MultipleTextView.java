package com.wow.carlauncher.common.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;

import com.wow.carlauncher.R;


public class MultipleTextView extends android.support.v7.widget.AppCompatTextView {
    private String prefixText;
    private String contentText;
    private String suffixText;
    private int prefixTextColor;
    private int contentTextColor;
    private int suffixTextColor;

    public MultipleTextView(Context context) {
        this(context, (AttributeSet) null);
    }

    public MultipleTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 16842884);
    }

    public MultipleTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.MultipleTextView);
        this.prefixText = ta.getString(R.styleable.MultipleTextView_prefixText);
        this.contentText = ta.getString(R.styleable.MultipleTextView_contentText);
        this.suffixText = ta.getString(R.styleable.MultipleTextView_suffixText);
        this.prefixTextColor = ta.getColor(R.styleable.MultipleTextView_prefixTextColor, this.getCurrentTextColor());
        this.contentTextColor = ta.getColor(R.styleable.MultipleTextView_contentTextColor, this.getCurrentTextColor());
        this.suffixTextColor = ta.getColor(R.styleable.MultipleTextView_suffixTextColor, this.getCurrentTextColor());
        ta.recycle();
        this.updateUI();
    }

    private boolean run = false;

    private void updateUI() {
        if (run) {
            return;
        }
        run = true;
        SpannableStringBuilder builder = new SpannableStringBuilder();
        if (!TextUtils.isEmpty(this.prefixText)) {
            SpannableString suffixSpannableString = new SpannableString(this.prefixText);
            suffixSpannableString.setSpan(new ForegroundColorSpan(this.prefixTextColor), 0, this.prefixText.length(), 18);
            builder.append(suffixSpannableString);
        }

        if (!TextUtils.isEmpty(this.contentText)) {
            SpannableString suffixSpannableString = new SpannableString(this.contentText);
            suffixSpannableString.setSpan(new ForegroundColorSpan(this.contentTextColor), 0, this.contentText.length(), 18);
            builder.append(suffixSpannableString);
        }

        if (!TextUtils.isEmpty(this.suffixText)) {
            SpannableString suffixSpannableString = new SpannableString(this.suffixText);
            suffixSpannableString.setSpan(new ForegroundColorSpan(this.suffixTextColor), 0, this.suffixText.length(), 18);
            builder.append(suffixSpannableString);
        }

        this.setText(builder);
        run = false;
    }

    public void setPrefixText(String text) {
        this.prefixText = text;
        this.updateUI();
    }

    public void setPrefixTextColor(int color) {
        this.prefixTextColor = color;
        this.updateUI();
    }

    public void setSuffixText(String text) {
        this.suffixText = text;
        this.updateUI();
    }

    public void setSuffixTextColor(int color) {
        this.suffixTextColor = color;
        this.updateUI();
    }

    public void setContentText(String text) {
        this.contentText = text;
        this.updateUI();
    }

    public void setContentTextColor(int color) {
        this.contentTextColor = color;
        this.updateUI();
    }
}
