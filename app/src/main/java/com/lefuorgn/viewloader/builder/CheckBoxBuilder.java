package com.lefuorgn.viewloader.builder;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.lefuorgn.util.StringUtils;
import com.lefuorgn.viewloader.base.LabelViewBuilder;
import com.lefuorgn.viewloader.widget.EditCheckBox;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * 复选框视图构建器
 */

public class CheckBoxBuilder extends LabelViewBuilder {

    private LinearLayout mParent; // 父控件
    private LinearLayout mCheckBoxGroup;

    private List<String> mTempValues; // 临时值
    private List<EditCheckBox> mTempViews;

    public CheckBoxBuilder(Context context, int paddingWidth, int paddingHeight,
                           float labelSize, int labelColor, float valueSize, int valueColor,
                           float describeSize, int describeColor, int describeBackground) {
        super(context, paddingWidth, paddingHeight, labelSize, labelColor, valueSize, valueColor,
                describeSize, describeColor, describeBackground);
    }

    @Override
    protected void initChildView(ViewGroup parent) {
        mParent = (LinearLayout) parent;
        mCheckBoxGroup = new LinearLayout(mParent.getContext());
        mParent.addView(mCheckBoxGroup);
    }

    @Override
    protected void initValue(String value) {
        mTempValues = new ArrayList<String>();
        mTempViews = new ArrayList<EditCheckBox>();
        if(!StringUtils.isEmpty(value)) {
            Collections.addAll(mTempValues, value.split(","));
        }
    }

    @Override
    protected void initChildData(JSONObject jsonObject) {
        // 排列方式
        String orientation = getString(jsonObject, "layout");
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        LayoutParams childParams;
        if("inline".equals(orientation)) {
            // 横向布局
            mCheckBoxGroup.setOrientation(LinearLayout.HORIZONTAL);
            mCheckBoxGroup.setGravity(Gravity.END);
            mParent.setOrientation(LinearLayout.HORIZONTAL);
            childParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        }else {
            // 垂直布局
            params.setMargins(mPaddingWidth, mPaddingHeight, 0, 0);
            mCheckBoxGroup.setOrientation(LinearLayout.VERTICAL);
            mParent.setOrientation(LinearLayout.VERTICAL);
            childParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        }
        mCheckBoxGroup.setLayoutParams(params);
        // 添加选项控件
        addOption(getArray(jsonObject, "options"), childParams);
    }

    /**
     * 添加选项
     */
    private void addOption(JSONArray jsonArray, LayoutParams childParams) {
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = getObject(jsonArray, i);
            boolean other = getBoolean(jsonObject, "other");
            String label = getString(jsonObject, "label");
            String value = getString(jsonObject, "value");
            if(!other) {
                // CheckBox
                CheckBox checkBox = new CheckBox(mCheckBoxGroup.getContext());
                checkBox.setTextSize(mValueSize);
                checkBox.setTextColor(mValueColor);
                checkBox.setText(label);
                if(mTempValues.contains(value)) {
                    checkBox.setChecked(true);
                    mTempValues.remove(value);
                }
                checkBox.setTag(value);
                checkBox.setMaxLines(1);
                checkBox.setLayoutParams(childParams);
                mCheckBoxGroup.addView(checkBox);
            }else {
                // 输入框
                EditCheckBox editText = new EditCheckBox(mCheckBoxGroup.getContext());
                editText.setTextSize(mValueSize);
                editText.setTextColor(mValueColor);
                editText.setHint(label);
                editText.setHintTextColor(Color.GRAY);
                editText.setLayoutParams(childParams);
                mCheckBoxGroup.addView(editText);
                mTempViews.add(editText);
            }
        }
        if(mTempValues.size() > 0 && mTempViews.size() > 0) {
            for (int i = 0; i < mTempViews.size() && i < mTempValues.size(); i++) {
                EditCheckBox text = mTempViews.get(i);
                text.setValue(mTempValues.get(i));
                text.setChecked(true);
            }
        }
        mTempValues.clear();
        mTempViews.clear();
        mTempValues = null;
        mTempViews = null;
    }

    @Override
    public String getValue() {
        StringBuilder sb = new StringBuilder();
        for (int index = 0; index < mCheckBoxGroup.getChildCount(); index++) {
            View view = mCheckBoxGroup.getChildAt(index);
            if(view instanceof CheckBox && ((CheckBox)view).isChecked()) {
                sb.append(view.getTag()).append(",");
            }else if(view instanceof EditCheckBox && ((EditCheckBox)view).isChecked()) {
                sb.append(((EditCheckBox)view).getValue()).append(",");
            }
        }
        return StringUtils.isEmpty(sb.toString()) ? "" : sb.substring(0, sb.length() - 1);
    }
}
