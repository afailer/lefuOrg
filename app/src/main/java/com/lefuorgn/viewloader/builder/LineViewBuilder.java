package com.lefuorgn.viewloader.builder;

import android.content.Context;
import android.graphics.Color;
import android.view.View;

import com.lefuorgn.R;
import com.lefuorgn.viewloader.base.ViewBuilder;
import com.lefuorgn.viewloader.widget.LineView;

import org.json.JSONObject;

/**
 * 分割线视图构建器
 */

public class LineViewBuilder extends ViewBuilder {

    private LineView mLineView;

    public LineViewBuilder(Context context, int paddingWidth, int paddingHeight) {
        super(context, paddingWidth, paddingHeight);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.builder_line;
    }

    @Override
    protected void initView(View parent) {
        mLineView = (LineView) parent;
        mLineView.setPadding(mPaddingWidth, mPaddingHeight, mPaddingWidth, mPaddingHeight);
    }

    @Override
    protected void initData(JSONObject jsonObject) {
        mLineView.setLineColor(Color.parseColor(getString(jsonObject, "color")));
        mLineView.setLineType(getLineType(getString(jsonObject, "type")));
    }

    /**
     * 获取分割线类型
     */
    private int getLineType(String type) {
        if("dashed".equals(type)) {
            // 虚线
            return LineView.DOTTED_LINE;
        }else if("double".equals(type)) {
            // 双实线
            return LineView.DOUBLE_SOLID_LINE;
        }else {
            // 实线
            return LineView.SOLID_LINE;
        }
    }
}
