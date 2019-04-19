package com.lefuorgn.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.lefuorgn.R;
import com.lefuorgn.util.StringUtils;

import java.util.Calendar;

/**
 * 日期Dialog选择器
 */

public final class DatePickerDialog extends DialogFragment {

    /**
     * 当前控件只能显示日期(只能选择日期)
     */
    public static final int DATE = 0x00000000;
    /**
     * 当前控件只能显示时间(只能选择时间)
     */
    public static final int TIME = 0x00000004;
    /**
     * 当前控件显示日期和时间(日期和时间都可选择) 默认
     */
    public static final int DATE_TIME = 0x00000008;

    private String mTitle = "设置日期";

    private int mDisplay = DATE_TIME; // 当前dialog页面显示的控件(日期、时间、俩者都显示)默认都显示
    private boolean showRightBtn = true; // 是否显示右侧按钮
    private boolean showLeftBtn = true; // 是否显示左侧按钮
    private String mRightText = "确认"; // 右侧按钮默认显示内容
    private String mLeftText = "取消"; // 左侧按钮默认显示内容
    private boolean cancelOnTouchOutSide; // 点击其他区域是否消失
    private ClickCallBack mClickCallBack; // 按钮点击回调事件
    private boolean isMinDate; // 是否限制最小时间
    private long mMinDate; // 最小时间(能选择的最小时间)

    private DatePicker mDatePicker; // 日期控件
    private TimePicker mTimePicker; // 时间控件

    private long defaultTime; // 设置默认时间

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
        LayoutInflater inflater = (LayoutInflater) getContext()
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("InflateParams")
        View view = inflater.inflate(R.layout.dialog_date_picker, null);
        // 初始化标题
        TextView title = (TextView) view.findViewById(R.id.tv_dialog_date_picker_title);
        title.setText(mTitle);
        // 初始化日期和时间控件
        Calendar calendar = Calendar.getInstance();
        if(defaultTime > 0) {
            calendar.setTimeInMillis(defaultTime);
        }
        if(mDisplay == DATE_TIME) {
            // 日期和时间控件都显示
            initDatePicker(view, true, calendar);
            initTimePicker(view, true, calendar);
        }else if(mDisplay == DATE) {
            // 只显示日期控件
            initDatePicker(view, true, calendar);
            initTimePicker(view, false, null);
        }else if(mDisplay == TIME) {
            initDatePicker(view, false, null);
            initTimePicker(view, true, calendar);
        }else {
            throw new IllegalArgumentException("the mDisplay is must DATA、TIME or DATE_TIME");
        }
        // 初始化按钮控件
        initButton(view);
        Dialog dialog = builder.create();
        dialog.show();
        dialog.setCanceledOnTouchOutside(cancelOnTouchOutSide);
        dialog.setContentView(view);
        return dialog;
    }

    /**
     * 初始化日期控件
     */
    private void initDatePicker(View view, boolean show, Calendar calendar) {
        mDatePicker = (DatePicker) view.findViewById(R.id.dp_dialog_date_picker);
        if(!show) {
            // 不显示
            mDatePicker.setVisibility(View.GONE);
            return;
        }
        mDatePicker.init(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH), null);
        if(isMinDate) {
            mDatePicker.setMinDate(mMinDate);
        }
    }

    /**
     * 初始化时间控件
     */
    private void initTimePicker(View view, boolean show, Calendar calendar) {
        mTimePicker = (TimePicker) view.findViewById(R.id.tp_dialog_date_picker);
        if(!show) {
            // 不显示
            mTimePicker.setVisibility(View.GONE);
            return;
        }
        mTimePicker.setIs24HourView(true);
        mTimePicker.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
        mTimePicker.setCurrentMinute(calendar.get(Calendar.MINUTE));
    }

    /**
     * 初始化按钮控件
     */
    private void initButton(View view) {
        TextView leftButton = (TextView) view.findViewById(R.id.btn_dialog_date_picker_left);
        TextView rightButton = (TextView) view.findViewById(R.id.btn_dialog_date_picker_right);
        // 设置确认按钮
        if(showRightBtn) {
            rightButton.setText(mRightText);
            rightButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mClickCallBack != null) {
                        Calendar calendar = Calendar.getInstance();
                        if(mDisplay == DATE_TIME) {
                            // 日期和时间控件都显示
                            calendar.set(mDatePicker.getYear(), mDatePicker.getMonth(),
                                    mDatePicker.getDayOfMonth(), mTimePicker.getCurrentHour(),
                                    mTimePicker.getCurrentMinute(), 0);
                        }else if(mDisplay == DATE) {
                            // 只显示日期控件
                            calendar.set(mDatePicker.getYear(), mDatePicker.getMonth(),
                                    mDatePicker.getDayOfMonth(), 0, 0, 0);
                        }else {
                            // 只显示时间控件
                            calendar.set(Calendar.HOUR_OF_DAY, mTimePicker.getCurrentHour());
                            calendar.set(Calendar.MINUTE, mTimePicker.getCurrentMinute());
                            calendar.set(Calendar.SECOND, 0);
                        }
                        calendar.set(Calendar.MILLISECOND, 0);
                        mClickCallBack.rightClick(calendar.getTimeInMillis());
                    }
                }
            });
        }else {
            rightButton.setVisibility(View.GONE);
        }
        // 设置取消按钮
        if(showLeftBtn) {
            leftButton.setText(mLeftText);
            leftButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mClickCallBack != null) {
                        mClickCallBack.leftClick();
                    }
                }
            });
        }else {
            leftButton.setVisibility(View.GONE);
        }
        if(!showRightBtn && !showLeftBtn) {
            // 俩个按钮都不显示, 则去掉最低布局, 并将内容设置成最下面的布局
            view.findViewById(R.id.ll_dialog_date_picker).setVisibility(View.GONE);
            mDatePicker.setBackgroundResource(R.drawable.dialog_fragment_fragment_text_bottom);
        }
    }

    /**
     * 设置显示模式, 默认日期和时间都显示
     * @param display One of {@link #DATE}, {@link #TIME}, or {@link #DATE_TIME}.
     * @return 当前对象
     */
    public DatePickerDialog setDisplay(int display) {
        this.mDisplay = display;
        return this;
    }

    /**
     * 设置按钮点击回调接口
     * @param callBack 回调接口函数对象
     * @return 当前对象
     */
    public DatePickerDialog setClickCallBack(ClickCallBack callBack) {
        mClickCallBack = callBack;
        return this;
    }

    /**
     * 设置标题
     */
    public DatePickerDialog setTitle(String title) {
        this.mTitle = title;
        return this;
    }

    /**
     * 是否显示确认按钮
     * @param show true: 显示; false: 不显示
     * @return 当前对象
     */
    public DatePickerDialog isShowRightBtn(boolean show) {
        showRightBtn = show;
        return this;
    }

    /**
     * 是否显示取消按钮
     * @param show true: 显示; false: 不显示
     * @return 当前对象
     */
    public DatePickerDialog isShowLeftBtn(boolean show) {
        showLeftBtn = show;
        return this;
    }

    /**
     * 设置确认按钮的内容
     * @param text 要显示的内容
     * @return 当前对象
     */
    public DatePickerDialog setRightBtnText(String text) {
        if(StringUtils.isEmpty(text)) {
            mRightText = "确认";
            return this;
        }
        mRightText = text;
        return this;
    }

    /**
     * 设置取消按钮的内容
     * @param text 要显示的内容
     * @return 当前对象
     */
    public DatePickerDialog setLeftBtnText(String text) {
        if(StringUtils.isEmpty(text)) {
            mLeftText = "取消";
            return this;
        }
        mLeftText = text;
        return this;
    }

    /**
     * 设置点击其他区域是否消失
     * @param flag true: 消失; false: 不消失
     * @return 当前对象
     */
    public DatePickerDialog isCancelOutside(boolean flag) {
        cancelOnTouchOutSide = flag;
        return this;
    }

    /**
     * 设置日期可以选择的最小时间
     * @param minDate 最小时间
     * @return 当前对象
     */
    public DatePickerDialog setMinDate(long minDate) {
        this.isMinDate = true;
        this.mMinDate = minDate;
        return this;
    }

    /**
     * 设置默认显示时间
     * @param time 指定显示的时间
     * @return 当前对象
     */
    public DatePickerDialog setDefaultTime(long time) {
        if(time == 0) {
            time = System.currentTimeMillis();
        }
        this.defaultTime = time;
        return this;
    }

    /**
     * 点击事件触发接口回调函数
     */
    public interface ClickCallBack {

        /**
         * 左侧按钮点击事件
         */
        void leftClick();

        /**
         * 右侧按钮点击事件; 如果当前控件只获取时间, 则获取的time日期是当天的日期
         */
        void rightClick(long time);

    }

}
