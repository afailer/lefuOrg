package com.lefuorgn.oa.dialog;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import com.lefuorgn.R;

/**
 * 审批驳回原因录入Dialog
 */

public class RejectApplyDialog extends DialogFragment {

    private EditText mEditView;
    private Callback callback;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        @SuppressLint("InflateParams")
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_reject_apply, null);
        mEditView = (EditText) view.findViewById(R.id.et_dialog_reject_apply);
        view.findViewById(R.id.btn_dialog_reject_apply).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(callback != null) {
                    callback.onItemClick(mEditView.getText().toString());
                }
                dismiss();
            }
        });
        Dialog dialog = builder.create();

        dialog.show();
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(view);
        // 获取dialog的窗体对象
        Window window = dialog.getWindow();
        if(window != null) {
            //只用下面这一行弹出对话框时需要点击输入框才能弹出软键盘
            window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
            //加上下面这一行弹出对话框时软键盘随之弹出
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            // 获取窗体的参数配置对象
            // 获取手机页面的宽度
            WindowManager wManager = getActivity().getWindowManager();
            Display display = wManager.getDefaultDisplay();
            DisplayMetrics outMetrics = new DisplayMetrics();
            display.getMetrics(outMetrics);

            // 获取窗体的参数配置对象
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            int height = layoutParams.height;
            int width = (int) (outMetrics.widthPixels * 0.62);
            window.setLayout(width, height);
        }
        return dialog;
    }

    public RejectApplyDialog setCallBack(Callback callBack) {
        this.callback = callBack;
        return this;
    }

    /**
     * 获取驳回原因监听
     */
    public interface Callback {

        /**
         * 题目数据点击事件
         */
        void onItemClick(String note);

    }

}
