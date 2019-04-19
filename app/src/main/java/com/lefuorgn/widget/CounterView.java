package com.lefuorgn.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.lefuorgn.R;

/**
 * 计数器控件
 */

public class CounterView extends LinearLayout implements View.OnClickListener, TextWatcher {

    private static final int DEFAULT_VALUE = 0;

    private View lineTopView, lineBottomView;
    private View lineLeftView, lineCenterLeftView, lineCenterRightView, lineRightView;
    private Button mAddBtn, mReduceBtn;
    private EditText mValueView;

    private boolean fromCodeAdd;

    private int minValue = DEFAULT_VALUE; // 最小值
    private int amount  = DEFAULT_VALUE; // 默认值
    private OnCounterChangeListener mListener;

    public CounterView(Context context) {
        super(context);
        init(context, null);
    }

    public CounterView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CounterView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.view_counter, this);
        lineTopView = findViewById(R.id.view_counter_horizontal_top);
        lineBottomView = findViewById(R.id.view_counter_horizontal_bottom);
        lineLeftView = findViewById(R.id.view_counter_vertical_left);
        lineCenterLeftView = findViewById(R.id.view_counter_vertical_center_left);
        lineCenterRightView = findViewById(R.id.view_counter_vertical_center_right);
        lineRightView = findViewById(R.id.view_counter_vertical_right);
        mAddBtn = (Button) findViewById(R.id.view_counter_add);
        mReduceBtn = (Button) findViewById(R.id.view_counter_reduce);
        mAddBtn.setOnClickListener(this);
        mReduceBtn.setOnClickListener(this);
        mValueView = (EditText) findViewById(R.id.view_counter_value);
        mValueView.addTextChangedListener(this);

        int borderColor = Color.BLACK; // 边框颜色
        int textColor = Color.BLACK; // 输入框颜色
        int textSize = 0; // 文本大小
        Drawable addButtonBackground = null; // 增加按钮背景颜色
        Drawable reduceButtonBackground = null; // 减少按钮背景颜色

        if(attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.CounterView);
            minValue = typedArray.getInteger(R.styleable.CounterView_minValue, DEFAULT_VALUE);
            amount = typedArray.getInteger(R.styleable.CounterView_defaultValue, DEFAULT_VALUE);
            borderColor = typedArray.getColor(R.styleable.CounterView_borderColor, Color.BLACK);
            textColor = typedArray.getColor(R.styleable.CounterView_textColor, Color.BLACK);
            textSize = typedArray.getDimensionPixelSize(R.styleable.CounterView_textSize, 0);
            addButtonBackground = typedArray.getDrawable(R.styleable.CounterView_addButtonBackground);
            reduceButtonBackground = typedArray.getDrawable(R.styleable.CounterView_reduceButtonBackground);
            typedArray.recycle();
        }
        setBorderColor(borderColor);
        setAddButtonBackground(addButtonBackground);
        setReduceButtonBackground(reduceButtonBackground);
        setValueSize(textSize);
        setValue(amount);
        setValueColor(textColor);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        mValueView.clearFocus();
        if(id == R.id.view_counter_reduce) {
            // 减少按钮
            if(amount > minValue) {
                amount--;
                setValue(amount);
            }else {
                // 已经减少到最小值了
                return;
            }
        }else if(id == R.id.view_counter_add) {
            // 增加按钮
            amount++;
            setValue(amount);
        }else {
            return;
        }
        if(mListener != null) {
            mListener.onCounterChange(this, amount);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if(fromCodeAdd) {
            // 当前时间触发来自setValue时间触发
            fromCodeAdd = false;
            return;
        }
        if(s.toString().isEmpty()) {
            // 当输入框清空的时候就设置最小值
            setValue(minValue);
            if(mListener != null) {
                mListener.onCounterChange(this, amount);
            }
            return;
        }
        if (s.toString().startsWith("0") && s.toString().trim().length() > 1) {
            // 开始位置上的数是0,且还有其他数字
            setValue(Integer.valueOf(s.subSequence(1, s.toString().length()).toString()));
        }
        amount = Integer.valueOf(s.toString());
        // 设置减少按钮的状态
        if(amount == minValue) {
            mReduceBtn.setSelected(true);
        }else {
            mReduceBtn.setSelected(false);
        }
        if(mListener != null) {
            mListener.onCounterChange(this, amount);
        }
    }

    /**
     * 设置边框颜色
     * @param color 边框颜色
     */
    public void setBorderColor(int color) {
        if(lineTopView != null) {
            lineTopView.setBackgroundColor(color);
            lineBottomView.setBackgroundColor(color);
            lineLeftView.setBackgroundColor(color);
            lineCenterLeftView.setBackgroundColor(color);
            lineCenterRightView.setBackgroundColor(color);
            lineRightView.setBackgroundColor(color);
        }
    }

    /**
     * 设置增加按钮背景
     * @param drawable 背景
     */
    @SuppressWarnings("deprecation")
    public void setAddButtonBackground(Drawable drawable) {
        if(mAddBtn != null && drawable != null) {
            mAddBtn.setBackgroundDrawable(drawable);
        }
    }

    /**
     * 设置减少按钮背景
     * @param drawable 背景
     */
    @SuppressWarnings("deprecation")
    public void setReduceButtonBackground(Drawable drawable) {
        if(mReduceBtn != null && drawable != null) {
            mReduceBtn.setBackgroundDrawable(drawable);
        }
    }

    /**
     * 设置文本内容大小, 包括添加和减少按钮文本
     * @param textSize 文本不能小于0
     */
    public void setValueSize(int textSize) {
        if(mValueView != null && mAddBtn != null && mReduceBtn != null && textSize > 0) {
            mValueView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            mAddBtn.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            mReduceBtn.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        }
    }

    /**
     * 设置文本内容颜色, 包括添加和减少按钮文本
     * @param color 文本不能小于0
     */
    public void setValueColor(int color) {
        if(mValueView != null && mAddBtn != null && mReduceBtn != null) {
            mValueView.setTextColor(color);
            mAddBtn.setTextColor(color);
            mReduceBtn.setTextColor(color);
        }
    }

    /**
     * 设置输入框内容值
     * @param value 内容值为数字
     */
    public void setValue(int value) {
        amount = value;
        if(mValueView != null) {
            fromCodeAdd = true;
            // 获取输入框上一个值
            int currentValue;
            try {
                currentValue = Integer.valueOf(mValueView.getText().toString());
            } catch (NumberFormatException e) {
                // 输入框为空, 将其置为最小值
                currentValue = minValue;
            }
            if(value == minValue) {
                // 当前值为最小值, 将减少按钮置为不可操作
                mReduceBtn.setSelected(true);
            }else if(value > minValue && currentValue == minValue) {
                // 当前从最小值变大, 将减少按钮置为可操作状态
                mReduceBtn.setSelected(false);
            }
            String str = value + "";
            mValueView.setText(str);
            mValueView.setSelection(mValueView.length());
        }
    }

    /**
     * 设置值变化监听
     * @param l 接口
     */
    public void setOnCounterChangeListener(OnCounterChangeListener l) {
        mListener = l;
    }

    /**
     * 值变化监听接口
     */
    public interface OnCounterChangeListener {

        /**
         * 值变化监听
         * @param view 当前控件对象
         * @param amount 当前输入框值
         */
        void onCounterChange(View view, int amount);

    }

}
