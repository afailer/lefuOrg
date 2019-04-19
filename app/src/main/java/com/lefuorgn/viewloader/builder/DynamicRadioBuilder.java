package com.lefuorgn.viewloader.builder;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.lefuorgn.R;
import com.lefuorgn.util.StringUtils;
import com.lefuorgn.viewloader.base.DynamicViewBuilder;

/**
 * 动态添加单选框视图构建器
 */

public class DynamicRadioBuilder extends DynamicViewBuilder {

    private boolean mDynamic;

    private RadioGroup mRadioGroup;
    private TextView mValueView;
    private CheckBox mStopView;

    public DynamicRadioBuilder(Context context, int paddingWidth, int paddingHeight,
                               float labelSize, int labelColor,
                               float valueSize, int valueColor, float buttonSize,
                               boolean dynamic) {
        super(context, paddingWidth, paddingHeight, labelSize, labelColor, valueSize, valueColor, buttonSize);
        mDynamic = dynamic;
    }

    @Override
    protected void initLabelData(TextView labelView, CheckBox stopView) {
        labelView.setText("审批人");
        labelView.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.apply_s, 0, 0, 0);
        mStopView = stopView;
        mStopView.setText("终止审批");
    }

    @Override
    protected void initButton(TextView upBtn, TextView downBtn) {
        if(mDynamic) {
            upBtn.setText("添加");
            downBtn.setText("减少");
        }else {
            upBtn.setVisibility(View.GONE);
            downBtn.setVisibility(View.GONE);
        }
    }

    @Override
    protected void initChildView(ViewGroup parent, int index) {
        if(mDynamic) {
            // 动态添加
            HorizontalScrollView hsv = new HorizontalScrollView(mContext);
            hsv.setLayoutParams(mChildParams);
            hsv.setPadding(0, 0, mPaddingWidth, 0);
            hsv.setHorizontalScrollBarEnabled(false);
            mRadioGroup = new RadioGroup(mContext);
            mRadioGroup.setOrientation(LinearLayout.HORIZONTAL);
            hsv.addView(mRadioGroup);
            parent.addView(hsv, index);
        }else {
            // 固定的
            mValueView = new TextView(mContext);
            mValueView.setLayoutParams(mChildParams);
            mValueView.setPadding(0, mPaddingHeight, 0, mPaddingHeight);
            mValueView.setTextSize(mValueSize);
            mValueView.setTextColor(mValueColor);
            parent.addView(mValueView, index);
        }
    }

    /**
     * 设置固定控件内容
     * @param text 用户名称
     * @param userId 用户ID
     */
    public void setTextView(CharSequence text, long userId) {
        if(mValueView != null) {
            mValueView.setText(text);
            mValueView.setTag(userId);
        }
    }

    /**
     * 显示停止控件
     * @param show true: 显示; false: 不显示
     */
    public void showStopView(boolean show) {
        if(mStopView != null) {
            mStopView.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    /**
     * 是否终止
     */
    public boolean isStop() {
        return !(mStopView == null || mStopView.getVisibility() == View.GONE) && mStopView.isChecked();
    }

    /**
     * 添加单选框控件
     * @param viewId 控件ID
     * @param name 用户名称
     * @param userId 用户ID
     * @param checked 当前状态是否被选中
     */
    public void addRadioButton(int viewId, String name, long userId, boolean checked) {
        if(mRadioGroup == null) {
            return;
        }
        int padding = StringUtils.dip2px(mContext, 6);
        RadioButton button = (RadioButton) mInflater
                .inflate(R.layout.builder_label_radio_button, mRadioGroup, false);
        button.setId(viewId);
        button.setTextSize(mValueSize);
        button.setTextColor(mValueColor);
        button.setText(name);
        button.setChecked(checked);
        button.setPadding(padding, padding, padding, padding);
        button.setTag(userId);
        mRadioGroup.addView(button);
    }

    /**
     * 控件的选择状态和删除状态切换
     * @param delete true: 当前状态是删除状态; false: 当前状态是选择状态
     */
    public void switchView(boolean delete) {
        if(checkBoxDelete == delete) {
            return;
        }
        mRadioGroup.clearCheck();
        checkBoxDelete = delete;
        for (int i = 0; i < mRadioGroup.getChildCount(); i++) {
            View view = mRadioGroup.getChildAt(i);
            if(view instanceof RadioButton) {
                RadioButton radioButton = (RadioButton) view;
                radioButton.setChecked(false);
                radioButton.setCompoundDrawablesWithIntrinsicBounds(
                        delete ? R.mipmap.delete_button : R.drawable.radio_button_selector, 0, 0, 0);
                if(checkBoxDelete) {
                    // 删除状态, 添加状态变化监听, 并移除当前被点击的控件
                    radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if(checkBoxDelete && mListener != null) {
                                mListener.onViewClick((Long) buttonView.getTag());
                                mRadioGroup.removeView(buttonView);
                            }
                        }
                    });
                }else {
                    // 移除当前控件的监听
                    radioButton.setOnCheckedChangeListener(null);
                }
            }
        }
    }

    /**
     * 移除所有的单选控件
     */
    public void removeAllViews() {
        if(mRadioGroup == null) {
            return;
        }
        mRadioGroup.clearCheck();
        mRadioGroup.removeAllViews();
    }

    /**
     * 获取被选中控件的绑定用户的ID
     * @return 选中状态的用户的ID, 如果不存在则返回0
     */
    public long getValue() {
        if(mValueView != null) {
            return (Long) mValueView.getTag();
        }
        if(mRadioGroup != null) {
            int viewId = mRadioGroup.getCheckedRadioButtonId();
            if(viewId == -1) {
                return 0;
            }
            View view = mRadioGroup.findViewById(viewId);
            if(view == null) {
                return 0;
            }
            return (Long) view.getTag();
        }
        return 0;
    }


}
