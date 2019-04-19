package com.lefuorgn.viewloader.display;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lefuorgn.R;
import com.lefuorgn.viewloader.base.DisplayViewBuilder;
import com.lefuorgn.viewloader.util.BuilderUtils;

import org.json.JSONObject;

/**
 * 日期区域展示视图构建器
 */

public class DateRangeDisplayBuilder extends DisplayViewBuilder {

    private TextView mStartLabel; // 第一行标签控件
    private TextView mStartValue; // 第一行值控件

    private TextView mEndLabel; // 第二行标签控件
    private TextView mEndValue; // 第二行值控件

    private View mResultContainer; // 计算结果容器
    private TextView mResultView; // 计算结果控件

    public DateRangeDisplayBuilder(Context context, int paddingWidth, int paddingHeight,
                                   float labelSize, int labelColor, float valueSize, int valueColor) {
        super(context, paddingWidth, paddingHeight, labelSize, labelColor, valueSize, valueColor);
    }

    @Override
    protected void initChildView(ViewGroup parent) {
        View start = mInflater.inflate(R.layout.builder_label_date, parent, false);
        initStartDateView(start);
        View end = mInflater.inflate(R.layout.builder_label_date, parent, false);
        initEndDateView(end);
        mResultContainer = mInflater.inflate(R.layout.builder_label_date, parent, false);
        initResultView(mResultContainer);
        parent.addView(start);
        parent.addView(end);
        parent.addView(mResultContainer);
    }

    private void initStartDateView(View parent) {
        // 第一行标签控件
        mStartLabel = (TextView) parent.findViewById(R.id.tv_builder_date_label);
        mStartLabel.setTextSize(mLabelSize - 2);
        mStartLabel.setTextColor(mLabelColor);
        // 第一行值控件
        mStartValue = (TextView) parent.findViewById(R.id.tv_builder_date_value);
        mStartValue.setTextSize(mValueSize);
        mStartValue.setTextColor(mValueColor);
        // 第一行按钮控件
        parent.findViewById(R.id.btn_builder_date).setVisibility(View.GONE);
    }

    private void initEndDateView(View parent) {
        // 第二行标签控件
        mEndLabel = (TextView) parent.findViewById(R.id.tv_builder_date_label);
        mEndLabel.setTextSize(mLabelSize - 2);
        mEndLabel.setTextColor(mLabelColor);
        // 第二行值控件
        mEndValue = (TextView) parent.findViewById(R.id.tv_builder_date_value);
        mEndValue.setTextSize(mValueSize);
        mEndValue.setTextColor(mValueColor);
        // 第二行按钮控件
        parent.findViewById(R.id.btn_builder_date).setVisibility(View.GONE);
    }

    /**
     * 初始化结果显示控件
     */
    private void initResultView(View parent) {
        // 第三行标签控件
        TextView resultLabel = (TextView) parent.findViewById(R.id.tv_builder_date_label);
        resultLabel.setTextSize(mLabelSize - 2);
        resultLabel.setTextColor(mLabelColor);
        resultLabel.setText("时长");
        // 第三行值控件
        mResultView = (TextView) parent.findViewById(R.id.tv_builder_date_value);
        mResultView.setTextSize(mValueSize);
        mResultView.setTextColor(mValueColor);
        // 第三行按钮控件
        parent.findViewById(R.id.btn_builder_date).setVisibility(View.GONE);
    }

    @Override
    protected void initChildData(JSONObject jsonObject) {
        String[] date = mValue.split(",");
        if(date.length == 2) {
            mStartValue.setText(date[0]);
            mEndValue.setText(date[1]);
        }
        // 设置标签
        mStartLabel.setText(getString(jsonObject, "startLabel"));
        mEndLabel.setText(getString(jsonObject, "endLabel"));
        // 设置是否显示计算结果
        boolean calculation = getBoolean(jsonObject, "duration");
        if(calculation) {
            mResultContainer.setVisibility(View.VISIBLE);
            mResultView.setText(BuilderUtils
                    .getCalculation(mStartValue, mEndValue, BuilderUtils
                            .getFormat(getString(jsonObject, "format"))));
        }else {
            mResultContainer.setVisibility(View.GONE);
        }
    }

    @Override
    protected boolean isHorizontal() {
        return false;
    }
}
