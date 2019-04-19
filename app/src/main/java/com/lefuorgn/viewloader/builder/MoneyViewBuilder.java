package com.lefuorgn.viewloader.builder;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.lefuorgn.R;
import com.lefuorgn.util.StringUtils;
import com.lefuorgn.viewloader.base.LabelViewBuilder;

import org.json.JSONObject;


/**
 * 金额视图构建器
 */

public class MoneyViewBuilder extends LabelViewBuilder {

    private float mUnitSize; // 金额文字大小
    private int mUnitColor; // 金额文字颜色

    private EditText mValueView;
    private TextView mUnitView;

    public MoneyViewBuilder(Context context, int paddingWidth, int paddingHeight,
                            float labelSize, int labelColor, float valueSize, int valueColor,
                            float describeSize, int describeColor, int describeBackground,
                            float unitSize, int unitColor) {
        super(context, paddingWidth, paddingHeight, labelSize, labelColor, valueSize, valueColor,
                describeSize, describeColor, describeBackground);
        this.mUnitSize = unitSize;
        this.mUnitColor = unitColor;
    }

    @Override
    protected void initChildView(ViewGroup parent) {
        // 输入框
        mValueView = (EditText) mInflater.inflate(R.layout.builder_label_edit_text, parent, false);
        LayoutParams params = (LayoutParams) mValueView.getLayoutParams();
        params.width = 0;
        params.weight = 1;
        mValueView.setTextSize(mValueSize);
        mValueView.setTextColor(mValueColor);
        mValueView.setGravity(Gravity.END);
        mValueView.setMaxLines(1);
        setPricePoint(mValueView, 2);
        parent.addView(mValueView);
        // 货币类型
        mUnitView = new TextView(parent.getContext());
        LayoutParams unitParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        mUnitView.setLayoutParams(unitParams);
        mUnitView.setTextSize(mUnitSize);
        mUnitView.setTextColor(mUnitColor);
        mUnitView.setPadding(StringUtils.dip2px(mContext, 4), 0, 0, 0);
        parent.addView(mUnitView);
    }

    @Override
    protected void initValue(String value) {
        mValueView.setText(value);
    }

    @Override
    protected void initChildData(JSONObject jsonObject) {
        mUnitView.setText("(" + getString(jsonObject, "moneyType") + ")");
    }

    /**
     * editText输入框小数点后只能为accur位,还有数字前面不可以0
     * @param view 输入控件
     * @param accuracy 保留到小数点后位数
     */
    private void setPricePoint(final EditText view, final int accuracy) {
        view.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (s.toString().contains(".")) {
                    // 如果字符串中有小数点则进行校验
                    if (s.length() - 1 - s.toString().indexOf(".") > accuracy) {
                        // 小数点后的数字个数大于1,则将小数点后除第一位都删除
                        s = s.toString().subSequence(0,
                                s.toString().indexOf(".") + accuracy + 1);
                        view.setText(s);
                        // 设置光标的位置,使其一直保持在最右边
                        view.setSelection(s.length());
                    }
                }
                if (".".equals(s.toString().trim())) {
                    // 以小数点为开始,在小数点的前面加上一个0
                    s = "0" + s;
                    view.setText(s);
                    view.setSelection(2);
                }

                if (s.toString().startsWith("0")
                        && s.toString().trim().length() > 1) {
                    // 开始位置上的数是0,且还有其他数字
                    if (!s.toString().substring(1, 2).equals(".")) {
                        // 第二位上的字符不为小数点,则无法继续输入
                        view.setText(s.subSequence(0, 1));
                        view.setSelection(1);
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void afterTextChanged(Editable s) {}

        });

    }

    @Override
    public String getValue() {
        return mValueView.getText().toString();
    }
}
