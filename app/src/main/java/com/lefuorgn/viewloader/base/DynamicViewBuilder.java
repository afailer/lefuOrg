package com.lefuorgn.viewloader.base;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.lefuorgn.R;

import org.json.JSONObject;

/**
 * 动态添加View, 视图构建器
 */

public abstract class DynamicViewBuilder extends ViewBuilder {

    private float mLabelSize; // 标签文字大小
    private int mLabelColor; // 标签文字颜色

    protected float mValueSize; // 值文字大小
    protected int mValueColor; // 值文字颜色

    private float mButtonSize; // 按钮文字大小

    protected LayoutParams mChildParams; //

    protected boolean checkBoxDelete; // 当前状态是否是删除状态
    protected OnButtonClickListener mListener;

    public DynamicViewBuilder(Context context, int paddingWidth, int paddingHeight,
                              float labelSize, int labelColor,
                              float valueSize, int valueColor, float buttonSize) {
        super(context, paddingWidth, paddingHeight);
        mLabelSize = labelSize;
        mLabelColor = labelColor;
        mValueSize = valueSize;
        mValueColor = valueColor;
        mButtonSize = buttonSize;
        mChildParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        mChildParams.weight = 1;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.builder_dynamic_view_parent;
    }

    @Override
    protected final void initView(View parent) {
        // 设置标签内边距
        parent.findViewById(R.id.ll_builder_dynamic_view_label)
                .setPadding(mPaddingWidth, mPaddingHeight, mPaddingWidth, mPaddingHeight);
        // 设置标签
        TextView labelView = (TextView) parent.findViewById(R.id.tv_builder_dynamic_view_label);
        labelView.setTextSize(mLabelSize);
        labelView.setTextColor(mLabelColor);
        labelView.setCompoundDrawablePadding(mPaddingWidth);
        CheckBox stopView = (CheckBox) parent.findViewById(R.id.cb_builder_dynamic_view);
        stopView.setTextSize(mLabelSize);
        stopView.setTextColor(mLabelColor);
        initLabelData(labelView, stopView);
        // 初始化按钮控件
        TextView upBtn = (TextView) parent.findViewById(R.id.btn_builder_dynamic_view_up);
        upBtn.setTextSize(mButtonSize);
        TextView downBtn = (TextView) parent.findViewById(R.id.btn_builder_dynamic_view_down);
        downBtn.setTextSize(mButtonSize);
        upBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener != null) {
                    mListener.onUpClick(v);
                }
            }
        });
        downBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener != null) {
                    mListener.onDownClick(v);
                }
            }
        });
        initButton(upBtn, downBtn);
        // 初始化子控件
        LinearLayout childParent = (LinearLayout) parent.findViewById(R.id.ll_builder_dynamic_view_child_parent);
        childParent.setPadding(mPaddingWidth, mPaddingHeight, mPaddingWidth, mPaddingHeight);
        initChildView(childParent, 0);
    }

    @Override
    protected final void initData(JSONObject jsonObject) {}

    /**
     * 初始化标签中的控件内容
     * @param labelView 标签控件内容
     * @param stopView 多选框控件
     */
    protected abstract void initLabelData(TextView labelView, CheckBox stopView);

    /**
     * 设置按钮信息
     * @param upBtn 最上面的按钮
     * @param downBtn 最下面的按钮
     */
    protected abstract void initButton(TextView upBtn, TextView downBtn);

    /**
     * 初始化文件
     */
    protected abstract void initChildView(ViewGroup parent, int index);

    /**
     * 添加按钮点击事件
     */
    public void setOnButtonClickListener(OnButtonClickListener listener) {
        mListener = listener;
    }

    public interface OnButtonClickListener {

        void onUpClick(View v);

        void onDownClick(View v);

        void onViewClick(long tag);

    }

}
