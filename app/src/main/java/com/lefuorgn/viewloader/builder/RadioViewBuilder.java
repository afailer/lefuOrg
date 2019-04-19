package com.lefuorgn.viewloader.builder;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RadioButton;

import com.lefuorgn.R;
import com.lefuorgn.util.StringUtils;
import com.lefuorgn.viewloader.base.LabelViewBuilder;
import com.lefuorgn.viewloader.widget.RadioEditButton;
import com.lefuorgn.viewloader.widget.RadioViewGroup;

import org.json.JSONArray;
import org.json.JSONObject;


/**
 * 单选框视图构建器
 */

public class RadioViewBuilder extends LabelViewBuilder {

    private LinearLayout mParent; // 父控件
    private RadioViewGroup mRadioGroup;
    private int mPosition; // 当前条目的位置

    public RadioViewBuilder(Context context, int paddingWidth, int paddingHeight,
                            float labelSize, int labelColor, float valueSize, int valueColor,
                            float describeSize, int describeColor, int describeBackground,
                            int position) {
        super(context, paddingWidth, paddingHeight, labelSize, labelColor, valueSize, valueColor,
                describeSize, describeColor, describeBackground);
        mPosition = position;
    }

    @Override
    protected void initChildView(ViewGroup parent) {
        mParent = (LinearLayout) parent;
        mRadioGroup = new RadioViewGroup(mParent.getContext());
        mParent.addView(mRadioGroup);
    }

    @Override
    protected void initValue(String value) {
        if(!StringUtils.isEmpty(mValue)) {
            int index = mValue.lastIndexOf(",");
            if(index != -1 && index == mValue.length() - 1) {
                mValue = mValue.substring(0, index);
            }
        }
    }

    @Override
    protected void initChildData(JSONObject jsonObject) {
        // 排列方式
        String orientation = getString(jsonObject, "layout");
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        RadioViewGroup.LayoutParams childParams;
        if("inline".equals(orientation)) {
            // 横向布局
            mRadioGroup.setOrientation(LinearLayout.HORIZONTAL);
            mRadioGroup.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
            mParent.setOrientation(LinearLayout.HORIZONTAL);
            childParams = new RadioViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            childParams.setMargins(0, 0, StringUtils.dip2px(mContext, 4), 0);
        }else {
            // 垂直布局
            params.setMargins(mPaddingWidth, 0, 0, 0);
            mRadioGroup.setOrientation(LinearLayout.VERTICAL);
            mParent.setOrientation(LinearLayout.VERTICAL);
            childParams = new RadioViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            childParams.setMargins(0, mPaddingHeight / 2 , 0, 0);
        }
        mRadioGroup.setLayoutParams(params);
        // 添加选项控件
        addOption(getArray(jsonObject, "options"), childParams);
    }

    /**
     * 添加选项
     */
    private void addOption(JSONArray jsonArray, RadioViewGroup.LayoutParams childParams) {
        int id = -1;
        RadioEditButton radioButton = null;
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = getObject(jsonArray, i);
            String label = getString(jsonObject, "label");
            String value = getString(jsonObject, "value");
            if(getBoolean(jsonObject, "other")) {
                // RadioEditButton
                RadioEditButton button = new RadioEditButton(mRadioGroup.getContext());
                button.setLayoutParams(childParams);
                button.setButtonId(mPosition * 100 + i);
                button.setTextSize(mValueSize);
                button.setTextColor(mValueColor);
                button.setText(label);
                if(radioButton == null) {
                    radioButton = button;
                }
                button.setMaxLines(1);
                button.setButtonTag(value);
                mRadioGroup.addView(button);
            }else {
                // RadioButton
                RadioButton button = (RadioButton) mInflater
                        .inflate(R.layout.builder_label_radio_button, mRadioGroup, false);
                RadioViewGroup.LayoutParams params = (RadioViewGroup.LayoutParams) button.getLayoutParams();
                params.width = childParams.width;
                params.topMargin = childParams.topMargin;
                params.rightMargin = childParams.rightMargin;
                button.setId(mPosition * 100 + i);
                button.setTextSize(mValueSize);
                button.setTextColor(mValueColor);
                button.setText(label);
                if(!"".equals(mValue) && mValue.equals(value)) {
                    id = button.getId();
                }
                button.setMaxLines(1);
                button.setTag(value);
                mRadioGroup.addView(button);
            }
        }
        // 设置默认选择项
        if(id != -1) {
            mRadioGroup.check(id);
        }else if(!"".equals(mValue) && radioButton != null) {
            mRadioGroup.check(radioButton.getButtonId());
            radioButton.setEditText(mValue);
        }
    }

    @Override
    public String getValue() {
        int viewId = mRadioGroup.getCheckedRadioButtonId();
        if(viewId == -1) {
            return "";
        }
        View checkedView = mRadioGroup.findViewById(viewId);
        if (checkedView != null) {
            ViewGroup viewGroup = (ViewGroup) checkedView.getParent();
            if(viewGroup != null && viewGroup instanceof RadioEditButton) {
                return ((RadioEditButton) viewGroup).getEditText();
            }else if(checkedView instanceof RadioButton) {
                return (String) checkedView.getTag();
            }
        }
        return "";
    }
}
