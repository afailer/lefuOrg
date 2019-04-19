package com.lefuorgn.viewloader.display;

import android.content.Context;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

import com.lefuorgn.viewloader.base.DisplayViewBuilder;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * 单选内容的展示
 */

public class RadioDisplayBuilder extends DisplayViewBuilder {

    private TextView mValueView;

    public RadioDisplayBuilder(Context context, int paddingWidth, int paddingHeight,
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
        int index = mValue.lastIndexOf(",");
        if(index != -1 && index == mValue.length() - 1) {
            mValue = mValue.substring(0, index);
        }
        JSONArray jsonArray = getArray(jsonObject, "options");
        boolean hit = false;
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject object = getObject(jsonArray, i);
            if(getString(object, "value").equals(mValue)) {

                hit = true;
                mValueView.setText(getString(object, "label"));
                break;
            }
        }
        if(!hit) {
            // 没有命中
            mValueView.setText(mValue);
        }
    }
}
