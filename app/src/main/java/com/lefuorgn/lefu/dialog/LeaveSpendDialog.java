package com.lefuorgn.lefu.dialog;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
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
import android.widget.TextView;

import com.lefuorgn.R;
import com.lefuorgn.dialog.DatePickerDialog;
import com.lefuorgn.util.StringUtils;
import com.lefuorgn.util.ToastUtils;

/**
 * 体征信息基本数据录入框
 */

public class LeaveSpendDialog extends DialogFragment {

    private TextView mTimeView;

    private DatePickerDialog mDialog; // 时间选择器
    private long mLimitTime; // 限制时间

    // 按钮点击回调事件
    private ClickCallBack mClickCallBack;

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
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_leave_spend, null);
        view.findViewById(R.id.iv_dialog_leave_spend_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        mTimeView = (TextView) view.findViewById(R.id.tv_dialog_leave_spend_date);
        mTimeView.setText(StringUtils.getFormatData(System.currentTimeMillis(), StringUtils.FORMAT));
        mTimeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mDialog == null) {
                    mDialog = new DatePickerDialog();
                    mDialog.setTitle("选择实际返回时间")
                        .setMinDate(System.currentTimeMillis())
                        .setClickCallBack(new DatePickerDialog.ClickCallBack() {
                            @Override
                            public void leftClick() {
                                mDialog.dismiss();
                            }

                            @Override
                            public void rightClick(long time) {
                                if(time <= mLimitTime) {
                                    ToastUtils.show(getContext(), "不能早于实际离院时间");
                                    return;
                                }
                                mTimeView.setText(StringUtils.getFormatData(time, StringUtils.FORMAT));
                                mDialog.dismiss();
                            }
                        });
                }
                mDialog.show(getFragmentManager(), "Dialog");
            }
        });
        view.findViewById(R.id.btn_dialog_leave_spend_cancel)
                .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 取消
                dismiss();
            }
        });
        view.findViewById(R.id.btn_dialog_leave_spend_confirm)
                .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 确认
                if(mClickCallBack != null) {
                    mClickCallBack.confirm(StringUtils.getFormatData(mTimeView.getText().toString(), StringUtils.FORMAT));
                }
                dismiss();
            }
        });
        Dialog dialog = builder.create();
        initDialogWindow(view, dialog);
        return dialog;
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
            // 取消dialog在软键盘之上的设置
            window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
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
        }
    }

    /**
     * 设置限制时间
     * @param limitTime 限制时间
     * @return 当前对象
     */
    public LeaveSpendDialog setLimitTime(long limitTime) {
        this.mLimitTime = limitTime;
        return this;
    }

    /**
     * 设置按钮点击回调接口
     * @param callBack 回调接口函数对象
     * @return 当前对象
     */
    public LeaveSpendDialog setClickCallBack(ClickCallBack callBack) {
        mClickCallBack = callBack;
        return this;
    }


    /**
     * 点击事件触发接口回调函数
     */
    public interface ClickCallBack {

        /**
         * 保存数据事件触发
         * @param time 销假时间
         */
        void confirm(long time);

    }

}
