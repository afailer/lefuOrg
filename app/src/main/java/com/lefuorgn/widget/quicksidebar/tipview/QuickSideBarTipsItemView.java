package com.lefuorgn.widget.quicksidebar.tipview;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.lefuorgn.R;

/**
 * 快速选择侧边栏，选择内容显示控件
 */

public class QuickSideBarTipsItemView extends View {

    private Path mBackgroundPath;

    private String mText; // 显示提示的内容
    private Paint mTextPaint; // 内容画笔
    private Paint mBackgroundPaint; // 背景画笔
    private int mWidth; // 当前空间的宽度
    private int mCenterTextStartX; // 文字X轴偏移量
    private int mCenterTextStartY; // 文字Y轴偏移量


    public QuickSideBarTipsItemView(Context context) {
        this(context, null);
    }

    public QuickSideBarTipsItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public QuickSideBarTipsItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        int textColor = context.getResources().getColor(android.R.color.black);
        int backgroundColor = context.getResources().getColor(android.R.color.darker_gray);
        float textSize = context.getResources().getDimension(R.dimen.textSize_quicksidebartips);
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.QuickSideBarView);
            textColor = a.getColor(R.styleable.QuickSideBarView_sidebarTextColor, textColor);
            backgroundColor = a.getColor(R.styleable.QuickSideBarView_sidebarBackgroundColor, backgroundColor);
            textSize = a.getDimension(R.styleable.QuickSideBarView_sidebarTextSize, textSize);
            a.recycle();
        }
        mText = "";
        mBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBackgroundPaint.setColor(backgroundColor);
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(textColor);
        mTextPaint.setTextSize(textSize);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mWidth = getMeasuredWidth();
        float height = getMeasuredWidth();
        float radius = mWidth * 0.5f;
        float[] radii;
        if(isRtl()) {
            radii = new float[]{radius, radius, radius, radius, radius, radius, 0f, 0f};
        }else {
            radii = new float[]{radius, radius, radius, radius, 0f, 0f, radius, radius};
        }
        RectF rectF = new RectF(0, 0, mWidth, height);
        mBackgroundPath = new Path();
        mBackgroundPath.addRoundRect(rectF, radii, Path.Direction.CW);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (TextUtils.isEmpty(mText)) {
            return;
        }
        canvas.drawColor(getResources().getColor(android.R.color.transparent));
        canvas.drawPath(mBackgroundPath, mBackgroundPaint);
        canvas.drawText(mText, mCenterTextStartX, mCenterTextStartY, mTextPaint);
    }

    /**
     * 设置文本内容
     * @param text 指定的文本内容
     */
    public void setText(String text) {
        mText = text;
        Rect rect = new Rect();
        mTextPaint.getTextBounds(mText, 0, mText.length(), rect);
        mCenterTextStartX = (int)((mWidth - rect.width()) * 0.5);
        mCenterTextStartY = mWidth - rect.height();
        invalidate();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public boolean isRtl() {
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) &&
                (getContext().getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_RTL);
    }
}
