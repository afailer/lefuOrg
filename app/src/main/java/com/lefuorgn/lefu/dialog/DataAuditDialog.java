package com.lefuorgn.lefu.dialog;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnShowListener;
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
import android.widget.EditText;

import com.lefuorgn.R;

/**
 * 体征信息基本数据录入框
 */

public class DataAuditDialog extends DialogFragment {

    private EditText mEditView;

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
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_data_audit, null);
        view.findViewById(R.id.iv_dialog_data_audit_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        mEditView = (EditText) view.findViewById(R.id.et_dialog_data_audit);
        view.findViewById(R.id.btn_dialog_data_audit_not_pass)
                .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 不通过
                if(mClickCallBack != null) {
                    mClickCallBack.noPassClick(mEditView.getText().toString());
                }
                dismiss();
            }
        });
        view.findViewById(R.id.btn_dialog_data_audit_adopt)
                .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 通过
                if(mClickCallBack != null) {
                    mClickCallBack.passClick(mEditView.getText().toString());
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
        // 自动弹出软键盘
        dialog.setOnShowListener(new OnShowListener() {
            public void onShow(DialogInterface dialog) {
                InputMethodManager inputManager =
                        (InputMethodManager)mEditView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(mEditView, InputMethodManager.SHOW_IMPLICIT);
            }
        });
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
     * 设置按钮点击回调接口
     * @param callBack 回调接口函数对象
     * @return 当前对象
     */
    public DataAuditDialog setClickCallBack(ClickCallBack callBack) {
        mClickCallBack = callBack;
        return this;
    }


    /**
     * 点击事件触发接口回调函数
     */
    public interface ClickCallBack {

        /**
         * 跳转到外部设备采集事件触发
         * @param value 备注
         */
        void noPassClick(String value);

        /**
         * 保存数据事件触发
         * @param value 备注
         */
        void passClick(String value);

    }

}
