package com.lefuorgn.viewloader.builder;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lefuorgn.R;
import com.lefuorgn.base.BaseActivity;
import com.lefuorgn.dialog.DatePickerDialog;
import com.lefuorgn.util.StringUtils;
import com.lefuorgn.util.ToastUtils;
import com.lefuorgn.viewloader.base.LabelViewBuilder;
import com.lefuorgn.viewloader.util.BuilderUtils;

import org.json.JSONObject;

/**
 * 日期视图构建器
 */

public class DateRangeViewBuilder extends LabelViewBuilder {

    private float mButtonSize; // 按钮文本大小

    private TextView mStartLabel; // 第一行标签控件
    private TextView mStartValue; // 第一行值控件
    private TextView mStartButton; // 第一行按钮控件
    private TextView mEndLabel; // 第二行标签控件
    private TextView mEndValue; // 第二行值控件
    private TextView mEndButton; // 第二行按钮控件
    private View mResultContainer; // 计算结果容器
    private TextView mResultView; // 计算结果控件

    private String mFormat;
    private boolean calculation; // 是否显示计算结果
    private DatePickerDialog mStartDialog;
    private DatePickerDialog mEndDialog;

    public DateRangeViewBuilder(Context context, int paddingWidth, int paddingHeight,
                                float labelSize, int labelColor, float valueSize, int valueColor,
                                float describeSize, int describeColor, int describeBackground,
                                float buttonSize) {
        super(context, paddingWidth, paddingHeight, labelSize, labelColor, valueSize, valueColor,
                describeSize, describeColor, describeBackground);
        this.mButtonSize = buttonSize;
    }

    @Override
    protected void initChildView(ViewGroup parent) {
        // 创建按钮
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

    /**
     * 初始化开始时间控件
     */
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
        mStartButton = (TextView) parent.findViewById(R.id.btn_builder_date);
        mStartButton.setText("选择");
        mStartButton.setTextSize(mButtonSize);
    }

    /**
     * 初始化结束时间控件
     */
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
        mEndButton = (TextView) parent.findViewById(R.id.btn_builder_date);
        mEndButton.setText("选择");
        mEndButton.setTextSize(mButtonSize);
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
    protected void initValue(String value) {
        String[] date = value.split(",");
        if(date.length == 2) {
            mStartValue.setText(date[0]);
            mEndValue.setText(date[1]);
        }
    }

    @Override
    protected void initChildData(JSONObject jsonObject) {
        // 获取日期格式
        String format = getString(jsonObject, "format");
        mFormat = BuilderUtils.getFormat(format);
        final int display = BuilderUtils.getDisplay(format);
        // 设置标签
        final String startLabel = getString(jsonObject, "startLabel");
        mStartLabel.setText(startLabel);
        mEndLabel.setText(getString(jsonObject, "endLabel"));
        // 设置是否显示计算结果
        calculation = getBoolean(jsonObject, "duration");
        if(calculation) {
            mResultContainer.setVisibility(View.VISIBLE);
            mResultView.setText(BuilderUtils.getCalculation(mStartValue, mEndValue, mFormat));
        }else {
            mResultContainer.setVisibility(View.GONE);
        }
        // 设置点击事件
        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showStartDateDialog(display);
            }
        });
        mEndButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(StringUtils.isEmpty(mStartValue.getText().toString())) {
                    ToastUtils.show(mContext, "请选择" + startLabel + "时间");
                    return;
                }
                showEndDateDialog(display, startLabel);
            }
        });
    }

    /**
     * 显示时间选择控件
     */
    private void showStartDateDialog(int display) {
        if(mStartDialog == null) {
            mStartDialog = new DatePickerDialog();
            mStartDialog.setTitle("选择时间")
                    .setDisplay(display)
                    .setMinDate(1388550537133L)
                    .setClickCallBack(new DatePickerDialog.ClickCallBack() {
                @Override
                public void leftClick() {
                    mStartDialog.dismiss();
                }

                @Override
                public void rightClick(long time) {
                    mEndValue.setText("");
                    if(calculation) {
                        mResultView.setText("");
                    }
                    mStartValue.setText(StringUtils.getFormatData(time, mFormat));
                    mStartDialog.dismiss();
                }
            });
        }
        String timeStr = mStartValue.getText().toString();
        if(!StringUtils.isEmpty(timeStr)) {
            long time;
            if(StringUtils.FORMAT_TIME.equals(mFormat)) {
                // 时间类型
                timeStr = StringUtils
                        .getFormatData(System.currentTimeMillis(), StringUtils.FORMAT_DATE) + " " + timeStr;
                time = StringUtils.getFormatData(timeStr, StringUtils.FORMAT_DATE_TIME);
                mStartDialog.setDefaultTime(time);
            }else {
                time = StringUtils.getFormatData(timeStr, mFormat);
                mStartDialog.setDefaultTime(time);
            }
        }
        mStartDialog.show(((BaseActivity) mContext).getSupportFragmentManager(), "DateStartBuilder");
    }

    /**
     * 显示时间选择控件
     */
    private void showEndDateDialog(int display, final String startLabel) {
        if(mEndDialog == null) {
            mEndDialog = new DatePickerDialog();
            mEndDialog.setTitle("选择时间")
                    .setDisplay(display)
                    .setMinDate(1388550537133L)
                    .setClickCallBack(new DatePickerDialog.ClickCallBack() {
                        @Override
                        public void leftClick() {
                            mEndDialog.dismiss();
                        }

                        @Override
                        public void rightClick(long time) {
                            long startTime;
                            if(StringUtils.FORMAT_TIME.equals(mFormat)) {
                                String date = StringUtils.getFormatData(time, StringUtils.FORMAT_DATE);
                                date = date + " " + mStartValue.getText().toString();
                                startTime = StringUtils.getFormatData(date, StringUtils.FORMAT_DATE_TIME);
                            }else {
                                startTime = StringUtils.getFormatData(mStartValue.getText().toString(), mFormat);
                            }
                            if(time <= startTime) {
                                ToastUtils.show(mContext, "不能早于" + startLabel + "时间");
                                return;
                            }
                            mEndValue.setText(StringUtils.getFormatData(time, mFormat));
                            if(calculation) {
                                mResultView.setText(BuilderUtils
                                        .getCalculation(mStartValue, mEndValue, mFormat));
                            }
                            mEndDialog.dismiss();
                        }
                    });
        }
        String timeStr = mEndValue.getText().toString();
        if(!StringUtils.isEmpty(timeStr)) {
            long time;
            if(StringUtils.FORMAT_TIME.equals(mFormat)) {
                // 时间类型
                timeStr = StringUtils
                        .getFormatData(System.currentTimeMillis(), StringUtils.FORMAT_DATE) + " " + timeStr;
                time = StringUtils.getFormatData(timeStr, StringUtils.FORMAT_DATE_TIME);
                mEndDialog.setDefaultTime(time);
            }else {
                time = StringUtils.getFormatData(timeStr, mFormat);
                mEndDialog.setDefaultTime(time);
            }
        }
        mEndDialog.show(((BaseActivity) mContext).getSupportFragmentManager(), "DateEndBuilder");
    }

    @Override
    protected boolean isHorizontal() {
        return false;
    }

    @Override
    public String getValue() {
        String startTime = mStartValue.getText().toString();
        String endTime = mEndValue.getText().toString();
        if(StringUtils.isEmpty(startTime) || StringUtils.isEmpty(endTime)) {
            return "";
        }
        return startTime + "," + endTime;
    }
}
