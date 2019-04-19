package com.lefuorgn.viewloader.builder;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.lefuorgn.R;
import com.lefuorgn.util.StringUtils;
import com.lefuorgn.viewloader.base.DynamicViewBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * 动态添加单选框视图构建器
 */

public class DynamicCheckBoxBuilder extends DynamicViewBuilder {

    private LinearLayout mChildParent;

    private int padding;

    private int mDynamicViewNum; // 固定控件的个数

    public DynamicCheckBoxBuilder(Context context, int paddingWidth, int paddingHeight,
                                  float labelSize, int labelColor, float valueSize,
                                  int valueColor, float buttonSize) {
        super(context, paddingWidth, paddingHeight,
                labelSize, labelColor, valueSize, valueColor, buttonSize);
        padding = StringUtils.dip2px(context, 6);
        mDynamicViewNum = 0;
    }

    @Override
    protected void initLabelData(TextView labelView, CheckBox stopView) {
        labelView.setText("抄送人");
        labelView.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.apply_c, 0, 0, 0);
        stopView.setVisibility(View.GONE);
    }

    @Override
    protected void initButton(TextView upBtn, TextView downBtn) {
        upBtn.setText("添加");
        downBtn.setText("减少");
    }

    @Override
    protected void initChildView(ViewGroup parent, int index) {
        HorizontalScrollView hsv = new HorizontalScrollView(mContext);
        hsv.setLayoutParams(mChildParams);
        hsv.setPadding(0, 0, mPaddingWidth, 0);
        hsv.setHorizontalScrollBarEnabled(false);
        mChildParent = new LinearLayout(mContext);
        mChildParent.setOrientation(LinearLayout.HORIZONTAL);
        mChildParent.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        hsv.addView(mChildParent);
        parent.addView(hsv, index);
    }

    /**
     * 添加固定控件
     * @param text 文本内容(用户名称)
     * @param userId 用户ID
     */
    public void addTextView(CharSequence text, long userId) {
        mDynamicViewNum++;
        TextView textView = new TextView(mContext);
        textView.setTextSize(mValueSize);
        textView.setTextColor(mValueColor);
        textView.setText(text);
        // 绑定当前用户的ID
        textView.setTag(userId);
        textView.setPadding(0, 0, padding, 0);
        mChildParent.addView(textView);
    }

    /**
     * 添加多选框
     * @param viewId 控件ID
     * @param name 用户名称
     * @param userId 用户ID
     * @param checked 是否被选中
     */
    public void addCheckBox(int viewId, CharSequence name, long userId, boolean checked) {
        if (mChildParent == null) {
            return;
        }
        for (int index = 0; index < mChildParent.getChildCount(); index++) {
            View view = mChildParent.getChildAt(index);
            // 如果该用户存在则返回
            if(userId == (Long) view.getTag()) {
                return;
            }
        }
        CheckBox checkBox = (CheckBox) mInflater
                .inflate(R.layout.builder_label_check_box, mChildParent, false);
        checkBox.setId(viewId);
        checkBox.setTextSize(mValueSize);
        checkBox.setTextColor(mValueColor);
        checkBox.setPadding(padding, padding, padding, padding);
        checkBox.setText(name);
        // 绑定当前用户的ID
        checkBox.setTag(userId);
        checkBox.setChecked(checked);
        mChildParent.addView(checkBox);
    }

    /**
     * 控件的选择状态和删除状态切换
     * @param delete true: 当前状态是删除状态; false: 当前状态是选择状态
     */
    public void switchView(boolean delete) {
        if(checkBoxDelete == delete) {
            return;
        }
        checkBoxDelete = delete;
        for (int i = 0; i < mChildParent.getChildCount(); i++) {
            View view = mChildParent.getChildAt(i);
            if(view instanceof CheckBox) {
                CheckBox checkBox = (CheckBox) view;
                checkBox.setChecked(false);
                checkBox.setCompoundDrawablesWithIntrinsicBounds(
                        delete ? R.mipmap.delete_button : R.drawable.check_box_selector, 0, 0, 0);
                if(checkBoxDelete) {
                    // 删除状态添加事件, 并移除当前控件
                    checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if(checkBoxDelete && mListener != null) {
                                mListener.onViewClick((Long) buttonView.getTag());
                                mChildParent.removeView(buttonView);
                            }
                        }
                    });
                }else {
                    // 非删除状态, 移除当前控件的事件
                    checkBox.setOnCheckedChangeListener(null);
                }
            }
        }
    }

    /**
     * 移除所有的多选框控件
     */
    public void removeAllCheckBox() {
        if(mChildParent != null && mChildParent.getChildCount() > mDynamicViewNum) {
            mChildParent.removeViews(mDynamicViewNum, mChildParent.getChildCount() - mDynamicViewNum);
        }
    }

    /**
     * 获取被选中控件的绑定用户的ID集合
     * @return 选中状态的用户的ID集合
     */
    public List<Long> getValue() {
        List<Long> result = new ArrayList<Long>();
        for (int index = 0; index < mChildParent.getChildCount(); index++) {
            View view = mChildParent.getChildAt(index);
            if(index < mDynamicViewNum || (view instanceof CheckBox && ((CheckBox) view).isChecked())) {
                long id = (Long) view.getTag();
                if(id != 0) {
                    result.add(id);
                }
            }
        }
        return result;
    }
}
