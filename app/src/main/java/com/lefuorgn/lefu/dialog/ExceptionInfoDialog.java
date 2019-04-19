package com.lefuorgn.lefu.dialog;

import android.annotation.SuppressLint;
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
import android.widget.TextView;

import com.lefuorgn.R;
import com.lefuorgn.util.StringUtils;

/**
 * 今日工作数据录入异常说明dialog
 */

public final class ExceptionInfoDialog extends DialogFragment {

    // 按钮点击回调事件
    private ClickCallBack mClickCallBack;
    // 标题
    private String mTitle = "提示";
    // 提示内容
    private String mContent = "";
    // 是否显示确认按钮
    private boolean showConfirmBtn = true;
    // 是否显示取消按钮
    private boolean showCancelBtn = true;
    // 确认按钮显示内容
    private String mConfirmText = "确认";
    // 取消按钮显示内容
    private String mCancelText = "取消";
    // 点击其他区域是否消失
    private boolean cancelOnTouchOutSide;


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
        @SuppressLint("InflateParams")
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_exception_info, null);
        TextView title = (TextView) view.findViewById(R.id.tv_dialog_exception_info_title);
        title.setText(mTitle);
        final EditText content = (EditText) view.findViewById(R.id.tv_dialog_exception_info_content);
        content.setText(mContent);
        TextView leftButton = (TextView) view.findViewById(R.id.btn_dialog_exception_info_left);
        TextView rightButton = (TextView) view.findViewById(R.id.btn_dialog_exception_info_right);
        // 设置确认按钮
        if(showConfirmBtn) {
            rightButton.setText(mConfirmText);
            rightButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mClickCallBack != null) {
                        mClickCallBack.confirm(content.getText().toString());
                    }
                    dismiss();
                }
            });
        }else {
            rightButton.setVisibility(View.GONE);
        }
        // 设置取消按钮
        if(showCancelBtn) {
            leftButton.setText(mCancelText);
            leftButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mClickCallBack != null) {
                        mClickCallBack.cancel();
                    }
                    dismiss();
                }
            });
        }else {
            leftButton.setVisibility(View.GONE);
        }
        if(!showConfirmBtn && !showCancelBtn) {
            // 俩个按钮都不显示, 则去掉最低布局, 并将内容设置成最下面的布局
            view.findViewById(R.id.ll_dialog_exception_info).setVisibility(View.GONE);
            content.setBackgroundResource(R.drawable.dialog_fragment_fragment_text_bottom);
        }
        Dialog dialog = builder.create();
        initDialogWindow(view, dialog);
        return dialog;
    }

    /**
     * 设置按钮点击回调接口
     * @param callBack 回调接口函数对象
     * @return 当前对象
     */
    public ExceptionInfoDialog setClickCallBack(ClickCallBack callBack) {
        mClickCallBack = callBack;
        return this;
    }

    /**
     * 设置要提示的标题
     * @param title 标题的内容
     * @return 当前对象
     */
    public ExceptionInfoDialog setTitle(String title) {
        mTitle = title;
        return this;
    }

    /**
     * 设置要提示的内容
     * @param value 要提示的内容
     * @return 当前对象
     */
    public ExceptionInfoDialog setContent(String value) {
        mContent = value;
        return this;
    }

    /**
     * 是否显示确认按钮
     * @param show true: 显示; false: 不显示
     * @return 当前对象
     */
    public ExceptionInfoDialog isShowConfirmBtn(boolean show) {
        showConfirmBtn = show;
        return this;
    }

    /**
     * 是否显示取消按钮
     * @param show true: 显示; false: 不显示
     * @return 当前对象
     */
    public ExceptionInfoDialog isShowCancelBtn(boolean show) {
        showCancelBtn = show;
        return this;
    }

    /**
     * 设置确认按钮的内容
     * @param text 要显示的内容
     * @return 当前对象
     */
    public ExceptionInfoDialog setConfirmBtnText(String text) {
        if(StringUtils.isEmpty(text)) {
            return this;
        }
        mConfirmText = text;
        return this;
    }

    /**
     * 设置取消按钮的内容
     * @param text 要显示的内容
     * @return 当前对象
     */
    public ExceptionInfoDialog setCancelBtnText(String text) {
        if(StringUtils.isEmpty(text)) {
            return this;
        }
        mCancelText = text;
        return this;
    }

    /**
     * 设置点击其他区域是否消失
     * @param flag true: 消失; false: 不消失
     * @return 当前对象
     */
    public ExceptionInfoDialog isCancelOutside(boolean flag) {
        cancelOnTouchOutSide = flag;
        return this;
    }

    /**
     * 点击事件触发接口回调函数
     */
    public interface ClickCallBack {

        /**
         * 跳转到外部设备采集事件触发
         */
        void cancel();

        /**
         * 保存数据事件触发
         */
        void confirm(String value);

    }

    /**
     * 初始化Dialog界面信息
     * @param view 当前界面显示的View
     * @param dialog 当前Dialog
     */
    private void initDialogWindow(View view, Dialog dialog) {
        // 自动弹出软键盘
        dialog.show();
        dialog.setCanceledOnTouchOutside(cancelOnTouchOutSide);
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
        }
    }


}
