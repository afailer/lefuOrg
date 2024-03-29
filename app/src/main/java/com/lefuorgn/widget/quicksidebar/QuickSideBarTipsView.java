package com.lefuorgn.widget.quicksidebar;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.lefuorgn.widget.quicksidebar.tipview.QuickSideBarTipsItemView;

/**
 * 快速选择侧边栏，选择内容显示控件容器
 */

public class QuickSideBarTipsView extends RelativeLayout {

    private QuickSideBarTipsItemView mTipsView;

    public QuickSideBarTipsView(Context context) {
        this(context, null);
    }

    public QuickSideBarTipsView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public QuickSideBarTipsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        // 添加指示条目
        mTipsView = new QuickSideBarTipsItemView(context, attrs);
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        addView(mTipsView, layoutParams);
    }


    public void setText(String text,int poistion, float y){
        mTipsView.setText(text);
        LayoutParams layoutParams = (LayoutParams) mTipsView.getLayoutParams();
        layoutParams.topMargin = (int)(y - getWidth() / 2.8);
        mTipsView.setLayoutParams(layoutParams);
    }

}
