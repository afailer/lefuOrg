package com.lefuorgn.viewloader.display;

import android.content.Context;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lefuorgn.util.StringUtils;
import com.lefuorgn.viewloader.base.DisplayViewBuilder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 日期区域展示视图构建器
 */

public class CheckBoxDisplayBuilder extends DisplayViewBuilder {

    private TextView mValueView;

    public CheckBoxDisplayBuilder(Context context, int paddingWidth, int paddingHeight,
                                  float labelSize, int labelColor, float valueSize, int valueColor) {
        super(context, paddingWidth, paddingHeight, labelSize, labelColor, valueSize, valueColor);
    }

    @Override
    protected void initChildView(ViewGroup parent) {
        ((LinearLayout) parent).setGravity(Gravity.NO_GRAVITY);
        int padding = StringUtils.dip2px(mContext, 6);
        mValueView = new TextView(parent.getContext());
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        mValueView.setLayoutParams(params);
        mValueView.setTextSize(mValueSize);
        mValueView.setTextColor(mValueColor);
        mValueView.setGravity(Gravity.END);
        mValueView.setLineSpacing(padding, 1.0f);
        parent.addView(mValueView);
    }

    @Override
    protected void initChildData(JSONObject jsonObject) {
        List<String> tempValues = new ArrayList<String>();
        if(!StringUtils.isEmpty(mValue)) {
            Collections.addAll(tempValues, mValue.split(","));
        }
        JSONArray jsonArray = getArray(jsonObject, "options");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject object = getObject(jsonArray, i);
            boolean other = getBoolean(object, "other");
            String label = getString(object, "label");
            String value = getString(object, "value");
            if(!other) {
                // CheckBox
                if(tempValues.contains(value)) {
                    sb.append(label).append("\n");
                    tempValues.remove(value);
                }
            }

        }
        for (String tempValue : tempValues) {
            sb.append(tempValue).append("\n");
        }
        if(sb.length() > 0) {
            mValueView.setText(sb.substring(0, sb.length() - 1));
        }else {
            mValueView.setText("");
        }
    }

}
