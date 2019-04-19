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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lefuorgn.R;
import com.lefuorgn.util.StringUtils;

/**
 * 进度条对话框, 点击外部和返回键不消失
 */

public final class ProgressDialog extends DialogFragment {

    private String mTitle;
    private TextView mNoteView;
    private ProgressBar mProgressBar;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        @SuppressLint("InflateParams")
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_fragment_base, null);
        TextView titleView = (TextView) view.findViewById(R.id.tv_dialog_fragment_base_title);
        if(!StringUtils.isEmpty(mTitle)) {
            titleView.setText(mTitle);
        }
        mNoteView = (TextView) view.findViewById(R.id.tv_dialog_fragment_base_note);
        mProgressBar = (ProgressBar) view.findViewById(R.id.pb_dialog_fragment_base);
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
            int width = (int) (outMetrics.widthPixels * 0.8);
            // 获取窗体的参数配置对象
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            int height = layoutParams.height;
            window.setLayout(width, height);
        }
        return dialog;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public void setNote(String note) {
        if(mNoteView != null) {
            mNoteView.setText(note);
        }
    }

    public void setProgressBar(int progress, int max) {
        if(mProgressBar != null) {
            mProgressBar.setMax(max);
            mProgressBar.setProgress(progress);
        }

    }

}
