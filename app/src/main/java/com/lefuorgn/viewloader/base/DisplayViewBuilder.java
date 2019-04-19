package com.lefuorgn.viewloader.base;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lefuorgn.R;

import org.json.JSONObject;

/**
 * 展示控件视图构建器基类
 */

public abstract class DisplayViewBuilder extends ViewBuilder {

    protected String mValue; // 默认值

    protected float mValueSize; // 值文字大小
    protected int mValueColor; // 值文字颜色

    protected float mLabelSize; // label文字大小
    protected int mLabelColor; // label文字颜色

    private TextView mLabelView;

    public DisplayViewBuilder(Context context, int paddingWidth, int paddingHeight,
                              float labelSize, int labelColor, float valueSize, int valueColor) {
        super(context, paddingWidth, paddingHeight);
        mLabelSize = labelSize;
        mLabelColor = labelColor;
        mValueSize = valueSize;
        mValueColor = valueColor;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.builder_display_view_parent;
    }

    @Override
    protected final void initView(View parent) {
        // 初始化包裹内容控件
        LinearLayout layout = (LinearLayout) parent;
        layout.setOrientation(isHorizontal() ? LinearLayout.HORIZONTAL : LinearLayout.VERTICAL);
        layout.setPadding(mPaddingWidth, mPaddingHeight, mPaddingWidth, mPaddingHeight);
        if(isHorizontal()) {
            layout.setGravity(Gravity.CENTER_VERTICAL);
        }
        // 初始化标签
        mLabelView = (TextView) parent.findViewById(R.id.tv_builder_display_view_parent);
        mLabelView.setTextSize(mLabelSize);
        mLabelView.setPadding(0, 0, mPaddingWidth, 0);
        // 初始化子控件
        initChildView(layout);
    }

    @Override
    protected final void initData(JSONObject jsonObject) {
        String label = getString(jsonObject, "label");
        mValue = getString(jsonObject, "value");
        // 设置标签内容
        mLabelView.setTextSize(mLabelSize);
        mLabelView.setTextColor(mLabelColor);
        mLabelView.setText(label);
        initChildData(jsonObject);
    }

    /**
     * 当前布局是否是水平布局
     * @return true: 是; false: 不是
     */
    protected boolean isHorizontal() {
        return true;
    }

    /**
     * 初始化文件
     */
    protected abstract void initChildView(ViewGroup parent);

    /**
     * 初始化内容
     */
    protected abstract void initChildData(JSONObject jsonObject);

}
