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
import android.text.Editable;
import android.text.Html;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.lefuorgn.R;
import com.lefuorgn.db.model.basic.SignConfig;
import com.lefuorgn.db.util.DataProcessUtils;
import com.lefuorgn.util.StringUtils;
import com.lefuorgn.util.ToastUtils;

/**
 * 体征信息基本数据录入框
 */

public class SignInfoDialog extends DialogFragment implements View.OnClickListener{

    private EditText mEditView;
    // 内容控件和内容提示控件
    private View mContentView, mNoteView;
    private View mConfirmView;
    private TextView mNote;

    // 按钮点击回调事件
    private ClickCallBack mClickCallBack;
    // dialog标题
    private String mTitle;
    // 当前所需配置
    private SignConfig mSignConfig;
    // 是否显示设备录入按钮
    private boolean mShowDevice;
    // 输入框回显内容
    private String mContent = "";


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
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_sign_info, null);
        ((TextView) view.findViewById(R.id.tv_dialog_sign_info_title)).setText(mTitle);
        ((TextView) view.findViewById(R.id.tv_dialog_sign_info_content)).setText(mTitle);
        ((TextView) view.findViewById(R.id.tv_dialog_sign_info_unit)).setText(getUnit(mTitle));
        view.findViewById(R.id.iv_dialog_sign_info_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        mEditView = (EditText) view.findViewById(R.id.et_dialog_sign_info);
        // 有回显内容,则显示回显内容
        mEditView.setText(mContent);
        mEditView.setSelection(mEditView.length());
        mContentView = view.findViewById(R.id.ll_dialog_sign_info_content);
        mNoteView = view.findViewById(R.id.ll_dialog_sign_info_note);
        // 提示内容控件
        mNote = (TextView) view.findViewById(R.id.tv_dialog_sign_info_note);

        view.findViewById(R.id.tv_dialog_sign_info_cancel).setOnClickListener(this);
        mConfirmView = view.findViewById(R.id.tv_dialog_sign_info_confirm);

        view.findViewById(R.id.btn_dialog_sign_info_input).setOnClickListener(this);
        if(mShowDevice && isShowDevice(mTitle)) {
            view.findViewById(R.id.btn_dialog_sign_info_device).setOnClickListener(this);
        }else {
            view.findViewById(R.id.btn_dialog_sign_info_device).setVisibility(View.GONE);
        }
        if(mSignConfig != null) {
            // 获取当前数值允许小数点后输入的数字个数
            int accur = (int) mSignConfig.getAccur();
            // 判断当前体征数据的类型
            // 默认的输入类型为double数字类型
            if("血压".equals(mTitle)) {
                // 血压
                // 血压要包含特殊字符,故文本输入类型为text
                mEditView.setInputType(InputType.TYPE_CLASS_TEXT);
            }else if(accur == 0) {
                // 小数点后不能有数据则不能添加小数点
                mEditView.setInputType(InputType.TYPE_CLASS_NUMBER);
                setPricePoint(mEditView, accur);
            }else {
                // 其他情况进行校验
                setPricePoint(mEditView, accur);
            }
        }
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_dialog_sign_info_input:
                checkEditText();
                break;
            case R.id.btn_dialog_sign_info_device:
                if(mClickCallBack != null) {
                    mClickCallBack.deviceClick();
                }
                dismiss();
                break;
            case R.id.tv_dialog_sign_info_cancel:
                mContentView.setVisibility(View.VISIBLE);
                mNoteView.setVisibility(View.GONE);
                mNote.setText("");
                break;
        }
    }

    /**
     * 校验输入框
     */
    private void checkEditText() {
        String value = mEditView.getText().toString().trim();
        if(StringUtils.isEmpty(value)) {
            ToastUtils.show(getActivity(), "输入内容不能为空");
            return;
        }
        if(!StringUtils.isEmpty(mContent) && mContent.equals(value)) {
            ToastUtils.show(getActivity(), "内容没有修改");
            return;
        }
        if("血压".equals(mTitle)) {
            checkPressureData(value);
        }else {
            checkData(value);
        }

    }

    /**
     * 校验输入框中获取的数据
     * @param value 校验数据
     */
    private void checkPressureData(String value) {
        if(!value.matches(DataProcessUtils.getPressureRegex())) {
            ToastUtils.show(getActivity(), "输入格式不对,如129/78");
            return;
        }
        // 切割字符串, 下标0是低血压; 下标1是高血压
        String[] array = DataProcessUtils.getBloodPressureArray(value);
        int high, low;
        try {
            high = Integer.parseInt(array[1]);
            low = Integer.parseInt(array[0]);
        }catch (Exception ex) {
            ToastUtils.show(getActivity(), "输入格式不对,如129/78");
            return;
        }
        if(array.length != 2) {
            ToastUtils.show(getActivity(), "输入格式不对,如129/78");
            return;
        }else if(Integer.valueOf(array[0]) > Integer.valueOf(array[1])) {
            ToastUtils.show(getActivity(), "收缩压必须大于舒张压");
            return;
        }
        if(high <= mSignConfig.getInputMax() && low >= mSignConfig.getInputMin()) {
            // bloodPressure符合要求
            if(high - low >= mSignConfig.getDiff()){
                final String content = high + "/" + low;
                final int color = DataProcessUtils.getPressureColor(high, low, mSignConfig);
                if(high > mSignConfig.getConfirmMax() || low < mSignConfig.getConfirmMin()) {
                    // 数据不正常
                    mContentView.setVisibility(View.GONE);
                    mNoteView.setVisibility(View.VISIBLE);
                    mNote.setText(Html.fromHtml("你测量的值是: <font color='red'>" + value + "</font>"));
                    mConfirmView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mClickCallBack.saveClick(content, color);
                            dismiss();
                        }
                    });
                }else {
                    // 数据正常
                    if(mClickCallBack != null) {
                        mClickCallBack.saveClick(content, color);
                        dismiss();
                    }
                }
            }else{
                ToastUtils.show(getActivity(), "压差必须大于"
                        + getString(0, Float.parseFloat(mSignConfig.getDiff() + "")));
            }

        }
        else {
            ToastUtils.show(getActivity(), "血压的输入范围是: "
                    + mSignConfig.getInputMin() + "~" + mSignConfig.getInputMax());
        }
    }

    /**
     * 校验输入框中获取的数据
     * @param value 校验数据
     */
    private void checkData(String value) {
        double d;
        try {
            d = Double.valueOf(value);
        }catch (Exception e) {
            ToastUtils.show(getActivity(), "非法字符");
            return;
        }
        final double data = d;
        if(mSignConfig == null) {
            ToastUtils.show(getActivity(), "没有指定的配置文件");
            return;
        }
        if(data <= mSignConfig.getInputMax() && data >= mSignConfig.getInputMin()) {
            if(data > mSignConfig.getConfirmMax() || data < mSignConfig.getConfirmMin()) {
                // 数据不正常
                mContentView.setVisibility(View.GONE);
                mNoteView.setVisibility(View.VISIBLE);
                mNote.setText(Html.fromHtml("你测量的值是: <font color='red'>" + data + "</font>"));
                mConfirmView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mClickCallBack.saveClick(getContent(data, mSignConfig),
                                DataProcessUtils.getColor(data, mSignConfig));
                        dismiss();
                    }
                });

            }else {
                // 数据正常
                if(mClickCallBack != null) {
                    mClickCallBack.saveClick(getContent(data, mSignConfig),
                            DataProcessUtils.getColor(data, mSignConfig));
                    dismiss();
                }
            }
        }else {
            ToastUtils.show(getActivity(), mTitle + "的输入范围是: "
                    + mSignConfig.getInputMin() + "~" + mSignConfig.getInputMax());
        }
    }

    /**
     * 获取要显示的内容
     * @param value 内容值
     * @param config 配置
     * @return 内容
     */
    private String getContent(double value, SignConfig config) {
        return StringUtils.numberFormat((int) config.getAccur(), value);
    }

    /**
     * 设置dialog的标题和内容
     * @param title 条目名称
     * @return 当前对象
     */
    public SignInfoDialog setTitle(String title) {
        mTitle = title;
        return this;
    }

    /**
     * 设置按钮点击回调接口
     * @param callBack 回调接口函数对象
     * @return 当前对象
     */
    public SignInfoDialog setClickCallBack(ClickCallBack callBack) {
        mClickCallBack = callBack;
        return this;
    }

    /**
     * 添加当前页面所需的配置文件
     * @param signConfig 配置文件
     * @return 当前对象
     */
    public SignInfoDialog setSignConfig(SignConfig signConfig) {
        mSignConfig = signConfig;
        return this;
    }

    /**
     * 是否允许血压和血糖显示设备采集按钮, 默认不显示
     * @param all true: 显示; false: 不显示
     * @return 当前对象
     */
    public SignInfoDialog setShowDevice(boolean all) {
        mShowDevice = all;
        return this;
    }

    /**
     * 设置数据回显内容
     * @param value 要回显的内容
     * @return 当前对象
     */
    public SignInfoDialog setContent(String value) {
        mContent = value;
        return this;
    }

    /**
     * 获取当前类型单位
     * @param title 标题名称
     * @return 单位
     */
    private String getUnit(String title) {
        String unit;
        if("体温".equals(title)) {
            unit = "°C";
        }else if ("血压".equals(title)) {
            unit = "mmHg";
        }else if ("血糖".equals(title)) {
            unit = "mmol/L";
        }else if ("心率".equals(title)) {
            unit = "次/分";
        }else if ("排便".equals(title)) {
            unit = "次/天";
        }else if ("呼吸".equals(title)) {
            unit = "次/分";
        }else if ("饮水".equals(title)) {
            unit = "ml";
        }else {
            unit = "";
        }
        return unit;
    }

    /**
     * 是否显示跳转设备采集页面
     * @param title 条目内容
     * @return true: 需要按钮; false: 不需要按钮
     */
    private boolean isShowDevice(String title) {
        if ("血压".equals(title)) {
            return true;
        }else if ("血糖".equals(title)) {
            return true;
        }
        return false;
    }

    /** editText输入框小数点后只能为1位,还有数字前面不可以0 */
    private void setPricePoint(final EditText editText, final int accur) {
        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (s.toString().contains(".")) {
                    // 如果字符串中有小数点则进行校验
                    if (s.length() - 1 - s.toString().indexOf(".") > accur) {
                        // 小数点后的数字个数大于1,则将小数点后除第一位都删除
                        s = s.toString().subSequence(0,
                                s.toString().indexOf(".") + accur + 1);
                        editText.setText(s);
                        // 设置光标的位置,使其一直保持在最右边
                        editText.setSelection(s.length());
                    }
                }
                if (".".equals(s.toString().trim())) {
                    // 以小数点为开始,在小数点的前面加上一个0
                    s = "0" + s;
                    editText.setText(s);
                    editText.setSelection(2);
                }

                if (s.toString().startsWith("0")
                        && s.toString().trim().length() > 1) {
                    // 开始位置上的数是0,且还有其他数字
                    if (!s.toString().substring(1, 2).equals(".")) {
                        // 第二位上的字符不为小数点,则无法继续输入
                        editText.setText(s.subSequence(0, 1));
                        editText.setSelection(1);
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void afterTextChanged(Editable s) {}

        });

    }

    /**
     * 点击事件触发接口回调函数
     */
    public interface ClickCallBack {

        /**
         * 跳转到外部设备采集事件触发
         */
        void deviceClick();

        /**
         * 保存数据事件触发
         * @param value 要保存的内容
         * @param color 当前内容的颜色
         */
        void saveClick(String value, int color);

    }

}
