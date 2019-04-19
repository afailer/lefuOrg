package com.lefuorgn.viewloader.builder;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.lefuorgn.R;
import com.lefuorgn.viewloader.base.ViewBuilder;

import org.json.JSONObject;

/**
 * 描述文字视图构建器基类
 */

public class CommentViewBuilder extends ViewBuilder {

    private float mValueSize;
    private TextView mValueView;

    public CommentViewBuilder(Context context, int paddingWidth, int paddingHeight, float valueSize) {
        super(context, paddingWidth, paddingHeight);
        mValueSize = valueSize;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.builder_comment;
    }

    @Override
    protected void initView(View parent) {
        mValueView = (TextView) parent;
        mValueView.setTextSize(mValueSize);
        mValueView.setPadding(mPaddingWidth, mPaddingHeight, mPaddingWidth, mPaddingHeight);
    }

    @Override
    protected void initData(JSONObject jsonObject) {
        mValueView.setText(getString(jsonObject, "content").replaceAll("\r\n","\n").replaceAll("\r","\n"));
        mValueView.setTextColor(Color.parseColor(getString(jsonObject, "fontColor")));
        mValueView.setBackgroundColor(Color.parseColor(getString(jsonObject, "backgroundColor")));
    }
}
