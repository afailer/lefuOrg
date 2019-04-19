package com.lefuorgn.viewloader.builder;

import android.content.Context;
import android.text.InputType;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.EditText;

import com.lefuorgn.R;
import com.lefuorgn.viewloader.base.LabelViewBuilder;

import org.json.JSONObject;


/**
 * 单行文本视图构建器
 */

public class TextViewBuilder extends LabelViewBuilder {

    private EditText mValueView;

    public TextViewBuilder(Context context, int paddingWidth, int paddingHeight,
                           float labelSize, int labelColor, float valueSize, int valueColor,
                           float describeSize, int describeColor, int describeBackground) {
        super(context, paddingWidth, paddingHeight, labelSize, labelColor, valueSize, valueColor,
                describeSize, describeColor, describeBackground);
    }

    @Override
    protected void initChildView(ViewGroup parent) {
        mValueView = (EditText) mInflater.inflate(R.layout.builder_label_edit_text, parent, false);
        mValueView.setTextSize(mValueSize);
        mValueView.setTextColor(mValueColor);
        mValueView.setGravity(Gravity.END);
        mValueView.setMaxLines(1);
        mValueView.setInputType(InputType.TYPE_CLASS_TEXT);
        parent.addView(mValueView);
    }

    @Override
    protected void initValue(String value) {
        mValueView.setText(value);
    }

    @Override
    protected void initChildData(JSONObject jsonObject) {

    }

    @Override
    public String getValue() {
        return mValueView.getText().toString();
    }
}
