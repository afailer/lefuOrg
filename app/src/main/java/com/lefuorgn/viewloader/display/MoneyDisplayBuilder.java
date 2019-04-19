package com.lefuorgn.viewloader.display;

import android.content.Context;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.lefuorgn.util.StringUtils;
import com.lefuorgn.viewloader.base.DisplayViewBuilder;

import org.json.JSONObject;

/**
 * 展示金额视图构建器基类
 */

public class MoneyDisplayBuilder extends DisplayViewBuilder {

    private float mUnitSize; // 金额文字大小
    private int mUnitColor; // 金额文字颜色

    private TextView mValueView;
    private TextView mUnitView;

    public MoneyDisplayBuilder(Context context, int paddingWidth, int paddingHeight,
                               float labelSize, int labelColor, float valueSize, int valueColor,
                               float unitSize, int unitColor) {
        super(context, paddingWidth, paddingHeight, labelSize, labelColor, valueSize, valueColor);
        this.mUnitSize = unitSize;
        this.mUnitColor = unitColor;
    }

    @Override
    protected void initChildView(ViewGroup parent) {
        mValueView = new TextView(parent.getContext());
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.width = 0;
        params.weight = 1;
        mValueView.setLayoutParams(params);
        mValueView.setTextSize(mValueSize);
        mValueView.setTextColor(mValueColor);
        mValueView.setGravity(Gravity.END);
        mValueView.setMaxLines(1);
        parent.addView(mValueView);
        // 货币类型
        mUnitView = new TextView(parent.getContext());
        LayoutParams unitParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        mUnitView.setLayoutParams(unitParams);
        mUnitView.setTextSize(mUnitSize);
        mUnitView.setTextColor(mUnitColor);
        mUnitView.setPadding(StringUtils.dip2px(mContext, 4), 0, 0, 0);
        parent.addView(mUnitView);
    }

    @Override
    protected void initChildData(JSONObject jsonObject) {
        mValueView.setText(mValue);
        mUnitView.setText("(" + getString(jsonObject, "moneyType") + ")");
    }
}
