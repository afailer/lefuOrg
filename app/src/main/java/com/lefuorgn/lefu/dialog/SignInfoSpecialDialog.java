package com.lefuorgn.lefu.dialog;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.lefuorgn.R;
import com.lefuorgn.util.StringUtils;
import com.lefuorgn.util.TLog;
import com.lefuorgn.util.ToastUtils;

import static com.lefuorgn.R.id.cb_dialog_sign_info_dinner;
import static com.lefuorgn.R.id.cb_dialog_sign_info_lunch;

/**
 * 体征信息特殊数据录入框; 包括进食和睡眠, 其中进食没有进行修改也是可以提交的
 */

public class SignInfoSpecialDialog extends DialogFragment {

    // 进食中的控件
    private CheckBox mEatAllView;
    private CheckBox mBreakfast;
    private CheckBox mLunchView;
    private CheckBox mDinnerView;

    // 按钮点击回调事件
    private ClickCallBack mClickCallBack;
    // dialog标题
    private String mTitle;
    // 输入框回显内容
    private String mContent = "";
    // 备注
    private String mReserved = "";


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setOnKeyListener(new DialogInterface.OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface dialog, int keyCode,
                                 KeyEvent event) {
                if(event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
                    dialog.dismiss();
                    return true;
                }
                return false;
            }

        });
        @SuppressLint("InflateParams")
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_sign_info_special, null);
        ((TextView) view.findViewById(R.id.tv_dialog_sign_info_special_title)).setText(mTitle);
        view.findViewById(R.id.iv_dialog_sign_info_special_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        // 初始化其他控件
        if("进食".equals(mTitle)) {
            view.findViewById(R.id.rl_dialog_sign_info_sleep).setVisibility(View.GONE);
            initEatView(view);
        }else {
            view.findViewById(R.id.ll_dialog_sign_info_eat).setVisibility(View.GONE);
            initSleepView(view);
        }
        Dialog dialog = builder.create();
        initDialogWindow(view, dialog);
        return dialog;
    }

    /**
     * 初始化进食控件
     * @param view 父View
     */
    private void initEatView(final View view) {
        // 初始化类别控件
        mBreakfast = (CheckBox) view.findViewById(R.id.cb_dialog_sign_info_breakfast);
        mLunchView = (CheckBox) view.findViewById(cb_dialog_sign_info_lunch);
        mDinnerView = (CheckBox) view.findViewById(cb_dialog_sign_info_dinner);
        mEatAllView = (CheckBox) view.findViewById(R.id.cb_dialog_sign_info_all);
        // 初始化食量控件
        final RadioGroup rg = (RadioGroup) view.findViewById(R.id.rg_dialog_sign_info_eat);
        // 初始化备注控件
        final CheckBox cbSnacks = (CheckBox) view.findViewById(R.id.cb_dialog_sign_info_snacks);
        final CheckBox cbMood = (CheckBox) view.findViewById(R.id.cb_dialog_sign_info_mood);
        final CheckBox cbDisease = (CheckBox) view.findViewById(R.id.cb_dialog_sign_info_disease);
        final EditText cbRemarks = (EditText) view.findViewById(R.id.et_dialog_sign_info_remarks_add);
        // 初始化数据
        if(!StringUtils.isEmpty(mContent)) {
            // 初始化类别
            String type = mContent.substring(0, 2);
            boolean typeFlag = true;
            if(mBreakfast.getText().equals(type)) {
                mBreakfast.setChecked(true);
                typeFlag = false;
            }
            if(typeFlag && mLunchView.getText().equals(type)) {
                mLunchView.setChecked(true);
                typeFlag = false;
            }
            if(typeFlag && mDinnerView.getText().equals(type)) {
                mDinnerView.setChecked(true);
            }
            // 初始化食量
            String intake = mContent.substring(2, 4);
            for (int i = 0; i < rg.getChildCount(); i++) {
                RadioButton rb = (RadioButton) rg.getChildAt(i);
                if(intake.equals(rb.getText())) {
                    rb.setChecked(true);
                    break;
                }
            }
            // 初始化备注
            if(!StringUtils.isEmpty(mReserved)) {
                String[] str = mReserved.split(" ");
                String[] content = str[0].split(",");
                for (String s : content) {
                    if (s.equals("零食")) {
                        cbSnacks.setChecked(true);
                    } else if (s.equals("心情")) {
                        cbMood.setChecked(true);
                    } else if (s.equals("疾病")) {
                        cbDisease.setChecked(true);
                    }
                }
                StringBuilder sb = new StringBuilder();
                for (int i = 1; i < str.length; i++) {
                    sb.append(str[i]);
                    if(i != str.length - 1) {
                        sb.append(" ");
                    }
                }
                cbRemarks.setText(sb.toString());
                cbRemarks.setSelection(cbRemarks.length());
            }
            TLog.log("修改页面");
            mEatAllView.setVisibility(View.INVISIBLE);
            mBreakfast.setOnClickListener(new UpdateCheckedChangeListener());
            mLunchView.setOnClickListener(new UpdateCheckedChangeListener());
            mDinnerView.setOnClickListener(new UpdateCheckedChangeListener());
        }else {
            TLog.log("添加页面");
            mBreakfast.setOnCheckedChangeListener(new SaveCheckedChangeListener());
            mLunchView.setOnCheckedChangeListener(new SaveCheckedChangeListener());
            mDinnerView.setOnCheckedChangeListener(new SaveCheckedChangeListener());
            mEatAllView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean isChecked = mEatAllView.isChecked();
                    // 全选按钮被点击了
                    mBreakfast.setChecked(isChecked);
                    mLunchView.setChecked(isChecked);
                    mDinnerView.setChecked(isChecked);
                }
            });
        }

        // 确定按钮点击事件
        view.findViewById(R.id.tv_dialog_sign_info_special_btn)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        StringBuilder sb = new StringBuilder();
                        // 获取进食类别信息
                        if(mBreakfast.isChecked()) {
                            sb.append(mBreakfast.getText().toString()).append(",");
                        }
                        if(mLunchView.isChecked()) {
                            sb.append(mLunchView.getText().toString()).append(",");
                        }
                        if(mDinnerView.isChecked()) {
                            sb.append(mDinnerView.getText().toString()).append(",");
                        }
                        if(sb.toString().endsWith(",")) {
                            sb.deleteCharAt(sb.length() - 1);
                            sb.append(";");
                        }else {
                            ToastUtils.show(getActivity(), "请选择类别");
                            return;
                        }
                        // 获取食量信息
                        RadioButton rb = (RadioButton) view.findViewById(rg.getCheckedRadioButtonId());
                        sb.append(rb.getText().toString()).append(";");
                        // 获取备注信息
                        if(cbSnacks.isChecked()) {
                            sb.append(cbSnacks.getText().toString()).append(",");
                        }
                        if(cbMood.isChecked()) {
                            sb.append(cbMood.getText().toString()).append(",");
                        }
                        if(cbDisease.isChecked()) {
                            sb.append(cbDisease.getText().toString()).append(",");
                        }
                        // 备注选项添加完毕
                        if(sb.toString().endsWith(",")) {
                            // 如果结尾有","分隔号,则去掉
                            sb.deleteCharAt(sb.length() - 1);
                        }
                        sb.append(" ").append(cbRemarks.getText().toString());
                        if(mClickCallBack != null) {
                            mClickCallBack.saveClick(sb.toString(), Color.GRAY);
                        }
                        SignInfoSpecialDialog.this.dismiss();
                    }
                });

    }

    /**
     * CheckBox选择监听事件
     */
    private class SaveCheckedChangeListener implements CompoundButton.OnCheckedChangeListener{

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            // 其他CheckBox被选择了
            if(isChecked) {
                if(mBreakfast.isChecked() && mLunchView.isChecked() && mDinnerView.isChecked()) {
                    mEatAllView.setChecked(true);
                }
            }else {
                mEatAllView.setChecked(false);
            }
        }
    }

    /**
     * CheckBox选择监听事件
     */
    private class UpdateCheckedChangeListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            // 其他CheckBox被选择了
            switch (v.getId()) {
                case R.id.cb_dialog_sign_info_breakfast:
                    mBreakfast.setChecked(true);
                    mLunchView.setChecked(false);
                    mDinnerView.setChecked(false);
                    break;
                case R.id.cb_dialog_sign_info_lunch:
                    mBreakfast.setChecked(false);
                    mLunchView.setChecked(true);
                    mDinnerView.setChecked(false);
                    break;
                case R.id.cb_dialog_sign_info_dinner:
                    mBreakfast.setChecked(false);
                    mLunchView.setChecked(false);
                    mDinnerView.setChecked(true);
                    break;
            }
        }
    }

    /**
     * 初始化睡眠控件
     * @param view 父View
     */
    private void initSleepView(final View view) {
        final RadioGroup rg = (RadioGroup) view.findViewById(R.id.rg_dialog_sign_info_sleep);
        final EditText eText = (EditText) view.findViewById(R.id.et_dialog_sign_info_other);
        if(!StringUtils.isEmpty(mContent)) {
            boolean flag = false;
            for (int i = 0; i < rg.getChildCount() - 1; i++) {
                RadioButton rb = (RadioButton) rg.getChildAt(i);
                if(mContent.equals(rb.getText())) {
                    rb.setChecked(true);
                    flag = true;
                    break;
                }
            }
            if(!flag) {
                RadioButton rb = (RadioButton) rg.getChildAt(rg.getChildCount() - 1);
                rb.setChecked(true);
                eText.setVisibility(View.VISIBLE);
                eText.setText(mContent);
                eText.setSelection(eText.length());
            }
        }
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.dialog_sign_info_other) {
                    eText.setVisibility(View.VISIBLE);
                    eText.setFocusable(true);
                    InputMethodManager inputManager = (InputMethodManager) getActivity()
                            .getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.showSoftInput(eText, InputMethodManager.SHOW_FORCED);
                }else {
                    eText.setVisibility(View.GONE);
                }
            }
        });
        initSleepClickListener(view, rg, eText);
    }

    /**
     * 睡眠确认按钮点击事件
     * @param view 睡眠布局
     * @param rg 单选布局
     * @param eText 其他信息输入框
     */
    private void initSleepClickListener(final View view, final RadioGroup rg, final EditText eText) {
        // 确定按钮点击事件
        view.findViewById(R.id.tv_dialog_sign_info_special_btn)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int id = rg.getCheckedRadioButtonId();
                        String content;
                        if(id == R.id.dialog_sign_info_other) {
                            content = eText.getText().toString();
                            InputMethodManager imm = (InputMethodManager) getActivity()
                                    .getSystemService(Context.INPUT_METHOD_SERVICE);
                            // 调用hideSoftInputFromWindow方法隐藏软键盘
                            imm.hideSoftInputFromWindow(eText.getWindowToken(), 0);
                        }else {
                            RadioButton rb = (RadioButton) view.findViewById(rg.getCheckedRadioButtonId());
                            content = rb.getText().toString();
                        }
                        if(StringUtils.isEmpty(content)) {
                            ToastUtils.show(getActivity(), "睡眠质量不能为空");
                            return;
                        }
                        if(content.equals(mContent)) {
                            ToastUtils.show(getActivity(), "内容没有修改");
                            return;
                        }
                        if(mClickCallBack != null) {
                            mClickCallBack.saveClick(content, Color.GRAY);
                        }
                        SignInfoSpecialDialog.this.dismiss();
                    }
                });
    }

    /**
     * 初始化Dialog界面信息
     * @param view 当前界面显示的View
     * @param dialog 当前Dialog
     */
    private void initDialogWindow(View view, Dialog dialog) {
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(view);
        // 获取dialog的窗体对象
        Window window = dialog.getWindow();
        if(window != null) {
            // 获取窗体的参数配置对象
            // 获取手机页面的宽度
            WindowManager wManager = getActivity().getWindowManager();
            Display display = wManager.getDefaultDisplay();
            DisplayMetrics outMetrics = new DisplayMetrics();
            display.getMetrics(outMetrics);
            int width = (int) (outMetrics.widthPixels * 0.8);
            // 获取窗体的参数配置对象
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            int height = layoutParams.height;
            window.setLayout(width, height);
            // 取消dialog在软键盘之上的设置
            window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        }
    }

    /**
     * 设置dialog的标题和内容
     * @param title 条目名称
     * @return 当前对象
     */
    public SignInfoSpecialDialog setTitle(String title) {
        mTitle = title;
        return this;
    }

    /**
     * 设置按钮点击回调接口
     * @param callBack 回调接口函数对象
     * @return 当前对象
     */
    public SignInfoSpecialDialog setClickCallBack(ClickCallBack callBack) {
        mClickCallBack = callBack;
        return this;
    }

    /**
     * 设置回显内容
     * @param value 内容值
     * @return 当前对象
     */
    public SignInfoSpecialDialog setContent(String value) {
        mContent = value;
        return this;
    }

    /**
     * 设置回显备注内容
     * @param reserved 备注
     * @return 当前对象
     */
    public SignInfoSpecialDialog setReserved(String reserved) {
        mReserved = reserved;
        return this;
    }

    /**
     * 点击事件触发接口回调函数
     */
    public interface ClickCallBack {

        /**
         * 保存数据事件触发
         * @param value 要保存的内容
         * @param color 当前内容的颜色
         */
        void saveClick(String value, int color);

    }

}
