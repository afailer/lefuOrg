package com.lefuorgn.dialog;

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
import com.lefuorgn.util.StringUtils;


/**
 * 页面级别的等待框
 */

public final class WaitDialog extends DialogFragment {

    private TextView mMsgView;
    private String message;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        @SuppressLint("InflateParams")
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_fragment_wait, null);
        mMsgView = (TextView) view.findViewById(R.id.tv_dialog_fragment_wait);
        if(!StringUtils.isEmpty(message)) {
            mMsgView.setText(message);
        }
        Dialog dialog = builder.create();
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(view);
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                return keyCode == KeyEvent.KEYCODE_BACK;
            }
        });
        // 获取dialog的窗体对象
        Window window = dialog.getWindow();
        if(window != null) {
            // 获取窗体的参数配置对象
            // 获取手机页面的宽度
            WindowManager wManager = getActivity().getWindowManager();
            Display display = wManager.getDefaultDisplay();
            DisplayMetrics outMetrics = new DisplayMetrics();
            display.getMetrics(outMetrics);

            // 获取窗体的参数配置对象
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            int height = layoutParams.height;
            int width = (int) (outMetrics.widthPixels * 0.35);
            window.setLayout(width, height);
        }
        return dialog;
    }

    /**
     * 设置提示信息
     */
    public WaitDialog setMessage(String message) {
        this.message = message;
        if(mMsgView != null) {
            mMsgView.setText(message);
        }
        return this;
    }

}
