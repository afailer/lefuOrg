package com.lefuorgn.lefu.util;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewParent;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.lefuorgn.R;
import com.lefuorgn.lefu.bean.Option;
import com.lefuorgn.util.StringUtils;
import com.lefuorgn.util.TLog;

import java.util.ArrayList;
import java.util.List;

/**
 * 评估表选项工具类
 */

public class EstimateUtils {

    /**
     * 单选状态标记
     */
    public static final int SINGLE_SELECTION = 1;
    /**
     * 多选状态标记
     */
    public static final int MULTISELECT = 2;

    // 用于存放控件的缓存
    private List<LinearLayout> mList;
    private Context context;
    private OnCheckedListener mOnCheckedListener;

    public EstimateUtils(Context context) {
        this.context = context;
        // 初始化缓存
        mList = new ArrayList<LinearLayout>();
    }

    /**
     * 多选框和单选框选择的监听状态
     */
    public interface OnCheckedListener {
        /**
         * 单选按钮点击的事件监听方法
         * @param position 当前位置
         * @param group 选项父控件
         * @param checkedId 是否选中
         */
        void onCheckedChanged(int position, RadioGroup group, int checkedId);
        /**
         * 多选按钮点击的事件监听方法
         * @param position 当前问题所在item的位置
         * @param checkBoxId 是从1开始的
         * @param buttonView 按钮对象
         * @param isChecked 是否选中
         */
        void onCheckedChanged(int position, int checkBoxId, CompoundButton buttonView, boolean isChecked);
    }

    public void setOnCheckedListener(OnCheckedListener onCheckedListener) {
        mOnCheckedListener = onCheckedListener;
    }

    /**
     * 获取单选的控件
     * @param questionNo 选型所属题目的题号
     * @param options 当前选项内容的集合
     * @param position 当前item的位置
     * @return 单选的控件
     */
    public RadioGroup getRadioView(int questionNo, List<Option> options, final int position) {
        if(mList.size() > position) {
            // 存在缓存, 则获取缓存数据
            LinearLayout layout = mList.get(position);
            if(layout instanceof RadioGroup) {
                RadioGroup group = (RadioGroup) layout;
                // 将其从它的父控件中移除
                ViewParent parent = group.getParent();
                if(parent != null && parent instanceof FrameLayout){
                    FrameLayout frameLayout = (FrameLayout) parent;
                    frameLayout.removeView(group);
                }
                return group;
            }
        }
        // 创建一个存放选项的容器
        RadioGroup radioGroup = new RadioGroup(context);
        radioGroup.setId(questionNo);
        // 设置其放置子空间的方式,为垂直布局
        radioGroup.setOrientation(LinearLayout.VERTICAL);
        // 设置其布局方式
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        LayoutParams childParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        int padding = StringUtils.dip2px(context, 6);
        radioGroup.setLayoutParams(params);
        radioGroup.setPadding(padding, 0, padding, 0);
        // 循环遍历为radioGroup添加相应的RadioButton
        for(int i = 0; i < options.size(); i++) {
            Option option = options.get(i);
            RadioButton radioButton = new RadioButton(context);
            radioButton.setId(questionNo* 10 + i + 1);
            radioButton.setText(option.getContent());
            radioButton.setChecked(option.getStatus() != 0);
            radioButton.setLayoutParams(childParams);
            radioButton.setButtonDrawable(context.getResources().getDrawable(android.R.color.transparent));
            radioButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.radio_button_selector, 0, 0, 0);
            radioButton.setCompoundDrawablePadding(padding / 2);
            radioButton.setPadding(padding, padding, padding, padding);
            radioGroup.addView(radioButton);
        }
        // 为容器添加监听事件
        radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                TLog.log("选项按钮被触发了" + position);
                if(mOnCheckedListener != null) {
                    mOnCheckedListener.onCheckedChanged(position, group, checkedId % 10);
                }
            }
        });
        mList.add(radioGroup);
        return radioGroup;
    }

    /**
     * 获取多选的控件
     * @param questionNo 选型所属题目的题号
     * @param options 当前选项内容的集合
     * @param position 当前item的位置
     * @return 多选控件
     */
    public View getMultiChoiceView (int questionNo, List<Option> options, final int position) {
        if(mList.size() > position) {
            LinearLayout linearLayout = mList.get(position);
            // 将其从它的父控件中移除
            ViewParent parent = linearLayout.getParent();
            if(parent != null && parent instanceof FrameLayout){
                FrameLayout frameLayout = (FrameLayout) parent;
                frameLayout.removeView(linearLayout);
            }
            return linearLayout;
        }
        // 创建一个存放多选选项的容器
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setId(questionNo);
        // 设置其放置子空间的方式,为垂直布局
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        // 设置其布局方式
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        linearLayout.setLayoutParams(params);
        // 循环遍历为radioGroup添加相应的RadioButton
        for(int i = 0; i < options.size(); i++) {
            Option option = options.get(i);
            final CheckBox checkBox = new CheckBox(context);
            checkBox.setId(i + 1);
            checkBox.setText(option.getContent());
            checkBox.setChecked(option.getStatus() != 0);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(mOnCheckedListener != null) {
                        mOnCheckedListener.onCheckedChanged(position, checkBox.getId(), buttonView, isChecked);
                    }
                }
            });
            linearLayout.addView(checkBox);
        }
        // 为容器添加监听事件
        mList.add(linearLayout);
        return linearLayout;
    }
}
