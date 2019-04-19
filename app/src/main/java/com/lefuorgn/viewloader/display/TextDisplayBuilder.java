package com.lefuorgn.viewloader.display;

import android.content.Context;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

import com.lefuorgn.viewloader.base.DisplayViewBuilder;

import org.json.JSONObject;

/**
 * 展示短文本视图构建器基类
 */

public class TextDisplayBuilder extends DisplayViewBuilder {

    private TextView mValueView;

    public TextDisplayBuilder(Context context, int paddingWidth, int paddingHeight,
                              float labelSize, int labelColor, float valueSize, int valueColor) {
        super(context, paddingWidth, paddingHeight, labelSize, labelColor, valueSize, valueColor);
    }

    @Override
    protected void initChildView(ViewGroup parent) {
        mValueView = new TextView(parent.getContext());
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        mValueView.setLayoutParams(params);
        mValueView.setTextSize(mValueSize);
        mValueView.setTextColor(mValueColor);
        mValueView.setGravity(Gravity.END);
        mValueView.setMaxLines(1);
        parent.addView(mValueView);
    }

    @Override
    protected void initChildData(JSONObject jsonObject) {
        mValueView.setText(mValue);
    }
}
