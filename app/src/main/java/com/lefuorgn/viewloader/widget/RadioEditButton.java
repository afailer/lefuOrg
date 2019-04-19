package com.lefuorgn.viewloader.widget;

import android.content.Context;
import android.graphics.Color;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Checkable;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.lefuorgn.R;
import com.lefuorgn.util.StringUtils;

/**
 * 带有输入框的单选框
 */

public class RadioEditButton extends LinearLayout implements Checkable {

    private RadioButton mRadioButton;
    private EditText mEditText;

    public RadioEditButton(Context context) {
        super(context);
        init(context);
    }

    public RadioEditButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RadioEditButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        setOrientation(HORIZONTAL);
        mRadioButton = (RadioButton) LayoutInflater.from(context)
                .inflate(R.layout.builder_label_radio_button, this, false);
        mEditText = new EditText(context);
        mEditText.setBackgroundResource(0);
        mEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.edit_text_line);
        mEditText.setPadding(StringUtils.dip2px(context, 4), 0, 0, 0);
        mEditText.setEnabled(false);
        mEditText.setInputType(InputType.TYPE_CLASS_TEXT);
        mEditText.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        addView(mRadioButton);
        addView(mEditText);
    }

    @Override
    public void toggle() {
        mRadioButton.toggle();
    }

    @Override
    public boolean performClick() {
        toggle();
        return super.performClick();
    }

    /**
     * 获取当前控件的被选择状态
     */
    public boolean isChecked() {
        return mRadioButton.isChecked();
    }

    /**
     * 设置当前控件的被选择状态
     */
    public void setChecked(boolean checked) {
        mEditText.setEnabled(checked);
        mRadioButton.setChecked(checked);
    }

    @Override
    public void setLayoutParams(ViewGroup.LayoutParams params) {
        super.setLayoutParams(params);
        LayoutParams layoutParams = (LinearLayout.LayoutParams) mEditText.getLayoutParams();
        layoutParams.width = params.width;
        layoutParams.height = params.height;
    }

    public void setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener Listener) {
        mRadioButton.setOnCheckedChangeListener(Listener);
    }

    /**
     * 设置文本内容大小
     * @param size 单位是SP
     */
    public void setTextSize(float size) {
        mRadioButton.setTextSize(size);
        mEditText.setTextSize(size);
    }

    /**
     * 设置文本颜色
     */
    public void setTextColor(int color) {
        mRadioButton.setTextColor(color);
        mEditText.setTextColor(color);
    }

    /**
     * 设置按钮文本内容
     */
    public void setText(CharSequence text) {
        mRadioButton.setText(text);
        mEditText.setHint(text);
        mEditText.setHintTextColor(Color.GRAY);
    }

    /**
     * 设置文本内容的最大行数
     */
    public void setMaxLines(int maxLines) {
        mRadioButton.setMaxLines(maxLines);
        mEditText.setMaxLines(maxLines);
    }

    /**
     * 设置按钮ID
     */
    public void setButtonId(int id) {
        mRadioButton.setId(id);
    }

    /**
     * 获取按钮ID
     */
    public int getButtonId() {
        return mRadioButton.getId();
    }

    /**
     * 设置按钮TAG
     */
    public void setButtonTag(final Object tag) {
        mRadioButton.setTag(tag);
    }

    /**
     * 设置值文本
     */
    public void setEditText(String mValue) {
        mEditText.setText(mValue);
        mEditText.setSelection(mEditText.length());
    }

    /**
     * 获取值文本
     */
    public String getEditText() {
        if(mEditText != null) {
            return mEditText.getText().toString();
        }
        return "";
    }

}
