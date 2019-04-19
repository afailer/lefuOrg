package com.lefuorgn.viewloader.widget;

import android.content.Context;
import android.text.InputType;
import android.util.AttributeSet;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.lefuorgn.R;

/**
 * 带有输入框的多选框
 */

public class EditCheckBox extends LinearLayout {

    private CheckBox mCheckBox;
    private EditText mEditText;

    public EditCheckBox(Context context) {
        super(context);
        init(context);
    }

    public EditCheckBox(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EditCheckBox(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        setOrientation(HORIZONTAL);
        mCheckBox = new CheckBox(context);
        mCheckBox.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        mEditText = new EditText(context);
        mEditText.setBackgroundResource(0);
        mEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.edit_text_line);
        mEditText.setEnabled(false);
        mEditText.setPadding(0, 0, 0, 0);
        mEditText.setMaxLines(1);
        mEditText.setInputType(InputType.TYPE_CLASS_TEXT);
        addView(mCheckBox);
        addView(mEditText);
        mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mEditText.setEnabled(isChecked);
            }
        });
    }

    /**
     * 设置文本内容的大小
     */
    public void setTextSize(float size) {
        mCheckBox.setTextSize(size);
        mEditText.setTextSize(size);
    }

    /**
     * 设置文本内容颜色
     */
    public void setTextColor(int color) {
        mCheckBox.setTextColor(color);
        mEditText.setTextColor(color);
    }

    /**
     * 设置输入框默认显示内容
     */
    public final void setHint(CharSequence hint) {
        mEditText.setHint(hint);
    }

    /**
     * 设置输入框默认显示内容颜色
     */
    public final void setHintTextColor(int color) {
        mEditText.setHintTextColor(color);
    }

    /**
     * 当前控件的选中状态
     */
    public boolean isChecked() {
        return mCheckBox.isChecked();
    }

    /**
     * 设置值
     */
    public void setValue(CharSequence value) {
        mEditText.setText(value);
        mEditText.setSelection(mEditText.length());
    }

    /**
     * 设置当前控件的被选中状态
     */
    public void setChecked(boolean checked) {
        mCheckBox.setChecked(checked);
    }

    /**
     * 获取当前的值
     */
    public String getValue() {
        return mEditText.getText().toString();
    }

}
