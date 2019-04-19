package com.lefuorgn.viewloader.builder;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout.LayoutParams;

import com.lefuorgn.R;
import com.lefuorgn.util.StringUtils;
import com.lefuorgn.viewloader.base.LabelViewBuilder;

import org.json.JSONObject;


/**
 * 多行文本视图构建器
 */

public class TextAreaViewBuilder extends LabelViewBuilder {

    private EditText mValueView;

    public TextAreaViewBuilder(Context context, int paddingWidth, int paddingHeight,
                               float labelSize, int labelColor, float valueSize, int valueColor,
                               float describeSize, int describeColor, int describeBackground) {
        super(context, paddingWidth, paddingHeight, labelSize, labelColor, valueSize, valueColor,
                describeSize, describeColor, describeBackground);
    }

    @Override
    protected void initChildView(ViewGroup parent) {
        mValueView = new EditText(parent.getContext());
        mValueView.setTextSize(mValueSize);
        mValueView.setTextColor(mValueColor);
        mValueView.setBackgroundResource(R.drawable.edit_text_apply_background_shape);
        mValueView.setGravity(Gravity.TOP);
        int padding = StringUtils.dip2px(mContext, 6);
        mValueView.setPadding(padding, padding, padding, padding);
        parent.addView(mValueView);
    }

    @Override
    protected void initValue(String value) {
        mValueView.setText(value);
    }

    @Override
    protected void initChildData(JSONObject jsonObject) {
        String height = getString(jsonObject, "height");
        // 获取屏幕的高度
        WindowManager manager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        // 获取控件的配置文件
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        // 根据配置获取控件的高度
        if("small".equals(height)) {
            // small 小尺寸  屏幕高的10%
            params.height = (int) (outMetrics.heightPixels * 0.1);
        }else if("large".equals(height)) {
            // large 大尺寸  屏幕高的60%
            params.height = (int) (outMetrics.heightPixels * 0.6);
        }else {
            // medium 标准尺寸 屏幕高的20%
            params.height = (int) (outMetrics.heightPixels * 0.2);
        }
        params.setMargins(0, mPaddingHeight, 0, 0);
        mValueView.setLayoutParams(params);
    }

    @Override
    protected boolean isHorizontal() {
        return false;
    }

    @Override
    public String getValue() {
        return mValueView.getText().toString();
    }
}
