package com.lefuorgn.viewloader.display;

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
import com.lefuorgn.viewloader.base.DisplayViewBuilder;

import org.json.JSONObject;

/**
 * 展示长文本文本视图构建器基类
 */

public class TextAreaDisplayBuilder extends DisplayViewBuilder {

    private EditText mValueView;

    public TextAreaDisplayBuilder(Context context, int paddingWidth, int paddingHeight,
                                  float labelSize, int labelColor, float valueSize, int valueColor) {
        super(context, paddingWidth, paddingHeight, labelSize, labelColor, valueSize, valueColor);
    }

    @Override
    protected void initChildView(ViewGroup parent) {
        mValueView = new EditText(parent.getContext());
        mValueView.setEnabled(false);
        mValueView.setTextSize(mValueSize);
        mValueView.setTextColor(mValueColor);
        mValueView.setBackgroundResource(R.drawable.edit_text_apply_background_shape);
        int padding = StringUtils.dip2px(mContext, 6);
        mValueView.setPadding(padding, padding, padding, padding);
        mValueView.setGravity(Gravity.TOP);
        mValueView.setMinLines(3);
        parent.addView(mValueView);
    }

    @Override
    protected void initChildData(JSONObject jsonObject) {
        // 获取屏幕的高度
        WindowManager manager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        mValueView.setLayoutParams(params);
        mValueView.setText(mValue);
    }

    @Override
    protected boolean isHorizontal() {
        return false;
    }

}
