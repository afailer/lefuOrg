package com.lefuorgn.viewloader.base;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lefuorgn.R;
import com.lefuorgn.util.StringUtils;

import org.json.JSONObject;


/**
 * 含有标签的视图构建器基类
 */

public abstract class LabelViewBuilder extends ViewBuilder {

    private String mId; // 前台后台唯一标识
    private String mLabel; // 标题
    private String mDescribe; // 描述
    private boolean mRequired; // 当前条目是否是必填项; true: 必填; false: 非必填
    protected String mValue; // 默认值

    protected float mValueSize; // 值文字大小
    protected int mValueColor; // 值文字颜色

    protected float mLabelSize; // label文字大小
    private float mLabelWarnSize; // label警示文字大小
    protected int mLabelColor; // label文字颜色

    private float mDescribeSize; // 条目描述内容大小
    private int mDescribeColor; // 条目描述内容颜色
    private int mDescribeBackground; // 条目描述内容背景颜色

    private LinearLayout mLinearLayout; // 当前条目的父控件

    private TextView mDescribeView; // 条目描述控件
    private TextView mLabelView;
    private View mWarningLineView; // 警告线

    /**
     *
     * @param context 环境上下文
     * @param paddingWidth 控件的左右内边距(单位DP)
     * @param paddingHeight 控件的上下内边距(单位DP)
     * @param labelSize 标签文字大小(单位是SP)
     * @param labelColor 标签文字颜色
     * @param valueSize 值文字大小
     * @param valueColor 值文字颜色
     * @param describeSize 条目描述内容大小(单位是SP)
     * @param describeColor 条目描述内容颜色
     * @param describeBackground 条目描述内容背景颜色
     */
    public LabelViewBuilder(Context context, int paddingWidth, int paddingHeight,
                            float labelSize, int labelColor, float valueSize, int valueColor,
                            float describeSize, int describeColor, int describeBackground) {
        super(context, paddingWidth, paddingHeight);
        mLabelSize = labelSize;
        mLabelWarnSize = labelSize - 4;
        mLabelColor = labelColor;
        mValueSize = valueSize;
        mValueColor = valueColor;
        mDescribeSize = describeSize;
        mDescribeColor = describeColor;
        mDescribeBackground = describeBackground;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.builder_label_view_parent;
    }

    @Override
    protected final void initView(View parent) {
        // 初始化包裹内容控件
        mLinearLayout = (LinearLayout) parent.findViewById(R.id.ll_builder_label_view_parent);
        mLinearLayout.setOrientation(isHorizontal() ? LinearLayout.HORIZONTAL : LinearLayout.VERTICAL);
        mLinearLayout.setPadding(mPaddingWidth, mPaddingHeight, mPaddingWidth, mPaddingHeight);
        if(isHorizontal()) {
            mLinearLayout.setGravity(Gravity.CENTER_VERTICAL);
        }
        // 初始化标签
        mLabelView = (TextView) parent.findViewById(R.id.tv_builder_label_view_parent);
        mLabelView.setTextSize(mLabelSize);
        mLabelView.setPadding(0, 0, mPaddingWidth, 0);
        // 初始化描述控件
        mDescribeView = (TextView) parent.findViewById(R.id.tv_builder_label_view_parent_describe);
        int top = StringUtils.sp2px(mContext, 3);
        mDescribeView.setPadding(mPaddingWidth, top, mPaddingWidth, top);
        mDescribeView.setTextColor(mDescribeColor);
        mDescribeView.setTextSize(mDescribeSize);
        mDescribeView.setBackgroundColor(mDescribeBackground);
        // 初始化警告线
        mWarningLineView = parent.findViewById(R.id.v_builder_label_view_parent_warning_line);
        // 初始化子控件
        initChildView(mLinearLayout);
    }

    @Override
    protected final void initData(JSONObject jsonObject) {
        mId = getString(jsonObject, "id");
        mLabel = getString(jsonObject, "label");
        mDescribe = getString(jsonObject, "describe");
        mRequired = getBoolean(jsonObject, "required");
        mValue = getString(jsonObject, "value");
        // 设置标签内容
        setLabel();
        // 设置条目描述内容
        setDescribe();
        initValue(mValue);
        initChildData(jsonObject);
    }

    /**
     * 设置label的值
     */
    private void setLabel() {
        if(mRequired) {
            // 当前条目必填
            String label = String.format("%s *(必填)", mLabel);
            int z = label.lastIndexOf("*");
            SpannableStringBuilder style = new SpannableStringBuilder(label);
            // 设置label字号、颜色
            style.setSpan(new AbsoluteSizeSpan(StringUtils.sp2px(mContext, mLabelSize)), 0,
                    z, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            style.setSpan(new ForegroundColorSpan(mLabelColor), 0, z, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            // 设置警告内容  *  字号、颜色
            style.setSpan(new AbsoluteSizeSpan(StringUtils.sp2px(mContext, mLabelWarnSize)), z,
                    z + 1, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            style.setSpan(new ForegroundColorSpan(Color.RED), z, z + 1, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            // 设置警告内容  (必填)  字号、颜色
            style.setSpan(new AbsoluteSizeSpan(StringUtils.sp2px(mContext, mLabelWarnSize)), z + 1,
                    label.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            style.setSpan(new ForegroundColorSpan(Color.parseColor("#D3D3D3")), z + 1, label.length(),
                    Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            mLabelView.setText(style);
        }else if(!StringUtils.isEmpty(mLabel)) {
            // 非必填
            SpannableStringBuilder style = new SpannableStringBuilder(mLabel);
            // 设置label字号
            style.setSpan(new AbsoluteSizeSpan(StringUtils.sp2px(mContext, mLabelSize)), 0,
                    mLabel.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            // 设置label颜色
            style.setSpan(new ForegroundColorSpan(mLabelColor), 0, mLabel.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            mLabelView.setText(style);
        }else {
            mLabelView.setText("");
        }
    }

    /**
     * 校验当前当前条目是否是必填并且已经完成
     * @return true: 非必填或者已经完成; false: 必填且未完成
     */
    public boolean isCompleted() {
        if(mRequired) {
            // 必填
            if(StringUtils.isEmpty(getValue())) {
                showWarningLine();
                return false;
            }else {
                hideWarningLine();
                return true;
            }
        }else {
            // 非必填
            hideWarningLine();
            return true;
        }
    }

    /**
     * 设置描述内容
     */
    private void setDescribe() {
        if(!StringUtils.isEmpty(mDescribe)) {
            mDescribeView.setVisibility(View.VISIBLE);
            mDescribeView.setText(Html.fromHtml("<font color='#5BDB99'>↓ </font>" + mDescribe));
        }else if(mDescribeView.getVisibility() == View.VISIBLE) {
            mDescribeView.setVisibility(View.GONE);
        }
    }

    /**
     * 显示警告线
     */
    private void showWarningLine() {
        if(mWarningLineView != null && mWarningLineView.getVisibility() == View.GONE) {
            mWarningLineView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 隐藏警告线
     */
    private void hideWarningLine() {
        if(mWarningLineView != null && mWarningLineView.getVisibility() == View.VISIBLE) {
            mWarningLineView.setVisibility(View.GONE);
        }
    }

    /**
     * 获取当前控件的位置
     */
    public int getPosition() {
        int[] location = new int[2];
        mWarningLineView.getLocationInWindow(location);
        return location[1] - mLinearLayout.getMeasuredHeight();
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
     * 根据指定的内容格式初始化Value值
     */
    protected abstract void initValue(String value);

    /**
     * 初始化内容
     */
    protected abstract void initChildData(JSONObject jsonObject);

    /**
     * 获取条目ID
     */
    public String getId() {
        return mId;
    }

    /**
     * 获取当前条目的值
     */
    public abstract String getValue();

}
