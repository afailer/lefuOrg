package com.lefuorgn.viewloader.builder;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.lefuorgn.R;
import com.lefuorgn.base.BaseActivity;
import com.lefuorgn.dialog.DatePickerDialog;
import com.lefuorgn.util.StringUtils;
import com.lefuorgn.viewloader.base.LabelViewBuilder;
import com.lefuorgn.viewloader.util.BuilderUtils;

import org.json.JSONObject;

/**
 * 日期视图构建器
 */

public class DateViewBuilder extends LabelViewBuilder {

    private float mButtonSize; // 按钮文本大小

    private TextView mButton;
    private TextView mValueView;

    private String mFormat;
    private DatePickerDialog mDatePickerDialog;

    public DateViewBuilder(Context context, int paddingWidth, int paddingHeight,
                           float labelSize, int labelColor, float valueSize, int valueColor,
                           float describeSize, int describeColor, int describeBackground, float buttonSize) {
        super(context, paddingWidth, paddingHeight, labelSize, labelColor, valueSize, valueColor,
                describeSize, describeColor, describeBackground);
        this.mButtonSize = buttonSize;
    }

    @Override
    protected void initChildView(ViewGroup parent) {
        // 创建按钮
        mButton = (TextView) mInflater.inflate(R.layout.builder_label_button, parent, false);
        mButton.setText("选择");
        mButton.setTextSize(mButtonSize);
        // 创建值显示控件
        mValueView = new TextView(parent.getContext());
        mValueView.setTextSize(mValueSize);
        mValueView.setTextColor(mValueColor);
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.weight = 1;
        params.setMargins(0, 0, StringUtils.dip2px(mContext, 4), 0);
        mValueView.setLayoutParams(params);
        mValueView.setGravity(Gravity.END);
        parent.addView(mValueView);
        parent.addView(mButton);
    }

    @Override
    protected void initValue(String value) {

    }

    @Override
    protected void initChildData(JSONObject jsonObject) {
        // 获取日期格式
        String format = getString(jsonObject, "format");
        mFormat = BuilderUtils.getFormat(format);
        // 初始化日期显示内容控件
        if(StringUtils.isEmpty(mValue)) {
            if(getBoolean(jsonObject, "systemDate")) {
                // 显示系统时间
                mValueView.setText(StringUtils.getFormatData(System.currentTimeMillis(), mFormat));
            }
        }else {
            mValueView.setText(mValue);
        }
        // 获取按钮的只读状态
        if(getBoolean(jsonObject, "readOnly")) {
            // 只读
            mButton.setVisibility(View.GONE);
        }else {
            final int disPlay = BuilderUtils.getDisplay(format);
            mButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDateDialog(disPlay);
                }
            });
        }
    }

    /**
     * 显示时间选择控件
     */
    private void showDateDialog(int display) {
        if(mDatePickerDialog == null) {
            mDatePickerDialog = new DatePickerDialog();
            mDatePickerDialog.setTitle("选择时间")
                    .setDisplay(display)
                    .setMinDate(1388550537133L)
                    .setClickCallBack(new DatePickerDialog.ClickCallBack() {
                @Override
                public void leftClick() {
                    mDatePickerDialog.dismiss();
                }

                @Override
                public void rightClick(long time) {
                    mValueView.setText(StringUtils.getFormatData(time, mFormat));
                    mDatePickerDialog.dismiss();
                }
            });
        }
        String timeStr = mValueView.getText().toString();
        if(!StringUtils.isEmpty(timeStr)) {
            long time;
            if(StringUtils.FORMAT_TIME.equals(mFormat)) {
                // 时间类型
                timeStr = StringUtils
                        .getFormatData(System.currentTimeMillis(), StringUtils.FORMAT_DATE) + " " + timeStr;
                time = StringUtils.getFormatData(timeStr, StringUtils.FORMAT_DATE_TIME);
                mDatePickerDialog.setDefaultTime(time);
            }else {
                time = StringUtils.getFormatData(timeStr, mFormat);
                mDatePickerDialog.setDefaultTime(time);
            }
        }
        mDatePickerDialog.show(((BaseActivity) mContext).getSupportFragmentManager(), "DateViewBuilder");
    }

    @Override
    public String getValue() {
        return mValueView.getText().toString();
    }
}
