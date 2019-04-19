package com.lefuorgn.viewloader.builder;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.lefuorgn.util.StringUtils;
import com.lefuorgn.util.TLog;
import com.lefuorgn.viewloader.adapter.SelectViewAdapter;
import com.lefuorgn.viewloader.base.BaseSpinnerAdapter;
import com.lefuorgn.viewloader.base.LabelViewBuilder;
import com.lefuorgn.viewloader.bean.SpinnerData;
import com.lefuorgn.viewloader.widget.SpinnerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 下拉菜单视图构建器
 */

public class SelectViewBuilder extends LabelViewBuilder {

    private SpinnerData mSpinnerData; // 当前选中的选项

    private SpinnerView mSpinner;

    public SelectViewBuilder(Context context, int paddingWidth, int paddingHeight,
                             float labelSize, int labelColor, float valueSize, int valueColor,
                             float describeSize, int describeColor, int describeBackground) {
        super(context, paddingWidth, paddingHeight, labelSize, labelColor, valueSize, valueColor,
                describeSize, describeColor, describeBackground);
    }

    @Override
    protected void initChildView(ViewGroup parent) {
        mSpinner = new SpinnerView(parent.getContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mSpinner.setLayoutParams(params);
        mSpinner.setTextSize(mValueSize);
        mSpinner.setTextColor(mValueColor);
        //noinspection SuspiciousNameCombination
        mSpinner.setYOffset(mPaddingHeight);
        parent.addView(mSpinner);
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
        // 初始化下拉菜单内容
        JSONArray jsonArray = getArray(jsonObject, "options");
        // 获取下拉菜单内容
        List<SpinnerData> list = new ArrayList<SpinnerData>();
        int position = -1;
        for (int i = 0; i < jsonArray.length(); i++) {
            SpinnerData data = new SpinnerData();
            JSONObject json = getObject(jsonArray, i);
            data.setLabel(getString(json, "label"));
            String value = getString(json, "value");
            data.setValue(value);
            if(!StringUtils.isEmpty(mValue) && mValue.equals(value)) {
                // 当前选项为默认项
                position = i;
                mSpinnerData = data;
            }
            list.add(data);
        }
        SelectViewAdapter adapter = new SelectViewAdapter(mContext, mValueSize, mValueColor, list);
        adapter.setOnSelectDataListener(new BaseSpinnerAdapter.OnSelectDataListener<SpinnerData>() {
            @Override
            public void onSelectData(SpinnerData spinnerData, int position) {
                mSpinnerData = spinnerData;
                TLog.error(mSpinnerData.toString());
            }
        });
        mSpinner.setAdapter(adapter);
        if(position != -1) {
            mSpinner.setSelectData(position, mSpinnerData.getLabel());
        }
    }

    @Override
    public String getValue() {
        if(mSpinnerData == null) {
            return "";
        }
        return mSpinnerData.getValue();
    }
}
