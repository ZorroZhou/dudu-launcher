package com.wow.carlauncher.common.view;

import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.wow.carlauncher.R;
import com.wow.carlauncher.common.util.CommonUtil;
import com.wow.carlauncher.common.util.ViewUtils;

import static com.wow.carlauncher.common.CommonData.TAG;

/**
 * Created by soap on 16/7/25.
 */
public class SetView extends LinearLayout {

    private int type;

    private String title, summary, summary2;

    private AchieveDialogController achieveDialogController;

    public void setAchieveDialogController(AchieveDialogController achieveDialogController) {
        this.achieveDialogController = achieveDialogController;
    }

    private OnValueChangeListener onValueChangeListener;

    public void setOnValueChangeListener(OnValueChangeListener onValueChangeListener) {
        this.onValueChangeListener = onValueChangeListener;
    }

    private OnDialogInputListener onDialogInputListener;

    public void setOnDialogInputListener(OnDialogInputListener onDialogInputListener) {
        this.onDialogInputListener = onDialogInputListener;
    }

    private String[] listNames;
    private String[] listValues;
    private String currentValue;

    private TextView tv_summary, tv_title;

    private boolean useState = true;

    private View contentLayoutView;

    public SetView(Context context) {
        super(context);
        init(context, null);
    }

    public SetView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.SetView);
            type = ta.getInt(R.styleable.SetView_type, 0);
            title = ta.getString(R.styleable.SetView_titleEx);
            if (CommonUtil.isNull(title)) {
                title = "";
            }
            summary = ta.getString(R.styleable.SetView_summary);
            if (CommonUtil.isNull(summary)) {
                summary = "";
            }
            summary2 = ta.getString(R.styleable.SetView_summary2);
            if (CommonUtil.isNull(summary2)) {
                summary2 = "";
            }
            int listName = ta.getResourceId(R.styleable.SetView_listName, 0);
            int listValue = ta.getResourceId(R.styleable.SetView_listValue, 0);

            switch (type) {
                case 1: {
                    if (listName == 0 || listValue == 0) {
                        break;
                    }
                    listNames = getResources().getStringArray(listName);
                    listValues = getResources().getStringArray(listValue);
                    break;
                }
            }

            ta.recycle();
        }

        contentLayoutView = LayoutInflater.from(context).inflate(R.layout.set_view_default, new LinearLayout(getContext()));

        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        addView(contentLayoutView, lp);

        tv_title = contentLayoutView.findViewById(android.R.id.title);
        tv_title.setText(title);

        tv_summary = contentLayoutView.findViewById(android.R.id.summary);

        switch (type) {
            case 0: {
                if (CommonUtil.isNull(summary)) {
                    tv_summary.setVisibility(GONE);
                } else {
                    tv_summary.setText(summary);
                }
                ViewGroup frame = (ViewGroup) contentLayoutView.findViewById(android.R.id.widget_frame);
                if (frame != null) {
                    ImageView imageView = new ImageView(getContext());
                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                    imageView.setImageResource(R.mipmap.set_view_right);
                    frame.addView(imageView, new LayoutParams(ViewUtils.dip2pxi(getContext(), 15), ViewUtils.dip2pxi(getContext(), 15)));
                }
                break;
            }
            case 1: {
                if (listNames.length == 0) {
                    break;
                }
                tv_summary.setText(listNames[0]);
                currentValue = listValues[0];

                ViewGroup frame = contentLayoutView.findViewById(android.R.id.widget_frame);
                if (frame != null) {
                    ImageView imageView = new ImageView(getContext());
                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                    imageView.setImageResource(R.mipmap.set_view_right);
                    frame.addView(imageView, new LayoutParams(ViewUtils.dip2pxi(getContext(), 15), ViewUtils.dip2pxi(getContext(), 15)));
                }
                break;
            }
            case 2: {
                tv_summary.setText(summary2);
                currentValue = "0";
                ViewGroup frame = contentLayoutView.findViewById(android.R.id.widget_frame);
                if (frame != null) {
                    CheckBox checkBox = (CheckBox) LayoutInflater.from(context).inflate(R.layout.set_view_frame_checkbox, null);
                    checkBox.setClickable(false);
                    checkBox.setFocusable(false);
                    checkBox.setBackgroundResource(android.R.color.transparent);
                    frame.addView(checkBox, new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                }
                break;
            }
            case 3: {
                tv_summary.setText(summary2);
                currentValue = "0";
                ViewGroup frame = contentLayoutView.findViewById(android.R.id.widget_frame);
                if (frame != null) {
                    Switch checkBox = (Switch) LayoutInflater.from(context).inflate(R.layout.set_view_frame_switch, null);
                    checkBox.setClickable(false);
                    checkBox.setFocusable(false);
                    checkBox.setBackgroundResource(android.R.color.transparent);
                    frame.addView(checkBox, new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                }
                break;
            }
            case 4: {
                currentValue = "";
                tv_summary.setText(summary);
                ViewGroup frame = (ViewGroup) contentLayoutView.findViewById(android.R.id.widget_frame);
                if (frame != null) {
                    ImageView imageView = new ImageView(getContext());
                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                    imageView.setImageResource(R.mipmap.set_view_right);
                    frame.addView(imageView, new LayoutParams(ViewUtils.dip2pxi(getContext(), 15), ViewUtils.dip2pxi(getContext(), 15)));
                }
                break;
            }
        }

        super.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!useState) {
                    return;
                }
                if (onClickListener != null) {
                    onClickListener.onClick(v);
                }
                switch (type) {
                    case 4: {
                        if (achieveDialogController == null) {
                            break;
                        }
                        final Dialog dialog = achieveDialogController.achieveDialog(title);
                        dialog.show();

                        achieveDialogController.initDialog(dialog);

                        View button = dialog.findViewById(achieveDialogController.getButtonId());
                        if (button == null) {
                            dialog.dismiss();
                            break;
                        }
                        EditText et = ((EditText) dialog.findViewById(android.R.id.edit));
                        if (et != null) {
                            et.setText(currentValue);
                        }
                        button.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                EditText et = ((EditText) dialog.findViewById(android.R.id.edit));
                                if (et != null) {
                                    String newValue = et.getText().toString();
                                    Log.d(TAG, "newValue: " + newValue + "--" + currentValue);
                                    if (newValue.equals(currentValue)) {
                                        dialog.dismiss();
                                    } else {
                                        String oldValue = currentValue;
                                        setValue(newValue);
                                        if (onDialogInputListener != null) {
                                            onDialogInputListener.onValueChange(newValue, oldValue, dialog);
                                        } else {
                                            dialog.dismiss();
                                        }
                                    }
                                }
                            }
                        });
                        break;
                    }
                    case 3: {
                        ViewGroup frame = (ViewGroup) contentLayoutView.findViewById(android.R.id.widget_frame);
                        for (int i = 0; i < frame.getChildCount(); i++) {
                            View cb = frame.getChildAt(i);
                            if (cb instanceof Switch) {
                                if (((Switch) cb).isChecked()) {
                                    String oldValue = currentValue;
                                    setValue("0");
                                    if (onValueChangeListener != null) {
                                        onValueChangeListener.onValueChange("0", oldValue);
                                    }
                                } else {
                                    String oldValue = currentValue;
                                    setValue("1");
                                    if (onValueChangeListener != null) {
                                        onValueChangeListener.onValueChange("1", oldValue);
                                    }
                                }
                                break;
                            }
                        }
                        break;
                    }
                    case 2: {
                        ViewGroup frame = (ViewGroup) contentLayoutView.findViewById(android.R.id.widget_frame);
                        for (int i = 0; i < frame.getChildCount(); i++) {
                            View cb = frame.getChildAt(i);
                            if (cb instanceof CheckBox) {
                                if (((CheckBox) cb).isChecked()) {
                                    String oldValue = currentValue;
                                    setValue("0");
                                    if (onValueChangeListener != null) {
                                        onValueChangeListener.onValueChange("0", oldValue);
                                    }
                                } else {
                                    String oldValue = currentValue;
                                    setValue("1");
                                    if (onValueChangeListener != null) {
                                        onValueChangeListener.onValueChange("1", oldValue);
                                    }

                                }
                                break;
                            }
                        }
                        break;
                    }
                    case 1: {
                        if (achieveDialogController == null || listNames.length != listValues.length || listValues.length == 0) {
                            break;
                        }
                        final Dialog dialog = achieveDialogController.achieveDialog(title);

                        if (dialog == null || achieveDialogController.getItem() == 0) {
                            break;
                        }

                        LinearLayout contnet = new LinearLayout(getContext());
                        contnet.setOrientation(LinearLayout.VERTICAL);
                        LayoutInflater inflater = LayoutInflater.from(getContext());
                        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                        LayoutParams lpi = new LayoutParams(LayoutParams.MATCH_PARENT, 1);
                        for (int i = 0; i < listNames.length; i++) {
                            View item = inflater.inflate(achieveDialogController.getItem(), null);
                            View vt = item.findViewById(android.R.id.title);
                            if (vt instanceof TextView) {
                                ((TextView) vt).setText(listNames[i]);
                            }
                            item.setTag(listValues[i]);
                            item.setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                    String oldValue = currentValue;
                                    setValue(v.getTag() + "");
                                    if (onValueChangeListener != null && !v.getTag().equals(oldValue)) {
                                        onValueChangeListener.onValueChange(v.getTag() + "", oldValue);
                                    }

                                }
                            });

                            contnet.addView(item, lp);
                            if (i != listNames.length - 1 && achieveDialogController.getInterval() != 0) {
                                contnet.addView(inflater.inflate(achieveDialogController.getInterval(), null), lpi);
                            }
                        }

                        dialog.show();

                        dialog.setContentView(contnet);
                        break;
                    }
                }
            }
        });
    }

    public void setValue(String value) {
        switch (type) {
            case 4: {
                if (CommonUtil.isNull(value)) {
                    tv_summary.setText(summary);
                } else {
                    tv_summary.setText(value + summary2);
                }

                currentValue = value;
                break;
            }
            case 3: {
                ViewGroup frame = (ViewGroup) contentLayoutView.findViewById(android.R.id.widget_frame);
                for (int i = 0; i < frame.getChildCount(); i++) {
                    View cb = frame.getChildAt(i);
                    if (cb instanceof Switch) {
                        if (value.equals("0")) {
                            ((Switch) cb).setChecked(false);
                            tv_summary.setText(summary2);
                        } else {
                            ((Switch) cb).setChecked(true);
                            tv_summary.setText(summary);
                        }
                        break;
                    }
                }
                currentValue = value;
                break;
            }
            case 2: {
                ViewGroup frame = (ViewGroup) contentLayoutView.findViewById(android.R.id.widget_frame);
                for (int i = 0; i < frame.getChildCount(); i++) {
                    View cb = frame.getChildAt(i);
                    if (cb instanceof CheckBox) {
                        if (value.equals("0")) {
                            ((CheckBox) cb).setChecked(false);
                            tv_summary.setText(summary2);
                        } else {
                            ((CheckBox) cb).setChecked(true);
                            tv_summary.setText(summary);
                        }
                        break;
                    }
                }
                currentValue = value;
                break;
            }
            case 1: {
                for (int i = 0; i < listValues.length; i++) {
                    if (listValues[i].equals(value)) {
                        currentValue = listValues[i];
                        tv_summary.setText(listNames[i]);
                        break;
                    }
                }
                break;
            }
        }
    }

    //设置小标题信息
    public void setSummary(String s) {
        if (CommonUtil.isNull(s)) {
            tv_summary.setVisibility(GONE);
        } else {
            tv_summary.setVisibility(VISIBLE);
            tv_summary.setText(s);
        }
        this.summary = s;
    }

    private ColorStateList titleColor, summaryColor;

    //设置为可用
    public void enable() {
        this.useState = true;
        if (titleColor != null) {
            this.tv_title.setTextColor(titleColor);
        }

        if (summaryColor != null) {
            this.tv_summary.setTextColor(summaryColor);
        }
    }

    //设置为不可用
    public void disable() {
        this.useState = false;
        titleColor = this.tv_title.getTextColors();
        this.tv_title.setTextColor(ContextCompat.getColor(getContext(), R.color.set_view_disable));

        summaryColor = this.tv_summary.getTextColors();
        this.tv_summary.setTextColor(ContextCompat.getColor(getContext(), R.color.set_view_disable));
    }

    //设置选中状态,只对check和switch有效
    public void setChecked(boolean c) {
        if (type == 2 || type == 3) {
            if (c) {
                setValue("1");
            } else {
                setValue("0");
            }
        }
    }

    //查看是否选中,只对check和switch有效
    public boolean isChecked() {
        if (type == 2 || type == 3 || currentValue.equals("1")) {
            return true;
        } else {
            return false;
        }
    }

    private OnClickListener onClickListener;

    @Override
    public void setOnClickListener(OnClickListener l) {
        this.onClickListener = l;
    }

    public static abstract class AchieveDialogController {
        public abstract Dialog achieveDialog(String title);

        public void initDialog(Dialog dialog) {

        }

        public int getItem() {
            return 0;
        }

        public int getInterval() {
            return 0;
        }

        public int getButtonId() {
            return 0;
        }
    }

    public interface OnValueChangeListener {
        void onValueChange(String newValue, String oldValue);
    }

    public interface OnDialogInputListener {
        void onValueChange(String newValue, String oldValue, Dialog dialog);
    }
}
